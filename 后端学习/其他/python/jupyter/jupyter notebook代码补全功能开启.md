我的版本是这个
![[Pasted image 20260614155707.png|367]]
1、点击进入JupyterLab
![[Pasted image 20260614155419.png]]
2、点击activate Command Palette 搜索settings Editor
![[Pasted image 20260614155859.png|565]]
![[Pasted image 20260614155932.png|441]]
![[Pasted image 20260614160050.png|561]]
3、进入设置（setting）后，找到Code Completion，点击Enable autocompletion
![[Pasted image 20260614160251.png]]
4、最后退出jupyter notebook（关闭终端），重新进入即可。



### 关闭 Jedi 补全引擎（针对老版本 Jupyter ）

如果你的 Jupyter 经常无故补全失效，可以在 Notebook 的最顶端加入以下代码并运行，强制关闭 Jedi 引擎，让 Jupyter 回退到内置的高效补全模式：

```
%config Completer.use_jedi = False
```
但是上面只是局部的，对于其他新开的文件都要输入上述代码运行

## 全局永久生效配置指南（一劳永逸）

我们可以通过修改 IPython 的启动脚本（Startup Scripts），让任何 Jupyter Notebook 在启动时默默自动执行这行命令。

### 第一步：在终端/命令行生成配置文件

打开你的终端（Windows 下建议使用 `cmd` 或 `PowerShell`，如果你用 Anaconda，可以直接用 `Anaconda Prompt`），运行以下命令：

Bash

```
ipython profile create
```

> **作用**：这会在你的本地用户目录下创建一个隐藏的配置文件夹（通常路径为 `C:\Users\你的用户名\.ipython\`）。

### 第二步：找到并进入 `startup` 文件夹

利用文件管理器或者命令行，导航到以下路径：

Code snippet

```
C:\Users\你的用户名\.ipython\profile_default\startup\
```

在这个 `startup` 文件夹内，所有 `.py` 格式的脚本都会在 Jupyter 内核启动时**自动按字母顺序优先执行**。

### 第三步：新建一个自动执行的 `.py` 文件

1. 在 `startup` 文件夹内，新建一个空白的文本文件，将其重命名为： **`00-disable-jedi.py`** （注意后缀名必须是 `.py`，而不是 `.txt`）。
    
2. 用记事本（或 VS Code / Cursor 等编辑器）打开它，写入以下两行纯 Python 代码：
    
    Python
    
    ```
    import IPython
    IPython.get_ipython().Configurable.get_config().Completer.use_jedi = False
    ```
    
    _(注：在纯 `.py` 脚本中不能直接写带 `%` 的魔法命令，所以需要转换为上面这两行对应的底层的 Python API 调用。)_
    
3. 保存并关闭文件。
    

## 验证配置是否成功

配置完成后，**完全关闭并重启你的 Jupyter Notebook 服务**。

新建一个空白的 `.ipynb` 文件，不需要输入任何 `%config` 命令，直接导入 pandas 并尝试提取切片：

```
import pandas as pd
df = pd.DataFrame({'A': [1, 2]})
df.   # 在这里按下 Tab 键
```

如果能瞬间弹出提示列表，说明全局配置已经成功生效！以后不管是新建文件还是重启电脑，Jedi 补全引擎都会被默认关闭，Jupyter 也会一直保持流畅的 `Tab` 补全状态。


可以让claude code为你配置命令如下：

帮我自动在 `.ipython` 的默认启动目录下新建一个名为 `00-disable-jedi.py` 的脚本。文件路径通常是 `~/.ipython/profile_default/startup/00-disable-jedi.py`（如果 windows 下路径不存在，请先帮我运行 `ipython profile create` 生成）。在这个文件里写入两行纯 Python 代码，用于在 Jupyter 启动时全局禁用 Jedi 补全引擎，以解决 Pandas 代码补全卡顿的问题。python代码为 import IPython
IPython.get_ipython().Configurable.get_config().Completer.use_jedi = False
