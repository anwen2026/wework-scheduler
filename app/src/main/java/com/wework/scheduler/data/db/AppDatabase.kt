package com.wework.scheduler.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wework.scheduler.data.db.entities.*

/**
 * 应用数据库
 */
@Database(
    entities = [
        ScheduleTask::class,
        MessageQueue::class,
        SendPlan::class,
        AppConfig::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun scheduleTaskDao(): ScheduleTaskDao
    abstract fun messageQueueDao(): MessageQueueDao
    abstract fun sendPlanDao(): SendPlanDao
    abstract fun appConfigDao(): AppConfigDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private const val DATABASE_NAME = "wework_scheduler.db"
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // 数据库创建时的初始化操作
                        // 可以在这里插入默认配置
                    }
                })
                .build()
        }
        
        /**
         * 初始化默认配置
         */
        suspend fun initDefaultConfig(context: Context) {
            val db = getInstance(context)
            val configDao = db.appConfigDao()
            
            // 检查是否已初始化
            val existingConfig = configDao.getByKey(ConfigKeys.DEFAULT_INTERVAL_SECONDS)
            if (existingConfig != null) {
                return // 已初始化，跳过
            }
            
            // 插入默认配置
            configDao.insertAll(
                listOf(
                    AppConfig(
                        key = ConfigKeys.DEFAULT_INTERVAL_SECONDS,
                        value = "30",
                        type = ConfigType.INT,
                        description = "默认消息间隔（秒）"
                    ),
                    AppConfig(
                        key = ConfigKeys.CANCEL_CODE,
                        value = "取消排单",
                        type = ConfigType.STRING,
                        description = "撤销码"
                    ),
                    AppConfig(
                        key = ConfigKeys.AUTO_REPLY_ENABLED,
                        value = "true",
                        type = ConfigType.BOOLEAN,
                        description = "是否启用自动回复"
                    ),
                    AppConfig(
                        key = ConfigKeys.SCHEDULE_GROUP_IDS,
                        value = "[]",
                        type = ConfigType.JSON,
                        description = "排单群 ID 列表"
                    ),
                    AppConfig(
                        key = ConfigKeys.LOG_ENABLED,
                        value = "true",
                        type = ConfigType.BOOLEAN,
                        description = "是否启用日志"
                    )
                )
            )
        }
    }
}
