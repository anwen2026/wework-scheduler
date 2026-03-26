package com.wework.scheduler.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * 视频号消息发送器
 */
class ChannelsMessageSender(
    private val classLoader: ClassLoader
) {
    
    /**
     * 发送视频号消息（通过转发）
     * @param conversationId 目标会话ID
     * @param channelsData 视频号消息数据（从原始消息提取）
     */
    fun sendChannelsMessage(
        conversationId: String,
        channelsData: ChannelsMessageData
    ): Boolean {
        return try {
            // 方法1：调用转发接口
            forwardChannelsMessage(conversationId, channelsData)
        } catch (e: Exception) {
            log("sendChannelsMessage failed: ${e.message}")
            
            // 方法2：构造消息对象发送
            try {
                constructAndSendChannelsMessage(conversationId, channelsData)
            } catch (e2: Exception) {
                log("constructAndSendChannelsMessage failed: ${e2.message}")
                false
            }
        }
    }
    
    /**
     * 方法1：通过转发接口发送
     * 企业微信有专门的转发方法，直接调用
     */
    private fun forwardChannelsMessage(
        conversationId: String,
        channelsData: ChannelsMessageData
    ): Boolean {
        // 获取消息控制器
        val msgControllerClass = XposedHelpers.findClass(
            "com.tencent.wework.msg.api.IMsgController",
            classLoader
        )
        val msgController = XposedHelpers.callStaticMethod(
            msgControllerClass,
            "getInstance"
        )
        
        // 调用转发方法
        // forwardMessage(conversationId, originalMessage)
        val result = XposedHelpers.callMethod(
            msgController,
            "forwardMessage",
            conversationId,
            channelsData.originalMessageObject  // 原始消息对象
        ) as? Boolean ?: false
        
        log("forwardChannelsMessage result: $result")
        return result
    }
    
    /**
     * 方法2：构造视频号消息对象发送
     * 如果转发失败，尝试手动构造消息
     */
    private fun constructAndSendChannelsMessage(
        conversationId: String,
        channelsData: ChannelsMessageData
    ): Boolean {
        // 1. 创建视频号消息对象
        val channelsMessageClass = XposedHelpers.findClass(
            "com.tencent.wework.msg.model.ChannelsMessage",
            classLoader
        )
        
        val channelsMessage = XposedHelpers.newInstance(channelsMessageClass)
        
        // 2. 设置字段
        XposedHelpers.setObjectField(channelsMessage, "feedId", channelsData.feedId)
        XposedHelpers.setObjectField(channelsMessage, "nonceId", channelsData.nonceId)
        XposedHelpers.setObjectField(channelsMessage, "thumbUrl", channelsData.thumbUrl)
        XposedHelpers.setObjectField(channelsMessage, "title", channelsData.title)
        XposedHelpers.setObjectField(channelsMessage, "desc", channelsData.desc)
        XposedHelpers.setObjectField(channelsMessage, "username", channelsData.username)
        XposedHelpers.setObjectField(channelsMessage, "avatar", channelsData.avatar)
        XposedHelpers.setIntField(channelsMessage, "mediaType", channelsData.mediaType)
        XposedHelpers.setIntField(channelsMessage, "duration", channelsData.duration)
        
        // 3. 发送
        val msgControllerClass = XposedHelpers.findClass(
            "com.tencent.wework.msg.api.IMsgController",
            classLoader
        )
        val msgController = XposedHelpers.callStaticMethod(
            msgControllerClass,
            "getInstance"
        )
        
        val result = XposedHelpers.callMethod(
            msgController,
            "sendChannelsMessage",
            conversationId,
            channelsMessage
        ) as? Boolean ?: false
        
        log("constructAndSendChannelsMessage result: $result")
        return result
    }
    
    private fun log(message: String) {
        android.util.Log.d("WeWorkScheduler", message)
    }
}

/**
 * 视频号消息数据
 */
data class ChannelsMessageData(
    val feedId: String,              // 视频号 Feed ID
    val nonceId: String,             // 唯一标识
    val thumbUrl: String,            // 封面图 URL
    val title: String,               // 标题
    val desc: String,                // 描述
    val username: String,            // 视频号名称
    val avatar: String,              // 头像 URL
    val mediaType: Int,              // 1=视频, 2=图片
    val duration: Int,               // 时长（秒）
    val originalMessageObject: Any? = null  // 原始消息对象（用于转发）
)
