package com.wework.scheduler.utils

import java.security.SecureRandom

/**
 * 撤销码生成器
 * 生成 15 位随机字母数字组合
 */
object CancelCodeGenerator {
    
    private const val CODE_LENGTH = 15
    private const val CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // 去掉易混淆的字符 I, O, 0, 1
    private val random = SecureRandom()
    
    /**
     * 生成唯一的撤销码
     * @return 15位随机码，如 W4SDH56YK7MN3PQ
     */
    fun generate(): String {
        return buildString {
            repeat(CODE_LENGTH) {
                append(CHARS[random.nextInt(CHARS.length)])
            }
        }
    }
    
    /**
     * 验证撤销码格式
     * @param code 撤销码
     * @return 是否为有效格式
     */
    fun isValid(code: String): Boolean {
        return code.matches(Regex("^[A-Z0-9]{15}$"))
    }
    
    /**
     * 解析撤销指令
     * 支持单个或多个撤销码（每行一个）
     * @param text 用户输入
     * @return 撤销码列表
     */
    fun parseCodes(text: String): List<String> {
        val lines = text.trim().lines()
        return lines
            .map { it.trim().uppercase() }
            .filter { isValid(it) }
    }
}
