
快捷键

| 快捷键        | 功能                     |
| ---------- | ---------------------- |
| esc        | 从输入模式退出到命令模式           |
| a          | 在当前 cell 上面创建一个新的 cell |
| b          | 在当前 cell 下面创建一个新的 cell |
| dd         | 删除当前 cell              |
| m          | 切换到 markdown 模式        |
| y          | 切换到 code 模式            |
| ctrl + 回车  | 运行 cell                |
| shift + 回车 | 运行当前 cell 并创建一个新的 cell |
conda常用命令
```
3.5 conda常用命令
创建环境：
conda create --name myenv: 创建一个名为 myenv 的新环境。
conda create --name myenv python=3.8: 创建一个带有指定 Python 版本的环境。
conda create --name myenv numpy pandas: 创建一个包含指定软件包的环境。
管理环境：
conda activate myenv: 激活名为 myenv 的环境。
conda deactivate: 停用当前环境。
conda env list: 列出所有可用环境。
-conda env remove --name myenv: 删除名为 myenv 的环境。
安装/卸载软件包：
conda install numpy: 安装 numpy 包。
conda install numpy=1.19.2: 安装指定版本的 numpy 包。
conda install --file requirements.txt: 从 requirements.txt 文件中安装所有指定的软件包。
conda remove numpy: 卸载 numpy 包。
更新软件包：
conda update numpy: 更新 numpy 包到最新版本。
conda update --all: 更新所有已安装的软件包到最新版本。
搜索软件包：
conda search numpy: 搜索可用的 numpy 版本。
conda search "*search_term*": 在 Conda 存储库中搜索指定的软件包。
清理：
conda clean --all: 清理不再需要的临时文件和缓存。
```

