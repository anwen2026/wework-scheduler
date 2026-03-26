package com.wework.scheduler.data.db

import androidx.room.*
import com.wework.scheduler.data.db.entities.SendPlan
import kotlinx.coroutines.flow.Flow

/**
 * 发单方案 DAO
 */
@Dao
interface SendPlanDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plan: SendPlan): Long
    
    @Update
    suspend fun update(plan: SendPlan)
    
    @Delete
    suspend fun delete(plan: SendPlan)
    
    @Query("SELECT * FROM send_plans WHERE id = :id")
    suspend fun getById(id: Long): SendPlan?
    
    @Query("SELECT * FROM send_plans ORDER BY createdAt DESC")
    suspend fun getAll(): List<SendPlan>
    
    @Query("SELECT * FROM send_plans ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<SendPlan>>
    
    @Query("SELECT * FROM send_plans WHERE enabled = 1 ORDER BY createdAt DESC")
    suspend fun getEnabled(): List<SendPlan>
    
    @Query("SELECT * FROM send_plans WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefault(): SendPlan?
    
    @Query("UPDATE send_plans SET isDefault = 0")
    suspend fun clearAllDefaults()
    
    @Query("UPDATE send_plans SET isDefault = 1 WHERE id = :id")
    suspend fun setAsDefault(id: Long)
    
    @Query("UPDATE send_plans SET enabled = :enabled WHERE id = :id")
    suspend fun updateEnabled(id: Long, enabled: Boolean)
    
    @Query("SELECT COUNT(*) FROM send_plans")
    suspend fun count(): Int
    
    @Transaction
    suspend fun setDefaultPlan(id: Long) {
        clearAllDefaults()
        setAsDefault(id)
    }
}
