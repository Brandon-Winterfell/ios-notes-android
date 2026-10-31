# 苹果 iOS 备忘录 Android App (完全复刻版)

本项目是深度复刻苹果 iOS 备忘录人机交互、字体（San Francisco 比例）、间距和视觉风格的 Android 应用程序。

完全针对**免本地开发环境**设计：利用 GitHub Actions 云端流水线，**无需电脑安装 Android Studio 或 JDK**，即可在 GitHub 上自动编译生成可安装的 APK 安装包！

---

## 🚀 3 步利用 GitHub 免费编译出 APK 安装包

### 第一步：在 GitHub 上新建仓库
1. 登录你的 GitHub 账号（若无账号可免费注册一个）。
2. 点击右上角的 **+** -> **New repository**。
3. 仓库名称填入：`ios-notes-android`（也可以填任意名称）。
4. 选择 **Public** 或 **Private**（私有仓库每月有 2000 分钟免费 Actions 构建时长，完全够用）。
5. 点击下方的 **Create repository**。

### 第二步：上传/推送本项目代码
你有两种非常简单的方法将代码放进 GitHub：

- **方法 A（无需终端，纯浏览器拖拽上传）**：
  1. 在本项目页面点击 **「一键下载 Android 完整工程 ZIP」**，在本地电脑解压。
  2. 打开刚才创建好的 GitHub 仓库页面，点击 **"uploading an existing file"**（上传现有文件）。
  3. 将解压出来的所有文件和文件夹（特别注意确保包含 `.github` 隐藏文件夹）直接拖入 GitHub 浏览器上传窗口。
  4. 点击下方的绿色按钮 **Commit changes** 提交。

- **方法 B（使用 Git 命令行）**：
  ```bash
  git init
  git add .
  git commit -m "feat: iOS Notes clone for Android"
  git branch -M main
  git remote add origin https://github.com/你的用户名/ios-notes-android.git
  git push -u origin main
  ```

### 第三步：在 GitHub Actions 中下载 APK
1. 打开你的 GitHub 仓库页面，点击顶部的 **Actions** 选项卡。
2. 你会看到名为 **"Build Android APK"** 的流水线正在自动运行（显示黄色旋转圆圈）。
3. 大约等待 **1 到 2 分钟**，流水线变成绿色对勾 ✔️ 即表示构建完成！
4. 点击该次构建记录，滚动到底部的 **Artifacts（构建产物）** 区域。
5. 点击下载 **`iOS-Notes-Android-Debug-APK`**。
6. 解压下载的 zip 包即可获得 **`app-debug.apk`**，发送到安卓手机即可直接安装体验！

---

## 🎨 苹果 iOS 备忘录核心细节复刻说明

1. **字体与间距 (Typography & Spacing)**：
   - 遵照苹果人机交互指南（Apple HIG），大标题采用 34sp 加粗，正文采用 17sp，辅助提示为 13sp。
   - 列表分组采用 iOS 经典 **Inset Grouped** 圆角卡片，边距精准控制为 16dp，卡片圆角 12dp，分割线采用 0.5dp 细线并缩进 16dp。
2. **调色板 (Color Palette)**：
   - 采用苹果备忘录经典的标志性琥珀黄（`#E4A93C`），完美适配系统夜间模式（Dark Mode）。
3. **功能结构**：
   - 文件夹层级（iCloud、备忘录、最近删除）。
   - 搜索栏实时高亮过滤。
   - 备忘录置顶（Pinned）与未置顶分栏显示。
   - 核对清单（Checklist）打勾动画与划线。
