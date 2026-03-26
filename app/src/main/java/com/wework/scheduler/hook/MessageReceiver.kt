package com.wework.scheduler.hook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.wework.scheduler.data.db.AppDatabase
import com.wework.scheduler.service.CommandParser
import com.wework.scheduler.service.ScheduleService
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 消息接收器
 * 通过 Hook 数据库插入来拦截消息
 */
class MessageReceiver(
    private val lpparam: XC_LoadPackage.LoadPackageParam,
    private val context: Context
) {
    private val classLoader = lpparam.classLoader
    private val database = AppDatabase.getInstance(context)
    private val commandParser = CommandParser()
    private val scheduleService = ScheduleService(context)
    private val scope = CoroutineScope(Dispatchers.IO)
    
    // 当前活跃的任务（每个群一个）
    private val activeTaskMap = mutableMapOf<String, Long>()
    
    /**
     * 初始化 Hook
     */
    fun init() {
        try {
            // Hook SQLiteDatabase 的 insert 方法
            hookDatabaseInsert()
            
            log("MessageReceiver initialized")
        } catch (e: Exception) {
            log("MessageReceiver init failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * Hook 数据库插入方法
     * 企业微信的消息都会插入到数据库，我们拦截这个操作
     */
    private fun hookDatabaseInsert() {
        try {
            XposedHelpers.findAndHookMethod(
                "android.database.sqlite.SQLiteDatabase",
                classLoader,
                "insert",
                String::class.java,  // table
                String::class.java,  // nullColumnHack
                ContentValues::class.java,  // values
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            val table = param.args[0] as? String ?: return
                            val values = param.args[2] as? ContentValues ?: return
                            
                            // 只处理消息表
                            if (table.contains("message", ignoreCase = true) ||
                                table.contains("msg", ignoreCase = true)) {
                                
                                handleMessageInsert(values)
                            }
                        } catch (e: Exception) {
                            // 忽略错误，避免影响企业微信正常运行
                        }
                    }
                }
            )
            
            log("Hooked SQLiteDatabase.insert")
            
        } catch (e: Exception) {
            log("hookDatabaseInsert error: ${e.message}")
        }
    }
    
    /**
     * 处理消息插入
     */
    private fun handleMessageInsert(values: ContentValues) {
        scope.launch {
            try {
                // 提取消息信息
                val conversationId = values.getAsString("conversationId") ?: 
                                    values.getAsString("conversation_id") ?: 
                                    values.getAsLong("conversationId")?.toString() ?: return@launch
                
                val content = values.getAsString("content") ?: 
                             values.getAsString("text") ?: return@launch
                
                val messageType = values.getAsInteger("type") ?: 
                                 values.getAsInteger("msgType") ?: 1
                
                val senderId = values.getAsString("senderId") ?: 
                              values.getAsString("sender_id") ?: 
                              values.getAsLong("senderId")?.toString() ?: return@launch
                
                log("Message inserted: conversationId=$conversationId, content=$content, type=$messageType")
                
                // 检查是否为排单群
                if (!isScheduleGroup(conversationId)) {
                    return@launch
                }
                
                // 检查是否为自己发送的消息（避免循环）
                if (isSelfMessage(senderId)) {
                    return@launch
                }
                
                // 解析指令
                val command = commandParser.parse(content, messageType)
                
                // 处理指令
                handleCommand(conversationId, command, values)
                
            } catch (e: Exception) {
                log("handleMessageInsert error: ${e.message}")
            }
        }
    }
    
    /**
     * 处理指令
     */
    private suspend fun handleCommand(
        groupId: String,
        command: com.wework.scheduler.service.Command,
        messageValues: ContentValues
    ) {
        // TODO: 实现指令处理逻辑
        // 这里会调用之前写好的 ScheduleService
        
        when (command) {
            is com.wework.scheduler.service.Command.CreateTask -> {
                // 创建任务
                log("Create task: ${command.time}")
            }
            is com.wework.scheduler.service.Command.AddMessage -> {
                // 添加消息到队列
                log("Add message: ${command.content}")
            }
            else -> {
                // 其他指令
            }
        }
    }
    
    /**
     * 检查是否为排单群
     */
    private suspend fun isScheduleGroup(groupId: String): Boolean {
        // TODO: 从配置中读取排单群列表
        // 暂时返回 true，后续完善
        return true
    }
    
    /**
     * 检查是否为自己发送的消息
     */
    private fun isSelfMessage(senderId: String): Boolean {
        // TODO: 获取当前用户 ID 并比较
        // 暂时返回 false
        return false
    }
    
    private fun log(message: String) {
        XposedBridge.log("[WeWorkScheduler-Receiver] $message")
    }
}
