确保你已经安装了anaconda
## 第一步：生成配置文件

若从未生成过 Jupyter 配置文件，需先执行命令创建：

一. 打开**命令提示符（CMD）** 或 **PowerShell**
二. 输入以下命令并回车：
```
jupyter notebook --generate-config
```

> 前置要求：需先在系统环境变量 Path 中配置 Anaconda 地址，PowerShell 才能识别该命令
> 1. 在 Windows 搜索栏输入 **“环境变量”**，选择 **“编辑系统环境变量”**。
    
2. 点击右下角的 **“环境变量”** 按钮。
    
3. 在下方的 **“系统变量”** 列表中找到 **`Path`**，双击打开。
    
4. 点击右侧的 **“新建”**，把你 D 盘 Anaconda 的以下三个路径加进去（假设你安装在`D:\Anaconda3`，请根据你真实的 D 盘安装路径修改）：
- `D:\Anaconda3`
    
- `D:\Anaconda3\Scripts` _(Jupyter 核心程序在这里)_
    
- `D:\Anaconda3\Library\bin`
    

5. 一路点击 **“确定”** 保存。
    
6. **重启 PowerShell**，再输入 `jupyter notebook` 就能直接识别并从 D 盘启动了！


三. 终端会输出配置文件路径，默认格式：
    
    `Writing default config to: C:\Users\用户名\.jupyter\jupyter_notebook_config.py`

---

## 第二步：修改配置文件

1. 按照终端输出的路径，找到 `jupyter_notebook_config.py` 文件
2. 用文本编辑器打开（推荐：VS Code）
3. 在文件中搜索代码（在vscode中的搜素中搜索notebook_dir）：
```
# c.ServerApp.notebook_dir = ''
```

4. 修改配置（**必须删除开头的 # 取消注释**），单引号内填写你想设置的 D 盘路径：原本的 `c.NotebookApp.notebook_dir` 在新版本中已经被废弃，改为了 **`c.ServerApp.root_dir`**。
```
# 示例：D盘新建JupyterWorkspace文件夹
c.ServerApp.root_dir = 'D:/JupyterWorkspace'
```
**记得保存**
### ⚠️ 重要避坑指南

1. 必须**手动提前在 D 盘创建好目标文件夹**（如 `JupyterWorkspace`）
    
2. 路径分隔符只能用 **正斜杠 `/`** 或 **双反斜杠 `\\`**
    
    正确示例：`'D:\\JupyterWorkspace'` 或 `'D:/JupyterWorkspace'`
    
3. 保存文件并关闭编辑器
    
---

## 第三步：修复快捷方式（关键步骤）

完成前两步后，点击快捷方式仍打开 C 盘，是因为快捷方式自带强制目录参数，必须删除：

1. 找到开始菜单 / 桌面的 **Jupyter Notebook 快捷方式**
2. 右键 → **属性**
3. 切换到 **快捷方式** 选项卡
4. 找到 **目标 (Target)** 输入框，删除末尾的 `%USERPROFILE%`
    
    - 修改前：`...jupyter-notebook-script.py %USERPROFILE%`
    - 修改后：`...jupyter-notebook-script.py`（保留前面原有空格）
    
5. 清空 **起始位置 (Start in)** 内容，或直接填写 D 盘目标路径（如 `D:\JupyterWorkspace`）
6. 点击**确定**，若提示管理员权限，选择继续即可

---

### 如果你想让终端（Anaconda Prompt）一打开就直接显示 D 盘

如果你希望截图里的这个黑框框一打开就直接是 `(base) D:\programme_data\python>`，可以这样改：

1. 在开始菜单找到 **Anaconda Prompt** 的图标，右键 -> **更多** -> **打开文件位置**。
    
2. 在打开的文件夹里，右键点击 **Anaconda Prompt** 的快捷方式，选择 **属性**。
    
3. 把 **“起始位置”** 后面的 `%USERPROFILE%` 删掉，直接改成你的 D 盘路径：`D:\programme_data\python`。
    
4. 点击确定。下次再打开它，终端就会直接出生在 D 盘了。
## 最终验证

重新双击 Jupyter Notebook 快捷方式，或在终端输入 `jupyter notebook`，即可直接从指定的 D 盘目录启动！


### 补充为什么设置anaconda环境变量
### 1. `D:\Anaconda3\Scripts` （最核心：各种命令行工具）

Jupyter Notebook、Conda、Pip 等我们常用的可执行程序（`.exe` 脚本）其实都躺在这个文件夹里。

- **配置后的好处：** 你可以在**任何普通的终端**（如系统的 PowerShell、CMD，或者 VS Code 里的终端）中，直接输入 `jupyter notebook`、`conda install`、`pip install` 并回车启动。
    
- **如果不配置：** 你必须每次都先打开特定的 "Anaconda Prompt"，或者在普通终端里死记硬背敲出极长的绝对路径（比如 `D:\Anaconda3\Scripts\jupyter-notebook.exe`）才能启动，非常痛苦。
    

### 2. `D:\Anaconda3` （Python 核心引擎）

这是 Anaconda 的安装根目录，里面躺着最核心的 `python.exe`（Python 解释器本身）。

- **配置后的好处：** 无论你在哪个盘符、哪个文件夹下，只要在终端敲 `python`，就能一秒进入 Python 交互式环境，或者直接运行你的 `.py` 脚本（如 `python test.py`）。
    
- **如果不配置：** 系统会提示“无法将 python 项识别为 cmdlet...”，你甚至无法在其他编辑器（如 VS Code）里直接调用这个 D 盘的 Python 环境。
    

### 3. `D:\Anaconda3\Library\bin` （底层依赖和底层工具）

这个文件夹里存放了大量的 `.dll` 动态链接库和底层 C/C++ 依赖软件（比如 OpenSSL、MKL 算法库、以及图形界面相关的一些支持组件）。

- **配置后的好处：** 保证你在运行一些复杂的 Python 科学计算库（如 NumPy, Pandas）或者加载某些加密、绘图组件时，系统能够顺利找到底层的“零部件”，**防止程序在后台因为“找不到某某 .dll 文件”而莫名其妙地闪退或报错**。