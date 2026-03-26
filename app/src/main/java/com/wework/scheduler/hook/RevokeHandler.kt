package com.wework.scheduler.hook

import com.wework.scheduler.data.db.AppDatabase
import com.wework.scheduler.data.db.entities.MessageStatus
import com.wework.scheduler.service.Command
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 撤销处理器
 * 处理撤销指令
 */
class RevokeHandler(
    private val database: AppDatabase,
    private val messageSender: MessageSender
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    
    /**
     * 处理撤销指令
     * @param groupId 群 ID
     * @param taskId 任务 ID
     * @param command 撤销指令（包含一个或多个撤销码）
     */
    fun handle(groupId: String, taskId: Long, command: Command.Revoke) {
        scope.launch {
            try {
                val codes = command.codes
                
                if (codes.isEmpty()) {
                    sendReply(groupId, "❌ 无效的撤销码")
                    return@launch
                }
                
                var successCount = 0
                var notFoundCount = 0
                var alreadyProcessedCount = 0
                val revokedMessages = mutableListOf<String>()
                
                for (code in codes) {
                    val result = revokeByCode(taskId, code)
                    when (result) {
                        is RevokeResult.Success -> {
                            successCount++
                            revokedMessages.add(result.preview)
                        }
                        is RevokeResult.NotFound -> {
                            notFoundCount++
                        }
                        is RevokeResult.AlreadyProcessed -> {
                            alreadyProcessedCount++
                        }
                    }
                }
                
                // 构建回复消息
                val reply = buildString {
                    if (successCount > 0) {
                        appendLine("✅ 已撤销 $successCount 条消息")
                        if (revokedMessages.size <= 3) {
                            // 少于3条，显示详情
                            revokedMessages.forEach { appendLine(it) }
                        }
                    }
                    if (notFoundCount > 0) {
                        appendLine("❌ $notFoundCount 个撤销码无效")
                    }
                    if (alreadyProcessedCount > 0) {
                        appendLine("⚠️ $alreadyProcessedCount 条消息已处理，无法撤销")
                    }
                }
                
                sendReply(groupId, reply.trim())
                
            } catch (e: Exception) {
                sendReply(groupId, "❌ 撤销失败：${e.message}")
            }
        }
    }
    
    /**
     * 根据撤销码撤销消息
     */
    private suspend fun revokeByCode(taskId: Long, code: String): RevokeResult {
        // 查询所有消息
        val messages = database.messageQueueDao().getByTaskId(taskId)
        
        // 查找匹配的消息（通过 extraData 中存储的撤销码）
        val message = messages.find { msg ->
            // 从 extraData 中提取撤销码
            val storedCode = extractCancelCode(msg.extraData)
            storedCode == code
        }
        
        if (message == null) {
            return RevokeResult.NotFound
        }
        
        if (message.status != MessageStatus.PENDING) {
            return RevokeResult.AlreadyProcessed(getStatusText(message.status))
        }
        
        // 撤销消息
        database.messageQueueDao().updateStatus(message.id, MessageStatus.CANCELLED)
        
        val preview = getMessagePreview(message)
        return RevokeResult.Success(preview)
    }
    
    /**
     * 从 extraData 中提取撤销码
     */
    private fun extractCancelCode(extraData: String?): String? {
        if (extraData == null) return null
        
        try {
            // extraData 是 JSON 格式，如 {"cancelCode": "W4SDH56Y"}
            val json = org.json.JSONObject(extraData)
            return json.optString("cancelCode", null)
        } catch (e: Exception) {
            return null
        }
    }
    
    private fun getStatusText(status: MessageStatus): String {
        return when (status) {
            MessageStatus.SENT -> "已发送"
            MessageStatus.SENDING -> "发送中"
            MessageStatus.CANCELLED -> "已取消"
            MessageStatus.SKIPPED -> "已跳过"
            MessageStatus.FAILED -> "发送失败"
            else -> "已处理"
        }
    }
    
    private fun getMessagePreview(message: com.wework.scheduler.data.db.entities.MessageQueue): String {
        val typeEmoji = when (message.messageType) {
            com.wework.scheduler.data.db.entities.MessageType.TEXT -> "💬"
            com.wework.scheduler.data.db.entities.MessageType.IMAGE -> "🖼️"
            com.wework.scheduler.data.db.entities.MessageType.VIDEO -> "🎬"
            com.wework.scheduler.data.db.entities.MessageType.EMOJI -> "😊"
            com.wework.scheduler.data.db.entities.MessageType.CHANNELS -> "📺"
            else -> "📄"
        }
        
        val content = when (message.messageType) {
            com.wework.scheduler.data.db.entities.MessageType.TEXT -> {
                val preview = message.content.take(15)
                preview + if (message.content.length > 15) "..." else ""
            }
            com.wework.scheduler.data.db.entities.MessageType.IMAGE -> "图片"
            com.wework.scheduler.data.db.entities.MessageType.VIDEO -> "视频"
            com.wework.scheduler.data.db.entities.MessageType.EMOJI -> "表情"
            com.wework.scheduler.data.db.entities.MessageType.CHANNELS -> "视频号"
            else -> "消息"
        }
        
        return "$typeEmoji $content"
    }
    
    private fun sendReply(groupId: String, content: String) {
        messageSender.sendTextMessage(groupId, content)
    }
}

/**
 * 撤销结果
 */
sealed class RevokeResult {
    /** 撤销成功 */
    data class Success(val preview: String) : RevokeResult()
    
    /** 撤销码不存在 */
    object NotFound : RevokeResult()
    
    /** 消息已处理，无法撤销 */
    data class AlreadyProcessed(val status: String) : RevokeResult()
}
