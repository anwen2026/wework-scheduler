package com.wework.scheduler.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wework.scheduler.data.db.AppDatabase
import kotlinx.coroutines.launch

/**
 * 主界面
 * 显示模块状态和基本配置
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    var moduleStatus by remember { mutableStateOf("检查中...") }
    var taskCount by remember { mutableStateOf(0) }
    
    LaunchedEffect(Unit) {
        // TODO: 检查模块状态
        moduleStatus = "✅ 模块已激活"
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("企业微信排单助手") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 模块状态卡片
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "模块状态",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = moduleStatus,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            // 使用说明卡片
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "使用说明",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = """
                            1. 在 LSPosed 中激活本模块
                            2. 重启企业微信
                            3. 在排单群发送指令：
                            
                            • hwsd 18:00 - 创建排单任务
                            • 发送消息 - 加入排单队列
                            • 停止 - 暂停排单
                            • 开始 - 恢复排单
                            • 状态 - 查看任务状态
                            • 撤销码 - 撤销指定消息
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // 功能按钮
            Button(
                onClick = {
                    scope.launch {
                        // TODO: 打开设置
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("打开设置")
            }
            
            Button(
                onClick = {
                    scope.launch {
                        // TODO: 查看任务列表
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("查看任务列表")
            }
        }
    }
}
