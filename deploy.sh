#!/bin/bash

echo "🚀 企业微信排单助手 - 自动部署脚本"
echo ""

# 检查是否已经配置 Git
if ! git config user.name > /dev/null 2>&1; then
    echo "⚙️  配置 Git 用户信息..."
    read -p "请输入你的 GitHub 用户名: " username
    read -p "请输入你的邮箱: " email
    git config --global user.name "$username"
    git config --global user.email "$email"
fi

# 检查是否已经有远程仓库
if ! git remote get-url origin > /dev/null 2>&1; then
    echo ""
    echo "📝 请按照以下步骤操作："
    echo ""
    echo "1. 打开 https://github.com/new"
    echo "2. 仓库名称填写: wework-scheduler"
    echo "3. 选择 Public 或 Private"
    echo "4. 不要勾选 'Initialize this repository with a README'"
    echo "5. 点击 'Create repository'"
    echo ""
    read -p "创建完成后，输入仓库 URL (如 https://github.com/你的用户名/wework-scheduler.git): " repo_url
    
    git remote add origin "$repo_url"
fi

# 推送到 GitHub
echo ""
echo "📤 推送代码到 GitHub..."
git push -u origin main

echo ""
echo "✅ 完成！"
echo ""
echo "📦 GitHub Actions 正在自动编译 APK..."
echo "🔗 查看进度: https://github.com/你的用户名/wework-scheduler/actions"
echo ""
echo "⏱️  编译需要 5-10 分钟"
echo "📥 编译完成后，在 Actions 页面下载 APK"
echo ""
