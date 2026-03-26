package com.wework.scheduler.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 消息队列表
 * 存储每个任务下的所有待发送消息
 */
@Entity(
    tableName = "message_queue",
    foreignKeys = [
        ForeignKey(
            entity = ScheduleTask::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("taskId"), Index("scheduledTime")]
)
data class MessageQueue(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** 关联的任务 ID */
    val taskId: Long,
    
    /** 消息类型 */
    val messageType: MessageType,
    
    /** 消息内容（文字内容或文件路径） */
    val content: String,
    
    /** 消息额外数据（JSON 格式，存储图片尺寸、视频时长等） */
    val extraData: String? = null,
    
    /** 发送顺序（从 0 开始） */
    val order: Int,
    
    /** 消息状态 */
    val status: MessageStatus = MessageStatus.PENDING,
    
    /** 计划发送时间（时间戳，毫秒） */
    val scheduledTime: Long,
    
    /** 实际发送时间 */
    val sentAt: Long? = null,
    
    /** 原始消息 ID（企业微信的消息 ID） */
    val originalMessageId: String? = null,
    
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 消息类型枚举
 */
enum class MessageType {
    /** 文字消息 */
    TEXT,
    
    /** 图片消息 */
    IMAGE,
    
    /** 视频消息 */
    VIDEO,
    
    /** 表情消息 */
    EMOJI,
    
    /** 视频号消息 */
    CHANNELS,
    
    /** 文件消息 */
    FILE,
    
    /** 语音消息 */
    VOICE,
    
    /** 链接消息 */
    LINK,
    
    /** 小程序消息 */
    MINIPROGRAM
}

/**
 * 消息状态枚举
 */
enum class MessageStatus {
    /** 待发送 */
    PENDING,
    
    /** 发送中 */
    SENDING,
    
    /** 已发送 */
    SENT,
    
    /** 已跳过 */
    SKIPPED,
    
    /** 已取消 */
    CANCELLED,
    
    /** 发送失败 */
    FAILED
}
