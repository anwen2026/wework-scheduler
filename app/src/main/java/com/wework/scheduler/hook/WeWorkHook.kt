package com.wework.scheduler.hook

import android.app.Application
import android.content.Context
import com.wework.scheduler.data.db.AppDatabase
import com.wework.scheduler.service.SendWorker
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Xposed 模块主入口
 */
class WeWorkHook : IXposedHookLoadPackage {
    
    companion object {
        private const val WEWORK_PACKAGE = "com.tencent.wework"
        private const val TAG = "WeWorkScheduler"
    }
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // 只 Hook 企业微信
        if (lpparam.packageName != WEWORK_PACKAGE) {
            return
        }
        
        log("WeWork Scheduler module loaded")
        
        try {
            // Hook Application 初始化
            hookApplication(lpparam)
        } catch (e: Exception) {
            log("Hook failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * Hook Application 初始化
     * 在企业微信启动时初始化我们的模块
     */
    private fun hookApplication(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            Application::class.java,
            "attach",
            Context::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    try {
                        val context = param.args[0] as Context
                        log("Application attached, context: ${context.packageName}")
                        
                        if (context.packageName == WEWORK_PACKAGE) {
                            initModule(context, lpparam)
                        }
                    } catch (e: Exception) {
                        log("Application hook error: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        )
    }
    
    /**
     * 初始化模块
     */
    private fun initModule(context: Context, lpparam: XC_LoadPackage.LoadPackageParam) {
        log("Initializing WeWork Scheduler module...")
        
        try {
            // 初始化数据库
            initDatabase(context)
            
            // 初始化消息发送器
            val messageSender = MessageSender(lpparam)
            messageSender.init()
            
            // 初始化消息接收器
            val messageReceiver = MessageReceiver(lpparam, context)
            messageReceiver.init()
            
            // 启动定时发送服务
            SendWorker.start(context)
            
            log("WeWork Scheduler module initialized successfully")
            
        } catch (e: Exception) {
            log("Module initialization failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * 初始化数据库
     */
    private fun initDatabase(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                AppDatabase.initDefaultConfig(context)
                log("Database initialized")
            } catch (e: Exception) {
                log("Database init error: ${e.message}")
            }
        }
    }
    
    private fun log(message: String) {
        XposedBridge.log("[$TAG] $message")
    }
}
