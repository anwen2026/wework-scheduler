package com.wework.scheduler.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 排单任务表
 * 每个任务对应一次 "hwsd HH:MM" 指令
 */
@Entity(tableName = "schedule_tasks")
data class ScheduleTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** 排单群 ID */
    val groupId: String,
    
    /** 排单群名称 */
    val groupName: String,
    
    /** 发送时间（时间戳，毫秒） */
    val sendTime: Long,
    
    /** 消息间隔（秒） */
    val intervalSeconds: Int = 30,
    
    /** 任务状态 */
    val status: TaskStatus = TaskStatus.PENDING,
    
    /** 发单方案 ID（关联 SendPlan） */
    val sendPlanId: Long? = null,
    
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis(),
    
    /** 更新时间 */
    val updatedAt: Long = System.currentTimeMillis(),
    
    /** 暂停时间 */
    val pausedAt: Long? = null,
    
    /** 原始指令消息 ID（用于撤销） */
    val commandMessageId: String? = null
)

/**
 * 任务状态枚举
 */
enum class TaskStatus {
    /** 待发送 */
    PENDING,
    
    /** 发送中 */
    SENDING,
    
    /** 运行中 */
    RUNNING,
    
    /** 已暂停 */
    PAUSED,
    
    /** 已完成 */
    COMPLETED,
    
    /** 已取消 */
    CANCELLED,
    
    /** 发送失败 */
    FAILED
}
