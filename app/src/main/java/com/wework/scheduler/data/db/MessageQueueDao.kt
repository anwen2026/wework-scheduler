package com.wework.scheduler.data.db

import androidx.room.*
import com.wework.scheduler.data.db.entities.*
import kotlinx.coroutines.flow.Flow

/**
 * 消息队列 DAO
 */
@Dao
interface MessageQueueDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageQueue): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageQueue>): List<Long>
    
    @Update
    suspend fun update(message: MessageQueue)
    
    @Delete
    suspend fun delete(message: MessageQueue)
    
    @Query("SELECT * FROM message_queue WHERE id = :id")
    suspend fun getById(id: Long): MessageQueue?
    
    @Query("SELECT * FROM message_queue WHERE taskId = :taskId ORDER BY `order` ASC")
    suspend fun getByTaskId(taskId: Long): List<MessageQueue>
    
    @Query("SELECT * FROM message_queue WHERE taskId = :taskId ORDER BY `order` ASC")
    fun getByTaskIdFlow(taskId: Long): Flow<List<MessageQueue>>
    
    @Query("SELECT * FROM message_queue WHERE taskId = :taskId AND status = :status ORDER BY `order` ASC")
    suspend fun getByTaskIdAndStatus(taskId: Long, status: MessageStatus): List<MessageQueue>
    
    @Query("SELECT * FROM message_queue WHERE status = 'PENDING' AND scheduledTime <= :time ORDER BY scheduledTime ASC")
    suspend fun getPendingMessagesBeforeTime(time: Long): List<MessageQueue>
    
    @Query("SELECT * FROM message_queue WHERE taskId = :taskId AND status = 'PENDING' AND scheduledTime <= :time ORDER BY scheduledTime ASC")
    suspend fun getPendingMessagesBeforeTimeByTask(taskId: Long, time: Long): List<MessageQueue>
    
    @Query("UPDATE message_queue SET status = :status, sentAt = :sentAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: MessageStatus, sentAt: Long? = null)
    
    @Query("UPDATE message_queue SET status = :status WHERE taskId = :taskId AND scheduledTime BETWEEN :startTime AND :endTime AND status = 'PENDING'")
    suspend fun markSkippedBetween(taskId: Long, startTime: Long, endTime: Long, status: MessageStatus)
    
    @Query("SELECT * FROM message_queue WHERE taskId = :taskId AND status = 'PENDING' ORDER BY `order` DESC LIMIT 1")
    suspend fun getLastPendingMessage(taskId: Long): MessageQueue?
    
    @Query("SELECT COUNT(*) FROM message_queue WHERE taskId = :taskId AND status = :status")
    suspend fun countByStatus(taskId: Long, status: MessageStatus): Int
    
    @Query("SELECT COUNT(*) FROM message_queue WHERE taskId = :taskId")
    suspend fun countByTaskId(taskId: Long): Int
    
    @Query("DELETE FROM message_queue WHERE taskId = :taskId")
    suspend fun deleteByTaskId(taskId: Long)
    
    @Query("DELETE FROM message_queue WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT MAX(`order`) FROM message_queue WHERE taskId = :taskId")
    suspend fun getMaxOrder(taskId: Long): Int?
    
    @Query("UPDATE message_queue SET scheduledTime = scheduledTime + :offsetMillis WHERE taskId = :taskId AND status = 'PENDING'")
    suspend fun adjustScheduledTime(taskId: Long, offsetMillis: Long)
    
    @Query("SELECT * FROM message_queue WHERE originalMessageId = :messageId LIMIT 1")
    suspend fun getByOriginalMessageId(messageId: String): MessageQueue?
}
