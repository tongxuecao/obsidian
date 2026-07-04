
在 Pandas 中，使用 `[[]]`（双括号）嵌套来提取多列数据，本质上是因为**外层的括号和内层的括号各自扮演着不同的角色**。

我们可以把它们拆开来看：

### 1. 外层的括号 `df3[...]` —— “提取算子”

外层的方括号是 Pandas 对象的**索引操作符（Indexing Operator）**。它的功能类似于 C/C++ 中的数组下标 `arr[i]` 或者 Python 字典的 `dict[key]`。 它负责告诉 Pandas：“我要开始从这个 DataFrame 里面拿东西了！”

### 2. 内层的括号 `['sepal_length', 'petal_length']` —— “列表（List）”

内层的方括号其实和 Pandas 没有任何关系，它只是一个**标准的 Python 列表（List）**，里面装了多个字符串。 在传统的 C/C++ 或 Python 中，如果你想把多个元素捆绑在一起当成一个整体传递，最常用的方式就是把它们放进一个列表或数组里。

### 💡 连起来理解

当你写 `df3[['sepal_length', 'petal_length']]` 时，你实际上是把一个**普通的 Python 列表**作为参数，传给了 DataFrame 的**提取算子**。

这类似于：

```
# 1. 先定义一个包含多个列名的普通列表
col_list = ['sepal_length', 'petal_length']

# 2. 把这个列表塞进外层的提取算子中
target_cols = df3[col_list]
```

Pandas 看到你传进来的是一个“列表”而不是“单个字符串”时，就知道你想提取多列，于是就会把这些列全部切出来，组合成一个新的 **DataFrame** 返回给你。