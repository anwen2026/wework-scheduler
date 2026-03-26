package com.wework.scheduler.service

import android.content.Context
import androidx.work.*
import com.wework.scheduler.data.db.AppDatabase
import com.wework.scheduler.data.db.entities.MessageStatus
import com.wework.scheduler.data.db.entities.TaskStatus
import com.wework.scheduler.hook.MessageSender
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * 定时发送 Worker
 * 每分钟检查一次，发送到时间的消息
 */
class SendWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val database = AppDatabase.getInstance(context)
    
    override suspend fun doWork(): Result {
        return try {
            log("SendWorker started")
            
            val now = System.currentTimeMillis()
            
            // 查询所有运行中的任务
            val runningTasks = database.scheduleTaskDao()
                .getByStatus(TaskStatus.RUNNING)
            
            log("Found ${runningTasks.size} running tasks")
            
            for (task in runningTasks) {
                try {
                    // 查询该任务下到时间的待发送消息
                    val messages = database.messageQueueDao()
                        .getPendingMessagesBeforeTimeByTask(task.id, now)
                    
                    if (messages.isEmpty()) {
                        continue
                    }
                    
                    log("Task ${task.id}: ${messages.size} messages to send")
                    
                    // 逐条发送
                    for (message in messages) {
                        try {
                            // 更新状态为发送中
                            database.messageQueueDao().updateStatus(
                                message.id,
                                MessageStatus.SENDING
                            )
                            
                            // 发送消息
                            val success = sendMessage(message)
                            
                            if (success) {
                                // 更新状态为已发送
                                database.messageQueueDao().updateStatus(
                                    message.id,
                                    MessageStatus.SENT,
                                    sentAt = System.currentTimeMillis()
                                )
                                
                                log("Message ${message.id} sent successfully")
                                
                                // 发送确认回复
                                sendConfirmation(task.groupId, message.order + 1)
                                
                            } else {
                                // 更新状态为失败
                                database.messageQueueDao().updateStatus(
                                    message.id,
                                    MessageStatus.FAILED
                                )
                                
                                log("Message ${message.id} send failed")
                            }
                            
                            // 等待间隔时间（避免发送过快）
                            delay(1000L)
                            
                        } catch (e: Exception) {
                            log("Error sending message ${message.id}: ${e.message}")
                            database.messageQueueDao().updateStatus(
                                message.id,
                                MessageStatus.FAILED
                            )
                        }
                    }
                    
                    // 检查任务是否完成
                    checkTaskCompletion(task.id)
                    
                } catch (e: Exception) {
                    log("Error processing task ${task.id}: ${e.message}")
                }
            }
            
            log("SendWorker completed")
            Result.success()
            
        } catch (e: Exception) {
            log("SendWorker error: ${e.message}")
            Result.retry()
        }
    }
    
    /**
     * 发送消息
     */
    private suspend fun sendMessage(message: com.wework.scheduler.data.db.entities.MessageQueue): Boolean {
        // TODO: 获取 MessageSender 实例并发送
        // 这里需要通过某种方式获取 Xposed Hook 的 MessageSender
        // 可以使用单例模式或依赖注入
        
        // 暂时返回 true，实际实现需要调用 MessageSender
        return true
    }
    
    /**
     * 发送确认消息
     */
    private suspend fun sendConfirmation(groupId: String, order: Int) {
        // TODO: 发送确认消息
        // "✅ 已发送第 N 条消息"
    }
    
    /**
     * 检查任务是否完成
     */
    private suspend fun checkTaskCompletion(taskId: Long) {
        val pendingCount = database.messageQueueDao()
            .countByStatus(taskId, MessageStatus.PENDING)
        
        if (pendingCount == 0) {
            // 所有消息已发送，标记任务为完成
            database.scheduleTaskDao().updateStatus(
                taskId,
                TaskStatus.COMPLETED
            )
            
            log("Task $taskId completed")
        }
    }
    
    private fun log(message: String) {
        android.util.Log.d("WeWorkScheduler-Worker", message)
    }
    
    companion object {
        private const val WORK_NAME = "send_worker"
        
        /**
         * 启动定时任务
         */
        fun start(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .build()
            
            val workRequest = PeriodicWorkRequestBuilder<SendWorker>(
                1, TimeUnit.MINUTES  // 每分钟执行一次
            )
                .setConstraints(constraints)
                .setInitialDelay(0, TimeUnit.SECONDS)
                .build()
            
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
        
        /**
         * 停止定时任务
         */
        fun stop(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(WORK_NAME)
        }
    }
}
