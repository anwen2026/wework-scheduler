package com.wework.scheduler.hook

import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * 消息发送器
 * 通过 Hook 企业微信的 IMsg 接口实现消息发送
 */
class MessageSender(
    private val lpparam: XC_LoadPackage.LoadPackageParam
) {
    private val classLoader = lpparam.classLoader
    
    // IMsg 接口实例（运行时获取）
    private var msgApi: Any? = null
    
    // Context（企业微信的 Application Context）
    private var appContext: Context? = null
    
    /**
     * 初始化 Hook
     */
    fun init() {
        try {
            // Hook Application 初始化，获取 Context
            hookApplicationContext()
            
            // Hook IMsg 接口的获取方法
            hookMsgApiInstance()
            
            log("MessageSender initialized")
        } catch (e: Exception) {
            log("MessageSender init failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * Hook Application Context
     */
    private fun hookApplicationContext() {
        XposedHelpers.findAndHookMethod(
            "android.app.Application",
            classLoader,
            "attach",
            Context::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val context = param.args[0] as Context
                    if (context.packageName == "com.tencent.wework") {
                        appContext = context
                        log("Got Application Context")
                    }
                }
            }
        )
    }
    
    /**
     * Hook IMsg 接口实例获取
     */
    private fun hookMsgApiInstance() {
        try {
            // 方法1：Hook IMsg 的静态获取方法
            val msgApiClass = XposedHelpers.findClass(
                "com.tencent.wework.msg.api.IMsg",
                classLoader
            )
            
            // 查找所有静态方法，找到返回 IMsg 的方法
            val methods = msgApiClass.declaredMethods
            for (method in methods) {
                if (method.returnType == msgApiClass && 
                    java.lang.reflect.Modifier.isStatic(method.modifiers)) {
                    
                    XposedBridge.hookMethod(method, object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            msgApi = param.result
                            log("Got IMsg instance via ${method.name}")
                        }
                    })
                }
            }
            
        } catch (e: Exception) {
            log("hookMsgApiInstance error: ${e.message}")
        }
    }
    
    /**
     * 发送文字消息
     * @param conversationId 会话ID（群ID或个人ID）
     * @param content 消息内容
     * @return 是否发送成功
     */
    fun sendTextMessage(conversationId: String, content: String): Boolean {
        return try {
            if (msgApi == null || appContext == null) {
                log("msgApi or appContext is null")
                return false
            }
            
            // 创建 Conversation 对象
            val conversationClass = XposedHelpers.findClass(
                "com.tencent.wework.foundation.model.Conversation",
                classLoader
            )
            val conversation = XposedHelpers.newInstance(conversationClass)
            
            // 设置会话ID
            XposedHelpers.setLongField(conversation, "conversationId", conversationId.toLong())
            
            // 创建 Message 对象
            val messageClass = XposedHelpers.findClass(
                "com.tencent.wework.foundation.model.Message",
                classLoader
            )
            val message = XposedHelpers.newInstance(messageClass)
            
            // 设置消息类型为文字（type = 1）
            XposedHelpers.setIntField(message, "type", 1)
            
            // 设置消息内容
            XposedHelpers.setObjectField(message, "content", content)
            
            // 调用 sendMessage 方法
            // void sendMessage(Context context, Conversation conversation, Message message, 
            //                  ze50 ze50Var, ISendMessageCallback iSendMessageCallback)
            XposedHelpers.callMethod(
                msgApi,
                "sendMessage",
                appContext,
                conversation,
                message,
                null,  // ze50Var (可能是额外参数，传 null)
                null   // callback
            )
            
            log("sendTextMessage success: $content")
            true
            
        } catch (e: Exception) {
            log("sendTextMessage failed: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 发送图片消息
     * @param conversationId 会话ID
     * @param imagePath 图片本地路径
     * @return 是否发送成功
     */
    fun sendImageMessage(conversationId: String, imagePath: String): Boolean {
        return try {
            if (msgApi == null || appContext == null) {
                log("msgApi or appContext is null")
                return false
            }
            
            // 调用 sendImageMessage 方法
            // boolean sendImageMessage(Context context, long j2, String str, ze50 ze50Var)
            val result = XposedHelpers.callMethod(
                msgApi,
                "sendImageMessage",
                appContext,
                conversationId.toLong(),
                imagePath,
                null  // ze50Var
            ) as? Boolean ?: false
            
            log("sendImageMessage result: $result")
            result
            
        } catch (e: Exception) {
            log("sendImageMessage failed: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 发送视频消息
     */
    fun sendVideoMessage(conversationId: String, videoPath: String): Boolean {
        return try {
            if (msgApi == null || appContext == null) {
                return false
            }
            
            // 创建 Conversation
            val conversationClass = XposedHelpers.findClass(
                "com.tencent.wework.foundation.model.Conversation",
                classLoader
            )
            val conversation = XposedHelpers.newInstance(conversationClass)
            XposedHelpers.setLongField(conversation, "conversationId", conversationId.toLong())
            
            // 创建 Message
            val messageClass = XposedHelpers.findClass(
                "com.tencent.wework.foundation.model.Message",
                classLoader
            )
            val message = XposedHelpers.newInstance(messageClass)
            
            // 设置消息类型为视频（type = 43）
            XposedHelpers.setIntField(message, "type", 43)
            XposedHelpers.setObjectField(message, "content", videoPath)
            
            // 发送
            XposedHelpers.callMethod(
                msgApi,
                "sendMessage",
                appContext,
                conversation,
                message,
                null,
                null
            )
            
            log("sendVideoMessage success")
            true
            
        } catch (e: Exception) {
            log("sendVideoMessage failed: ${e.message}")
            false
        }
    }
    
    /**
     * 转发消息（用于视频号等特殊消息）
     */
    fun forwardMessage(conversationId: String, originalMessage: Any): Boolean {
        return try {
            if (msgApi == null || appContext == null) {
                return false
            }
            
            // 创建目标 Conversation
            val conversationClass = XposedHelpers.findClass(
                "com.tencent.wework.foundation.model.Conversation",
                classLoader
            )
            val conversation = XposedHelpers.newInstance(conversationClass)
            XposedHelpers.setLongField(conversation, "conversationId", conversationId.toLong())
            
            // 调用转发方法
            XposedHelpers.callMethod(
                msgApi,
                "sendMessage",
                appContext,
                conversation,
                originalMessage,  // 直接传原始消息对象
                null,
                null
            )
            
            log("forwardMessage success")
            true
            
        } catch (e: Exception) {
            log("forwardMessage failed: ${e.message}")
            false
        }
    }
    
    private fun log(message: String) {
        XposedBridge.log("[WeWorkScheduler-Sender] $message")
    }
}
