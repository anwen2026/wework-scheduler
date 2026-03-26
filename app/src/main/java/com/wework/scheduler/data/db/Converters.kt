package com.wework.scheduler.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wework.scheduler.data.db.entities.*

/**
 * Room 数据库类型转换器
 * 用于将复杂类型转换为数据库支持的基本类型
 */
class Converters {
    
    private val gson = Gson()
    
    // ========== TaskStatus 转换 ==========
    
    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String {
        return value.name
    }
    
    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus {
        return try {
            TaskStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            TaskStatus.PENDING
        }
    }
    
    // ========== MessageType 转换 ==========
    
    @TypeConverter
    fun fromMessageType(value: MessageType): String {
        return value.name
    }
    
    @TypeConverter
    fun toMessageType(value: String): MessageType {
        return try {
            MessageType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            MessageType.TEXT
        }
    }
    
    // ========== MessageStatus 转换 ==========
    
    @TypeConverter
    fun fromMessageStatus(value: MessageStatus): String {
        return value.name
    }
    
    @TypeConverter
    fun toMessageStatus(value: String): MessageStatus {
        return try {
            MessageStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            MessageStatus.PENDING
        }
    }
    
    // ========== ConfigType 转换 ==========
    
    @TypeConverter
    fun fromConfigType(value: ConfigType): String {
        return value.name
    }
    
    @TypeConverter
    fun toConfigType(value: String): ConfigType {
        return try {
            ConfigType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            ConfigType.STRING
        }
    }
    
    // ========== List<TargetGroup> 转换 ==========
    
    @TypeConverter
    fun fromTargetGroupList(value: List<TargetGroup>): String {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toTargetGroupList(value: String): List<TargetGroup> {
        return try {
            val type = object : TypeToken<List<TargetGroup>>() {}.type
            gson.fromJson(value, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // ========== List<String> 转换 ==========
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // ========== Map<String, Any> 转换（用于 extraData）==========
    
    @TypeConverter
    fun fromMap(value: Map<String, Any>?): String? {
        return value?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toMap(value: String?): Map<String, Any>? {
        return try {
            value?.let {
                val type = object : TypeToken<Map<String, Any>>() {}.type
                gson.fromJson(it, type)
            }
        } catch (e: Exception) {
            null
        }
    }
}
