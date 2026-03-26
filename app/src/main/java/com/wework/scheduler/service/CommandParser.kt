package com.wework.scheduler.service

/**
 * 指令解析器
 * 将用户输入的文本解析为具体的指令
 */
class CommandParser {
    
    /**
     * 解析指令
     * @param text 用户输入的文本
     * @param messageType 消息类型（用于判断是否为普通消息）
     * @return 解析后的指令
     */
    fun parse(text: String, messageType: Int = 1): Command {
        val trimmed = text.trim()
        
        // 空消息
        if (trimmed.isEmpty()) {
            return Command.Unknown
        }
        
        // 非文字消息（图片、视频等）直接作为普通消息
        if (messageType != 1) {
            return Command.AddMessage(text)
        }
        
        // 检查是否为撤销码（8位字母数字）
        val cancelCodes = com.wework.scheduler.utils.CancelCodeGenerator.parseCodes(trimmed)
        if (cancelCodes.isNotEmpty()) {
            return Command.Revoke(cancelCodes)
        }
        
        // 解析文字指令
        return when {
            // hwsd HH:MM - 创建任务
            trimmed.matches(Regex("^hwsd\\s+\\d{1,2}:\\d{2}$", RegexOption.IGNORE_CASE)) -> {
                val time = trimmed.substring(4).trim()
                Command.CreateTask(time)
            }
            
            // 停止
            trimmed in listOf("停止", "暂停", "stop", "pause") -> {
                Command.Stop
            }
            
            // 开始
            trimmed in listOf("开始", "继续", "恢复", "start", "resume", "continue") -> {
                Command.Start
            }
            
            // 取消任务
            trimmed in listOf("取消任务", "取消", "cancel") -> {
                Command.Cancel
            }
            
            // 状态
            trimmed in listOf("状态", "查询", "status") -> {
                Command.Status
            }
            
            // 清空
            trimmed in listOf("清空", "清空排单", "clear") -> {
                Command.Clear
            }
            
            // 确认清空
            trimmed in listOf("确认清空", "confirm clear") -> {
                Command.ConfirmClear
            }
            
            // 延迟 N 分钟
            trimmed.matches(Regex("^延迟\\s*\\d+\\s*分钟?$")) -> {
                val minutes = extractNumber(trimmed)
                Command.Delay(minutes)
            }
            
            // 提前 N 分钟
            trimmed.matches(Regex("^提前\\s*\\d+\\s*分钟?$")) -> {
                val minutes = extractNumber(trimmed)
                Command.Advance(minutes)
            }
            
            // 修改间隔 N 秒
            trimmed.matches(Regex("^间隔\\s*\\d+\\s*秒?$")) -> {
                val seconds = extractNumber(trimmed)
                Command.SetInterval(seconds)
            }
            
            // 帮助
            trimmed in listOf("帮助", "help", "?", "？") -> {
                Command.Help
            }
            
            // 列表
            trimmed in listOf("列表", "任务列表", "list") -> {
                Command.List
            }
            
            // 历史
            trimmed in listOf("历史", "历史记录", "history") -> {
                Command.History
            }
            
            // 设置发单方案
            trimmed.startsWith("方案") -> {
                val planName = trimmed.substring(2).trim()
                if (planName.isEmpty()) {
                    Command.ListPlans
                } else {
                    Command.SetPlan(planName)
                }
            }
            
            // 其他情况作为普通消息
            else -> {
                Command.AddMessage(text)
            }
        }
    }
    
    /**
     * 从文本中提取数字
     */
    private fun extractNumber(text: String): Int {
        val regex = Regex("\\d+")
        val match = regex.find(text)
        return match?.value?.toIntOrNull() ?: 0
    }
}

/**
 * 指令类型
 */
sealed class Command {
    
    /** 创建任务 (hwsd HH:MM) */
    data class CreateTask(val time: String) : Command()
    
    /** 停止 */
    object Stop : Command()
    
    /** 开始 */
    object Start : Command()
    
    /** 取消任务 */
    object Cancel : Command()
    
    /** 撤销消息（支持多个撤销码） */
    data class Revoke(val codes: List<String>) : Command()
    
    /** 查询状态 */
    object Status : Command()
    
    /** 清空排单 */
    object Clear : Command()
    
    /** 确认清空 */
    object ConfirmClear : Command()
    
    /** 延迟 N 分钟 */
    data class Delay(val minutes: Int) : Command()
    
    /** 提前 N 分钟 */
    data class Advance(val minutes: Int) : Command()
    
    /** 设置消息间隔 */
    data class SetInterval(val seconds: Int) : Command()
    
    /** 帮助 */
    object Help : Command()
    
    /** 任务列表 */
    object List : Command()
    
    /** 历史记录 */
    object History : Command()
    
    /** 列出发单方案 */
    object ListPlans : Command()
    
    /** 设置发单方案 */
    data class SetPlan(val planName: String) : Command()
    
    /** 添加普通消息 */
    data class AddMessage(val content: String) : Command()
    
    /** 未知指令 */
    object Unknown : Command()
}
