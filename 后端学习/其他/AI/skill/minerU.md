**MinerU 扮演的角色就像是给 AI 戴上了一副“学霸眼镜”。** 它负责把复杂的扫描件、图片、公式转换为 AI 最喜欢、读得最懂的 **结构化 Markdown（公式转 LaTeX，表格转 HTML）**，再喂给 AI。

在 AI 流程中使用 MinerU 辅助解析文件，主要有以下三种主流玩法：

## 玩法一：作为 MCP 工具直接接入 Claude Code / Cursor（推荐，最丝滑）

既然 MinerU 官方提供了 MCP 服务，你可以直接把它作为“超能力技能”配置到你的 AI 客户端里。

### 1. 本地安装 MinerU 核心

首先在电脑上（推荐 GPU 环境）通过 Python 安装 MinerU：

Bash

```
pip install -U "mineru[all]"
```

### 2. 将 MinerU MCP 注册到 Claude Code

使用我们前面学到的命令，将 MinerU 的文档探索服务（`MinerU-Document-Explorer`）注册为全局技能：

Bash

```
claude mcp add mineru python -m mineru_mcp_server --scope user
```

_(注：具体启动命令以官方最新 `mineru-mcp` 模块名为准，它会保持后台常驻，避免每次解析重复加载 AI 模型)_

### 3. 在对话中直接调遣它

配置好后，你在终端里和 Claude 对话时，直接把论文、发票图片或 PDF 往那儿一丢：

> 💬 **你：** “帮我分析一下 `D:/paper/concurrency.pdf` 这篇论文的第三页，尤其是那个数学公式是什么意思？”
> 
> 🤖 **Claude（后台自动调用 MinerU）：** > _[Tool Call: mineru.parse_file]_ > 自动把 PDF 里的复杂公式精准提取为 $E = mc^2$ 这样的标准 LaTeX，并把排版理顺，然后由 Claude 为你进行深度语义解析。

## 玩法二：在大模型开发框架中集成（如 Dify、LangChain、FastGPT）

如果你正在开发自己的 **“校园二手交易平台”** 或者构建一个 **RAG（检索增强生成）知识库系统**，用户上传的商品发票截图、说明书 PDF 需要被 AI 理解，就可以在后端管道（Pipeline）中集成 MinerU。

以大模型编排平台 **Dify** 为例，MinerU 官方已经入驻了 Dify 插件市场：

1. **安装插件**：在 Dify 的 Marketplace 中搜索并一键安装 **MinerU** 插件。
    
2. **选择模式**：
    
    - **本地/私有化部署**：调用你本地通过 `mineru-api` 启动的异步任务接口（`POST /tasks`）。
        
    - **云端 API 模式**：直接对接 MinerU 官方提供的 `Precision Extract API`（高精度提取接口，需填入 Token）。
        
3. **工作流编排**：
    
    [用户上传文件/图片] ➔ [MinerU 节点：解析为标准 Markdown/JSON] ➔ [大语言模型 LLM 节点：理解并回答]
    

通过这样一层清洗，LLM 拿到的就是“毫无杂质”的纯净文本。

## 玩法三：传统且稳妥的“先解析、后喂鱼”法（CLI / SDK）

如果你不想搞复杂的环境对接，只想写个简单的 Python 脚本，让 MinerU 先把一堆图片/PDF 啃完，再丢给大模型：

### 1. 命令行批量预处理（CLI）

在终端中直接运行命令，把特定目录下的复杂图片或 PDF 转换成 Markdown：

Bash

```
# 自动识别排版，将 PDF 中的表格转成 HTML 源码，公式转成 LaTeX 源码
mineru -i D:/programme_data/receipt.png -o D:/output_markdown
```

### 2. Python 联动脚本

你可以写一个简单的全自动脚本，MinerU 刚解析完，立马自动调用本地的 DeepSeek 或 OpenAI 接口：

Python

```
from magic_pdf.data.data_reader_writer import FileBasedReaderWriter
from magic_pdf.pipe.UNIPipe import UNIPipe
import openai

# 1. 启动 MinerU 深度解析图片/PDF
file_path = "D:/programme_data/complex_table.png"
image_bits = open(file_path, "rb").read()
# （此处省略 MinerU 复杂的初始化 Pipe 代码...）
# 解析完成后获得纯净的 markdown_text（包含完美的 HTML 表格代码）

# 2. 直接打包喂给大模型
client = openai.OpenAI(api_key="your-key", base_url="...")
response = client.chat.completions.create(
    model="deepseek-chat",
    messages=[
        {"role": "system", "content": "你是一个精通数据分析的 AI 助手。"},
        {"role": "user", "content": f"请帮我分析以下由 MinerU 解析出的结构化数据：\n{markdown_text}"}
    ]
)
print(response.choices[0].message.content)
```

### 💡 为什么 AI 时代大家这么推崇 MinerU？

而 MinerU 拥有 **Layout（版面分析）** 和 **Formula（公式识别）** 双重 VLM 引擎，它会先看懂图片的结构（哦，这是页眉应该删掉；这是左栏，这是右栏，应该先读左再读右；这是一个跨页的表格，应该合并），最后拼装出人类阅读顺序的 Markdown。这也是为什么它是目前构建 Agent 和高级知识库（RAG）最强前置辅助的原因。

https://github.com/opendatalab/MinerU-Ecosystem/blob/main/skills/SKILL.md
这个和MCP功能上有什么区别吗

### 📊 核心区别一览表

|维度|SKILL.md (Agent Skills 规范)|MCP (Model Context Protocol)|
|---|---|---|
|**本质是什么**|**“软技能”**：包含 YAML 元数据 + **Markdown 提示词指南**的自包含文件夹|**“硬接口”**：基于 JSON-RPC 的**客户端-服务端（Client-Server）通信协议**|
|**主要承载形式**|静态的 Markdown 规则、参考模板、少量辅助自动化脚本（Python/JS）|独立运行的后台进程（Server），通过 API 或 Stdio 传输结构化数据|
|**大模型怎么用它**|大模型**主动阅读** `SKILL.md`，理解后将其作为运行时的“动态系统提示词”来指导自己|大模型**被动触发**工具调用（Tool Calling），像调 API 一样把数据传过去让程序执行|
|**开发难度**|**极低**。用大白话写 Markdown 规则和几行脚本就能做，对人类高度可读|**中等**。需要编写符合 Anthropic SDK 规范的代码，处理 JSON 传输|
|**典型代表**|各种特定业务流（如“按标准写论文摘要”、“商品图批量裁剪”）|数据库连接器、联网搜索服务、本地文件读写系统|

### 🔍 深度对比：它们是如何运作的？

#### 1. 工作原理的区别（“教大模型做事” vs “给大模型配工具”）

- **`SKILL.md` 是“认知和规矩”：** 在 MinerU 生态中，如果配置了 `SKILL.md`，Claude Code 启动时会**自动扫描**并“读完”这个 Markdown 文件。文件里用自然语言写着：“_当你遇到包含论文的图片时，你应该触发步骤 A，调用 scripts 里的解析脚本，并且输出格式必须符合 references/ 文件夹下的标准 LaTeX 模板..._”。 **它是通过扩充大模型的系统提示词（System Prompt）和工作流逻辑来生效的。**
    
- **`MCP` 是“硬核物理通道”：** MCP 根本不包含任何“教大模型怎么思考”的文字。它就是一个死板的 TCP/Stdio 连接通道。当大模型遇到 PDF 时，它说：“我需要用 `mineru.parse_file` 这个工具”，然后把文件路径作为 JSON 参数扔给 MCP 服务端，由本地的 Python 进程跑完把 Markdown 结果再顺着通道扔回来。
    

#### 2. “渐进式加载”与 Token 消耗

- **`SKILL.md` 的优势（渐进式披露 - Progressive Disclosure）：** Claude Code 设计 `SKILL.md` 的初衷是为了省钱和省上下文。它包含两部分：顶部的 **YAML Frontmatter**（写着这个技能的名字和适用场景）和底部的详细指令。 当你在终端跟 Claude 聊天时，它默认**只读名字和简介**。只有当你真正说“帮我解析一下这张图片”时，它发现相符，才会**动态地**把整个 `SKILL.md` 的详细内容和里面的参考代码加进上下文里。
    
- **`MCP` 的优势：** MCP 传输的是纯粹的数据，不占用任何长期的提示词上下文。只有在工具被调用的那一瞬间，产生一次性的输入和输出 Token。
    

#### 3. 它们在 MinerU 生态里如何协同？

以你发链接的 MinerU 生态为例，最完美的完全体形式并不是二选一，而是 **“用 `SKILL.md` 作为大脑，驱动底层的 MCP 肌肉”**：

1. **底层（MCP 层）：** 部署一个 MinerU MCP Server。它在后台常驻，随时准备接收文件路径，调用 VLM 深度解析 PDF，并提供一堆原子化工具（如 `parse_pdf`、`extract_table`）。
    
2. **上层（SKILL 层）：** 编写一个 `SKILL.md` 文件夹。里面规定了在“自动化处理学术论文”这个特定场景下，应该如何优雅地结合 MinerU 的 MCP 工具，如何切分长文本、如何过滤页眉页脚、如何存储到你的 D 盘等具体的、带有业务逻辑的操作步骤。
    

### 总结建议

如果你只是想**让 Claude 拥有调用 MinerU 解析文件的基本物理能力**，配一个 **MCP Server** 是最直接的；如果你想**定制一个非常复杂的自动化文档处理流水线（比如自动把图片解析、分类、重命名并归档到特定 D 盘目录）**，那么去研究和编写该链接下的 **`SKILL.md` 技能文件夹** 会让你如虎添翼。