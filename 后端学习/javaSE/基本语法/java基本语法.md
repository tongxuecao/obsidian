### 隐式类型转换：
当两个**不同类型的数值**进行算术运算或比较运算时，Java 会自动将它们转换成**同一类型**再进行操作。这个转换的铁律是：**向表达范围更大的类型看齐（低精度向高精度提升）**。

## 一、 核心基础语法对比

在写算法的控制流和基础变量声明时，有几个关键的“小动作”需要注意：

| **语法特性**  | **C++ (STL 风格)**               | **Java**               | **关键差异与避坑提示**                                                                |
| --------- | ------------------------------ | ---------------------- | ---------------------------------------------------------------------------- |
| **布尔类型**  | `bool`                         | `boolean`              | Java 必须写全称。                                                                  |
| **布尔字面量** | `true` / `false` (或 `1` / `0`) | `true` / `false`       | **Java 的 `if` 里面必须是严格的布尔值**，绝对不能写 `if(1)` 或 `if(count)`，必须写 `if(count > 0)`。 |
| **常数定义**  | `const int MAX = 100;`         | `final int MAX = 100;` | Java 使用 `final` 关键字表示不可变。                                                    |
| **空指针**   | `nullptr` (或 `NULL`)           | `null`                 | Java 全都是小写的 `null`。                                                          |

## 二、 核心数据结构（容器）映射表

算法题离不开“动态数组、哈希表、队列、栈”。两者的标准库命名完全不同，但功能几乎一一对应：

| **算法常用结构**   | **C++ (STL)**           | **Java (Collections)**                           | **核心方法差异 (C++ vs Java)**                                                                                       |
| ------------ | ----------------------- | ------------------------------------------------ | -------------------------------------------------------------------------------------------------------------- |
| **动态数组**     | `vector<int> arr;`      | `ArrayList<Integer> arr;`                        | **C++:** `push_back()`, `pop_back()`, `[i]`<br>**Java:** `add()`, `remove(size()-1)`, `get(i)`                 |
| **字符串**      | `string s;`             | `String s;`<br><br>  <br><br>`StringBuilder sb;` | **Java 的 String 是不可变的**！算法中如果需要频繁拼接、修改字符串，**必须使用 `StringBuilder`**，否则会 OOM 或超时。                                |
| **普通哈希表**    | `unordered_map<K, V>`   | `HashMap<K, V>`                                  | **C++:** `mp[key] = val;` (不存在会自增)<br>**Java:** `put()`, `get()`, `getOrDefault()` (极常用)                       |
| **自动排序哈希**   | `map<K, V>` (红黑树)       | `TreeMap<K, V>`                                  | Java 对应的也是红黑树，支持 `firstKey()`, `lastKey()`。                                                                    |
| **不重复集合**    | `unordered_set<T>`      | `HashSet<T>`                                     | **C++:** `insert()`, `count()`<br>**Java:** `add()`, `contains()`                                              |
| **双端队列 / 栈** | `deque<T>` / `stack<T>` | `ArrayDeque<T>`                                  | **Java 刷算法不要用 `Stack` 类（性能差）**。用 `ArrayDeque` 作为栈：`push()`, `pop()`, `peek()`；作为队列：`addLast()`, `pollFirst()`。 |
| **优先队列(堆)**  | `priority_queue<int>`   | `PriorityQueue<Integer>`                         | **C++ 默认是大顶堆**；**Java 默认是小顶堆**（切记！）。Java 想用大顶堆需写：`new PriorityQueue<>((a, b) -> b - a)`。                       |

## 三、 算法常用操作：数组与字符串

### 1. 数组与容器的长度获取

- **C++：** 统一使用 `.size()`（原生数组除外）。
    
- **Java：** 比较分裂，需要死记硬背：
    
    - 原生数组：`arr.length`（成员变量，不带括号）
        
    - 字符串：`s.length()`（方法，带括号）
        
    - 集合（List/Set/Map）：`list.size()`（方法，带括号）
        
### 2. 数组的初始化与默认值

- **C++：** 在函数内部声明的数组，如果没有显式初始化，里面是**随机垃圾数据**。
    
- **Java：** 任何时候 `new` 出来的数组，JVM 都会**自动赋予默认值**（数字为 `0`，布尔为 `false`，对象为 `null`），无需手动 `memset`。
    
### 3. 排序（Sort）

- **C++：** `sort(arr.begin(), arr.end());`
    
- **Java：** * 原生数组：`Arrays.sort(arr);`
    
    - 集合（List）：`Collections.sort(list);` 或 `list.sort(Comparator.naturalOrder());`
        
## 四、 底层机制的不同（算法避坑核心）

### 1. 指针 vs 引用（内存安全）

- **C++：** 存在真正的指针（`int* p`）。通过结构体节点指针（如 `ListNode* next`）构建链表或二叉树。可以通过 `&` 进行**引用传递**，在函数内部直接修改外部变量。
    
- **Java：** 没有指针，只有**引用**。树和链表的节点声明为 `ListNode next;`。Java 只有**值传递**，如果你想在递归函数内部修改一个全局的 `ans`（比如记录最大路径和），你不能像 C++ 那样传 `&ans`，你只能：
    
    - 把 `ans` 声明为类的**全局成员变量**。
        
    - 或者传一个长度为 1 的数组 `int[] ans = new int[1]` 进去。
        
### 2. 垃圾回收（内存管理）

- **C++：** 必须手动管理内存。如果你在树/链表题中 `new` 了节点，或者在每轮测试数据中开辟了空间，原则上需要 `delete`，否则会内存泄漏。
    
- **Java：** 拥有自动垃圾回收机制（GC）。你在写 DFS/BFS 期间随便 `new` 对象，方法结束离开作用域后，JVM 会自动帮你回收内存，写算法题时完全不需要考虑内存释放问题。



switch
##  现代 Switch 表达式（Java 12+ / 17 彻底确立）

现代 Java 对 `switch` 进行了彻底的重构，后端写业务代码时推荐全面转入新语法。

### ① 箭头语法（Lambda-like 箭头）

使用 `->` 代替 `:`，**彻底消除了 `break` 穿透问题**。只会执行匹配到的分支，执行完自动退出。


```
// 支持单行或多行（多行用大括号）
switch (userRole) {
    case "ADMIN" -> System.out.println("全权限");
    case "USER", "GUEST" -> {
        System.out.println("受限权限");
        log.info("普通用户登录");
    }
    default -> throw new IllegalArgumentException("未知角色");
}
```

### ② 作为表达式返回值

`switch` 变成了一个表达式，可以直接返回结果并赋值给变量：


```
// 干净利落，再也不用定义中间临时变量
String result = switch (day) {
    case MONDAY, FRIDAY -> "工作日忙碌";
    case SATURDAY, SUNDAY -> "周末休息";
    default -> "普通工作日";
}; // 注意：作为表达式时，末尾要有分号
```

### ③ `yield` 关键字

如果在一个返回值的 `switch` 分支中需要写多行复杂逻辑，可以使用 `yield` 关键字来返回一个值：

```
int result = switch (input) {
    case 1 -> 10;
    case 2 -> {
        int temp = input * 2;
        yield temp + 5; // 相当于该分支的 return
    }
    default -> 0;
};
```

### 后端开发规约与最佳实践

1. **强力推荐结合枚举（Enum）使用**： 在分布式电商或支付系统中，订单状态、支付状态一律采用 `switch(enum)` 处理。当业务增加新状态时，配合现代 switch 可以确保代码的健壮性。
    
2. **穷举性检查（Exhaustiveness）**： 当 `switch` 作为表达式返回值使用时，编译器会强制要求你**穷举所有可能的情况**。如果处理的是枚举，你必须把所有枚举项列完，或者写上 `default` 分支，否则代码无法通过编译。这在代码重构、增加新状态时能起到完美的编译期保护作用。
    
3. **警惕 NullPointerException**： 无论是传统还是现代 `switch`，如果传入的变量（如 `String` 或 `Enum`）本身是 `null`，运行到 `switch(value)` 时会**直接抛出 `NPE`**。
    
    > **避坑规范**：在进入 `switch` 之前，必须对目标变量进行前置非空校验（除非在 Java 21+ 中使用最新的模式匹配 `case null -> ...`）。