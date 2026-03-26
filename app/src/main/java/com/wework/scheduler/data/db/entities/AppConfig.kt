package com.wework.scheduler.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 配置表
 * 存储全局配置项
 */
@Entity(tableName = "app_config")
data class AppConfig(
    @PrimaryKey
    val key: String,
    
    /** 配置值 */
    val value: String,
    
    /** 配置类型（用于类型转换） */
    val type: ConfigType = ConfigType.STRING,
    
    /** 配置描述 */
    val description: String? = null,
    
    /** 更新时间 */
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * 配置类型枚举
 */
enum class ConfigType {
    STRING,
    INT,
    LONG,
    BOOLEAN,
    FLOAT,
    JSON
}

/**
 * 预定义的配置键
 */
object ConfigKeys {
    /** 默认消息间隔（秒） */
    const val DEFAULT_INTERVAL_SECONDS = "default_interval_seconds"
    
    /** 撤销码 */
    const val CANCEL_CODE = "cancel_code"
    
    /** 是否启用自动回复 */
    const val AUTO_REPLY_ENABLED = "auto_reply_enabled"
    
    /** 排单群 ID 列表（JSON 数组） */
    const val SCHEDULE_GROUP_IDS = "schedule_group_ids"
    
    /** 是否启用日志 */
    const val LOG_ENABLED = "log_enabled"
    
    /** 默认发单方案 ID */
    const val DEFAULT_SEND_PLAN_ID = "default_send_plan_id"
}
