package com.wework.scheduler.service

import android.content.Context
import com.wework.scheduler.data.db.AppDatabase
import com.wework.scheduler.data.db.entities.*
import com.wework.scheduler.utils.CancelCodeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * 排单服务
 * 处理排单相关的业务逻辑
 */
class ScheduleService(private val context: Context) {
    
    private val database = AppDatabase.getInstance(context)
    
    /**
     * 创建排单任务
     */
    suspend fun createTask(
        groupId: String,
        groupName: String,
        sendTime: Long,
        intervalSeconds: Int,
        sendPlanId: Long?
    ): ScheduleTask = withContext(Dispatchers.IO) {
        val task = ScheduleTask(
            groupId = groupId,
            groupName = groupName,
            sendTime = sendTime,
            intervalSeconds = intervalSeconds,
            status = TaskStatus.PENDING,
            sendPlanId = sendPlanId
        )
        
        val taskId = database.scheduleTaskDao().insert(task)
        task.copy(id = taskId)
    }
    
    /**
     * 添加消息到队列
     */
    suspend fun addMessage(
        taskId: Long,
        messageType: MessageType,
        content: String,
        extraData: Map<String, Any>? = null
    ): MessageQueue = withContext(Dispatchers.IO) {
        val task = database.scheduleTaskDao().getById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        // 获取当前最大序号
        val maxOrder = database.messageQueueDao().getMaxOrder(taskId) ?: -1
        val order = maxOrder + 1
        
        // 计算计划发送时间
        val scheduledTime = task.sendTime + (order * task.intervalSeconds * 1000L)
        
        // 生成撤销码
        val cancelCode = CancelCodeGenerator.generate()
        
        // 构建 extraData JSON
        val extraDataJson = buildExtraData(cancelCode, extraData)
        
        // 创建消息队列记录
        val message = MessageQueue(
            taskId = taskId,
            messageType = messageType,
            content = content,
            extraData = extraDataJson,
            order = order,
            scheduledTime = scheduledTime
        )
        
        val messageId = database.messageQueueDao().insert(message)
        message.copy(id = messageId)
    }
    
    /**
     * 暂停任务
     */
    suspend fun pauseTask(taskId: Long) = withContext(Dispatchers.IO) {
        database.scheduleTaskDao().updateStatus(taskId, TaskStatus.PAUSED)
        database.scheduleTaskDao().updatePausedAt(taskId, System.currentTimeMillis())
    }
    
    /**
     * 恢复任务
     */
    suspend fun resumeTask(taskId: Long) = withContext(Dispatchers.IO) {
        val task = database.scheduleTaskDao().getById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        val pausedAt = task.pausedAt ?: return@withContext
        val now = System.currentTimeMillis()
        
        // 标记停止期间的消息为已跳过
        database.messageQueueDao().markSkippedBetween(
            taskId = taskId,
            startTime = pausedAt,
            endTime = now,
            status = MessageStatus.SKIPPED
        )
        
        // 更新任务状态
        database.scheduleTaskDao().updateStatus(taskId, TaskStatus.RUNNING)
        database.scheduleTaskDao().updatePausedAt(taskId, null)
    }
    
    /**
     * 取消任务
     */
    suspend fun cancelTask(taskId: Long) = withContext(Dispatchers.IO) {
        database.scheduleTaskDao().updateStatus(taskId, TaskStatus.CANCELLED)
        
        // 取消所有待发送的消息
        val pendingMessages = database.messageQueueDao()
            .getByTaskIdAndStatus(taskId, MessageStatus.PENDING)
        
        for (message in pendingMessages) {
            database.messageQueueDao().updateStatus(message.id, MessageStatus.CANCELLED)
        }
    }
    
    /**
     * 延迟任务
     */
    suspend fun delayTask(taskId: Long, minutes: Int) = withContext(Dispatchers.IO) {
        val offsetMillis = minutes * 60 * 1000L
        
        // 更新任务发送时间
        val task = database.scheduleTaskDao().getById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        val newSendTime = task.sendTime + offsetMillis
        database.scheduleTaskDao().update(task.copy(sendTime = newSendTime))
        
        // 更新所有待发送消息的时间
        database.messageQueueDao().adjustScheduledTime(taskId, offsetMillis)
    }
    
    /**
     * 提前任务
     */
    suspend fun advanceTask(taskId: Long, minutes: Int) = withContext(Dispatchers.IO) {
        val offsetMillis = -minutes * 60 * 1000L
        
        // 更新任务发送时间
        val task = database.scheduleTaskDao().getById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        val newSendTime = task.sendTime + offsetMillis
        
        // 确保不会提前到过去
        if (newSendTime < System.currentTimeMillis()) {
            throw IllegalArgumentException("Cannot advance to past time")
        }
        
        database.scheduleTaskDao().update(task.copy(sendTime = newSendTime))
        
        // 更新所有待发送消息的时间
        database.messageQueueDao().adjustScheduledTime(taskId, offsetMillis)
    }
    
    /**
     * 修改消息间隔
     */
    suspend fun setInterval(taskId: Long, seconds: Int) = withContext(Dispatchers.IO) {
        val task = database.scheduleTaskDao().getById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        // 更新任务间隔
        database.scheduleTaskDao().update(task.copy(intervalSeconds = seconds))
        
        // 重新计算所有待发送消息的时间
        val messages = database.messageQueueDao()
            .getByTaskIdAndStatus(taskId, MessageStatus.PENDING)
        
        for (message in messages) {
            val newScheduledTime = task.sendTime + (message.order * seconds * 1000L)
            database.messageQueueDao().update(
                message.copy(scheduledTime = newScheduledTime)
            )
        }
    }
    
    /**
     * 获取任务统计信息
     */
    suspend fun getTaskStats(taskId: Long): TaskStats = withContext(Dispatchers.IO) {
        val task = database.scheduleTaskDao().getById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        val totalCount = database.messageQueueDao().countByTaskId(taskId)
        val sentCount = database.messageQueueDao().countByStatus(taskId, MessageStatus.SENT)
        val pendingCount = database.messageQueueDao().countByStatus(taskId, MessageStatus.PENDING)
        val skippedCount = database.messageQueueDao().countByStatus(taskId, MessageStatus.SKIPPED)
        val cancelledCount = database.messageQueueDao().countByStatus(taskId, MessageStatus.CANCELLED)
        val failedCount = database.messageQueueDao().countByStatus(taskId, MessageStatus.FAILED)
        
        TaskStats(
            task = task,
            totalCount = totalCount,
            sentCount = sentCount,
            pendingCount = pendingCount,
            skippedCount = skippedCount,
            cancelledCount = cancelledCount,
            failedCount = failedCount
        )
    }
    
    /**
     * 格式化时间
     */
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * 格式化日期时间
     */
    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    /**
     * 格式化时长
     */
    fun formatDuration(millis: Long): String {
        if (millis < 0) return "已过期"
        
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        
        return when {
            hours > 0 -> "${hours}小时${minutes}分"
            minutes > 0 -> "${minutes}分钟"
            else -> "即将发送"
        }
    }
    
    /**
     * 构建 extraData JSON
     */
    private fun buildExtraData(cancelCode: String, extraData: Map<String, Any>?): String {
        val json = JSONObject()
        json.put("cancelCode", cancelCode)
        
        extraData?.forEach { (key, value) ->
            json.put(key, value)
        }
        
        return json.toString()
    }
}

/**
 * 任务统计信息
 */
data class TaskStats(
    val task: ScheduleTask,
    val totalCount: Int,
    val sentCount: Int,
    val pendingCount: Int,
    val skippedCount: Int,
    val cancelledCount: Int,
    val failedCount: Int
)
