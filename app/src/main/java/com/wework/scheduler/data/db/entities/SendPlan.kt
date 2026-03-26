package com.wework.scheduler.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.wework.scheduler.data.db.Converters

/**
 * 发单方案表
 * 定义消息要发送到哪些群
 */
@Entity(tableName = "send_plans")
@TypeConverters(Converters::class)
data class SendPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** 方案名称 */
    val name: String,
    
    /** 目标群 ID 列表 */
    val targetGroups: List<TargetGroup>,
    
    /** 是否为默认方案 */
    val isDefault: Boolean = false,
    
    /** 是否启用 */
    val enabled: Boolean = true,
    
    /** 创建时间 */
    val createdAt: Long = System.currentTimeMillis(),
    
    /** 更新时间 */
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * 目标群信息
 */
data class TargetGroup(
    /** 群 ID */
    val groupId: String,
    
    /** 群名称 */
    val groupName: String,
    
    /** 是否启用 */
    val enabled: Boolean = true
)
