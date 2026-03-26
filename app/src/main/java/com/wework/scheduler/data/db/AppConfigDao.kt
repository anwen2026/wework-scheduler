package com.wework.scheduler.data.db

import androidx.room.*
import com.wework.scheduler.data.db.entities.AppConfig
import com.wework.scheduler.data.db.entities.ConfigType
import kotlinx.coroutines.flow.Flow

/**
 * 配置 DAO
 */
@Dao
interface AppConfigDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: AppConfig)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(configs: List<AppConfig>)
    
    @Update
    suspend fun update(config: AppConfig)
    
    @Delete
    suspend fun delete(config: AppConfig)
    
    @Query("SELECT * FROM app_config WHERE key = :key")
    suspend fun getByKey(key: String): AppConfig?
    
    @Query("SELECT * FROM app_config")
    suspend fun getAll(): List<AppConfig>
    
    @Query("SELECT * FROM app_config")
    fun getAllFlow(): Flow<List<AppConfig>>
    
    @Query("SELECT value FROM app_config WHERE key = :key")
    suspend fun getValue(key: String): String?
    
    @Query("UPDATE app_config SET value = :value, updatedAt = :updatedAt WHERE key = :key")
    suspend fun updateValue(key: String, value: String, updatedAt: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM app_config WHERE key = :key")
    suspend fun deleteByKey(key: String)
    
    // 便捷方法：获取特定类型的配置值
    
    suspend fun getString(key: String, defaultValue: String = ""): String {
        return getValue(key) ?: defaultValue
    }
    
    suspend fun getInt(key: String, defaultValue: Int = 0): Int {
        return getValue(key)?.toIntOrNull() ?: defaultValue
    }
    
    suspend fun getLong(key: String, defaultValue: Long = 0L): Long {
        return getValue(key)?.toLongOrNull() ?: defaultValue
    }
    
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return getValue(key)?.toBooleanStrictOrNull() ?: defaultValue
    }
    
    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return getValue(key)?.toFloatOrNull() ?: defaultValue
    }
    
    // 便捷方法：设置配置值
    
    suspend fun setString(key: String, value: String, description: String? = null) {
        insert(AppConfig(key, value, ConfigType.STRING, description))
    }
    
    suspend fun setInt(key: String, value: Int, description: String? = null) {
        insert(AppConfig(key, value.toString(), ConfigType.INT, description))
    }
    
    suspend fun setLong(key: String, value: Long, description: String? = null) {
        insert(AppConfig(key, value.toString(), ConfigType.LONG, description))
    }
    
    suspend fun setBoolean(key: String, value: Boolean, description: String? = null) {
        insert(AppConfig(key, value.toString(), ConfigType.BOOLEAN, description))
    }
    
    suspend fun setFloat(key: String, value: Float, description: String? = null) {
        insert(AppConfig(key, value.toString(), ConfigType.FLOAT, description))
    }
}
