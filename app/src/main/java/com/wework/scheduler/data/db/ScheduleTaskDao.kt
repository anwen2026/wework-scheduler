package com.wework.scheduler.data.db

import androidx.room.*
import com.wework.scheduler.data.db.entities.*
import kotlinx.coroutines.flow.Flow

/**
 * 排单任务 DAO
 */
@Dao
interface ScheduleTaskDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: ScheduleTask): Long
    
    @Update
    suspend fun update(task: ScheduleTask)
    
    @Delete
    suspend fun delete(task: ScheduleTask)
    
    @Query("SELECT * FROM schedule_tasks WHERE id = :id")
    suspend fun getById(id: Long): ScheduleTask?
    
    @Query("SELECT * FROM schedule_tasks WHERE groupId = :groupId ORDER BY createdAt DESC")
    suspend fun getByGroupId(groupId: String): List<ScheduleTask>
    
    @Query("SELECT * FROM schedule_tasks WHERE status = :status ORDER BY sendTime ASC")
    suspend fun getByStatus(status: TaskStatus): List<ScheduleTask>
    
    @Query("SELECT * FROM schedule_tasks ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<ScheduleTask>>
    
    @Query("SELECT * FROM schedule_tasks ORDER BY createdAt DESC")
    suspend fun getAll(): List<ScheduleTask>
    
    @Query("UPDATE schedule_tasks SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: TaskStatus, updatedAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE schedule_tasks SET pausedAt = :pausedAt WHERE id = :id")
    suspend fun updatePausedAt(id: Long, pausedAt: Long?)
    
    @Query("SELECT * FROM schedule_tasks WHERE status IN ('PENDING', 'RUNNING') AND sendTime <= :time ORDER BY sendTime ASC")
    suspend fun getActiveTasksBeforeTime(time: Long): List<ScheduleTask>
    
    @Query("DELETE FROM schedule_tasks WHERE status = 'COMPLETED' AND updatedAt < :beforeTime")
    suspend fun deleteCompletedBefore(beforeTime: Long)
    
    @Query("SELECT COUNT(*) FROM schedule_tasks WHERE status = :status")
    suspend fun countByStatus(status: TaskStatus): Int
    
    @Query("SELECT * FROM schedule_tasks WHERE groupId = :groupId AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestActiveTask(groupId: String): ScheduleTask?
}
