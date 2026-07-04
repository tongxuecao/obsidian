`StringBuilder` 是一个**可变的字符序列**。它与 `String` 的最大区别在于：它的所有修改操作都是在==**原对象基础**==上进行的，不会在堆内存中产生大量无用的中间垃圾对象，是后端拼接字符串的绝对主力。
##  方法提取与标准定义

| 方法名                                | 说明                                             |
| ---------------------------------- | ---------------------------------------------- |
| public StringBuilder append (任意类型) | 添加数据，并返回对象本身                                   |
| public StringBuilder reverse()     | 反转容器中的内容                                       |
| public int length()                | 返回长度（字符出现的个数）                                  |
| public String toString()           | 通过 toString () 就可以实现把 StringBuilder 转换为 String |

## 工业级代码实战演练

在实际开发中，我们通常会利用 `append()` 的链式编程（Chain Programming）特性，让代码变得极其优雅。

Java

```
public class StringBuilderDemo {
    public static void main(String[] args) {
        // 1. 初始化容器
        StringBuilder sb = new StringBuilder();

        // 2. append() 核心实战：支持任意类型，支持链式调用
        sb.append("Order:")     // 拼接 String
          .append(10023L)       // 拼接 long 类型单号
          .append('_')          // 拼接 char
          .append(true);        // 拼接 boolean

        System.out.println("拼接后的内容: " + sb); // 输出: Order:10023_true

        // 3. length() 实战：获取实际字符长度
        int totalLen = sb.length();
        System.out.println("当前字符长度: " + totalLen);

        // 4. reverse() 实战：反转字符串（算法题高频）
        sb.reverse();
        System.out.println("反转后的内容: " + sb); // 输出: eurt_32001:redrO

        // 5. toString() 实战：最终收尾，转回标准的 String 供业务后续使用
        // 注意：反转回正序后再转 String
        String finalResult = sb.reverse().toString(); 
        System.out.println("最终转为String的对象: " + finalResult);
    }
}
```

##  补充：隐藏的硬核高频方法

除了图片上的 4 个基础方法，在日常后端开发（特别是写复杂的 SQL 动态拼接、或者解析 JSON）时，以下两个方法出镜率极高，建议一并记入笔记：

### 删除指定区间：`delete()`

- **标准签名**：`public StringBuilder delete(int start, int end)`
    
- **后端场景**：循环拼接列表时，最后经常会多出一个逗号 `,`。我们用它一键剔除末尾多余的符号。
    
- **实战示例**：
    ```
    StringBuilder sql = new StringBuilder("1,2,3,4,");
    // 删掉最后一个逗号（左闭右开区间：从最后一个字符开始，到总长度结束）
    sql.delete(sql.length() - 1, sql.length()); 
    System.out.println(sql); // 输出: 1,2,3,4
    ```
### 指定位置插入：`insert()`

- **标准签名**：`public StringBuilder insert(int offset, String str)`
    
- **后端场景**：在已有字符串的特定位置强行塞入新内容。
    
- **实战示例**：
    ```
    StringBuilder sb = new StringBuilder("JavaSE");
    sb.insert(4, "进阶"); // 在下标为 4 的地方插入
    System.out.println(sb); // 输出: Java进阶SE
    ```
## 链式编程
在后端开发中，**链式编程（Chain Programming / Method Chaining）** 是一种极其优雅的代码编写风格。你在 `StringBuilder` 中用到的 `.append("A").append("B").append("C")` 就是最标准的链式编程。

**核心铁律**： 要想在上一个方法后面继续 `.点` 出下一个方法，**上一个方法执行完的返回值，必须是一个对象！且通常是当前对象本身（`this`）。**

# StringJoiner

### 1. 它的两个核心构造函数

- **构造方法一（只指定分隔符）**：
    ```
    public StringJoiner(CharSequence delimiter)
    ```
    - _示例_：`new StringJoiner(",")` $\rightarrow$ 拼接出来长这样：`A,B,C`
    -
- **构造方法二（指定分隔符 + 前缀 + 后缀）**：
    ```
    public StringJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix)
    ```
    
    - _示例_：`new StringJoiner("-", "[", "]")` $\rightarrow$ 拼接出来长这样：`[A-B-C]`
        

##  二、高频实战：常用方法与代码演练

`StringJoiner` 的方法极少，学习成本几乎为零。它同样**支持链式编程**。

###  核心方法表

|**方法名**|**完整的标准签名**|**功能说明**|
|---|---|---|
|**`add()`**|`public StringJoiner add(CharSequence newElement)`|添加一个新元素，并**自动补上分隔符**，返回自身（支持链式）。|
|**`length()`**|`public int length()`|返回当前拼接出来的字符串的总长度（包含前缀、后缀和分隔符）。|
|**`toString()`**|`public String toString()`|最终收尾，转成标准的 `String`。|

`StringJoiner` 并不是凭空出世的新物种，它其实就是**给 `StringBuilder`（可变数组）套上了一层专门用来做“格式化拼接”的皮肤**。
## 后端终极选型：我到底该用谁？

学到这里，你手里的“字符串拼接兵器”已经有四件了：`+`、`StringBuilder`、`StringJoiner`、`String.join()`。在企业级开发中，我们该如何选型？

1. **纯字面量、极少量的拼接**：直接用 **`+`**（如 `String s = "Hello_" + userId;`），编译期会自动优化，可读性最好。
    
2. **复杂的循环拼接、SQL 动态组装**：优先用 **`StringBuilder`**，性能是绝对的王者，支持的玩法也最多（如 `insert`, `delete` 等）。
    
3. **集合/数组转换为特定符号分隔的字符串（如 CSV 格式、ID 列表拼装成 1,2,3）**：
    
    - 如果**不需要**前后缀 $\rightarrow$ 优先用 **`String.join()`**（最快最省心）。
        
    - 如果**需要**加括号或前后缀 $\rightarrow$ 优先用 **`StringJoiner`**。
        
4. **Java 8 的高级进阶玩法**：后续你学到 `Stream` 流时，会接触到 `Collectors.joining(",")`，它的底层同样是 `StringJoiner`，专门用来在流式处理中做数据聚合。