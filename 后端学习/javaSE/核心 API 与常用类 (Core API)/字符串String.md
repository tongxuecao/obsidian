## 一、 核心底子：String 的“不可变性” (Immutability)

在 Java 中，`String` 的底层是一个被 `final` 修饰的数组。一旦创建，**绝对不可更改**。

###  后端视角：为什么要设计成不可变？

1. **安全（线程安全 & 业务安全）**：多线程并发时，字符串作为参数传递可以省去加锁开销。同时，数据库连接密码、URL 都是字符串，如果可变，极易被恶意篡改。
    
2. **字符串常量池 (String Pool) 的基石**：为了节省内存，Java 会把字面量（如 `"abc"`）缓存在常量池中。如果 `String` 可变，改了其中一个，所有指向 `"abc"` 的变量全都会被波及。
    

### 字符串拼接

只要**表达式一侧是字符串**，整个 `+` 就会变成**字符串拼接**，而非算术加法：

1. 基本数据类型（`int`/`double`/`boolean`/`char` 等）→ 自动转为对应文本形式
2. 引用类型（对象）→ 自动调用该对象的 `toString()` 方法
#### 引用类型（对象）

如果**不重写 `toString()`**，打印对象 / 字符串拼接对象时，会执行 **`Object` 类原生的 `toString()`**，输出格式：

```
类名@哈希码（十六进制）
```


#### 易错点（重点）

#### 1. 全是数字在前，先算加法再拼接

`+` 从左往右执行，**前面没有字符串时，先做算术运算**
```
// 先算 1+2=3，再拼接 "3abc"
String s1 = 1 + 2 + "abc";  // 3abc

// 先拼接 "12"，再拼接 "123"
String s2 = "1" + 2 + 3;    // 123
```

#### 2. `null` 特殊处理

拼接 `null` 不会空指针异常，会转为字符串 `"null"`

```
String str = null + "test"; 
System.out.println(str); // nulltest
```



##  二、 高频实战：开发中最常用的 5 大类 API

### 1. 判空与去空格（防 Null 轰炸）

后端接收前端传参或接口调用时，**第一步永远是判空**。

- `isEmpty()`：检查长度是否为 0。
    
- `isBlank()` **(Java 11+)**：不仅检查长度，还能智能识别“全空格”的情况（如 `" "`），**强烈推荐替代 `isEmpty()`**。
    
- `trim()`：去除首尾空格。
    
- `strip()` **(Java 11+)**：升级版去空格，支持去除非 ASCII 的全角空格。
    

> 🚨 **资深避坑**：调用这些方法前，必须确保字符串本身不为 `null`，否则直接爆出 `NullPointerException`。实际开发中推荐使用：`StringUtils.isNotBlank(str)`（源自第三方依赖 Apache Commons Lang3 或 Spring）。

### 2. 查找与包含（业务路由/规则过滤）


- `contains(CharSequence s)`：是否包含子串（常用于敏感词过滤）。
    
- `public char charAt(int index)` 是 **`String` 类的实例方法**，作用是**根据索引获取字符串中单个字符**

- `startsWith(String prefix)` / `endsWith(String suffix)`：是否以特定字符开头/结尾（常用于判断文件格式、拦截 URL 路由）。
    
- `indexOf(String str)`：返回子串第一次出现的位置，找不到返回 `-1`。
    

### 3. 转换与分割（数据清洗/报文解析）

- `split(String regex)`：按规则拆分（如解析 CSV 数据 `str.split(",")`）。
    - ⚠️ **注意**：参数是**正则表达式**。如果你想按点 `.` 分割，直接写 `split(".")` 会匹配所有字符，必须转义写成 `split("\\.")`。
        
- `String replace(String target, String replacement)`：普通替换。返回全新的字符
    
- `replaceAll(String regex, String replacement)`：正则替换（常用于脱敏，如把手机号中间4位换成 `*`）。
    
- `toLowerCase()` / `toUpperCase()`：大小写转换（常用于邮箱、验证码的不区分大小写比对）。
    

### 4. 截取（提取有用信息）

- `substring(int beginIndex, int endIndex)`：截取 [左闭右开) 区间的子串。
    
    - _示例_：`"HelloWorld".substring(0, 5)` $\rightarrow$ 返回 `"Hello"`。
        

### 5. 格式化（日志打印与 SQL 拼接）

- `String.format(String format, Object... args)`：占位符拼接。
    
    - _示例_：`String.format("用户 %s 在 %s 登录成功", userId, time)`
        
    - 💡 **后端避坑**：生产环境中打印日志，**不要**用 `String.format()`，而应该用 Logback/Log4j2 自带的 `{}` 占位符，效率高得多：`log.info("User {} logged in", userId);`
        

## ⚡ 三、 性能进阶：String、StringBuilder 与 StringBuffer

高并发场景下，**绝对不要在循环里使用 `+` 拼接字符串！**

Java

```
// ❌ 错误示范：每次循环都会在堆内存中创建新的 StringBuilder 和 String 对象，瞬间内存暴涨，引发 GC（垃圾回收）卡顿
String sql = "SELECT * FROM user WHERE id IN (";
for (int i = 0; i < 1000; i++) {
    sql += i + ",";
}
```

### 🛠️ 工业界选型指南

1. **`String`**：适用于**少量**字符串操作，或者字面量拼接（如 `String s = "a" + "b";` 触发编译期优化，不影响性能）。
    
2. **`StringBuilder`**：**后端最常用**。适用于**单线程**环境下存在大量频繁操作、拼接字符串的场景（如复杂的 SQL 组装、HTML 模板拼接）。性能最高。
    
3. **`StringBuffer`**：方法加了 `synchronized` 锁。适用于**多线程环境**下的字符串共享拼接。但由于后端通常在方法内部（栈内存）局部操作字符串，基本不涉及多线程竞争，因此**99% 的场景请优先选择 `StringBuilder`**。
    

## 🎯 四、 终极考核："==” 与 “equals()” 的深水区

这是后端面试必问，也是新手必踩的雷。

Java

```
String s1 = "hello";                  // 进常量池
String s2 = "hello";                  // 复用常量池里的对象
String s3 = new String("hello");      // 在堆内存中开辟全新空间

System.out.println(s1 == s2);         // true（指向常量池同一地址）
System.out.println(s1 == s3);         // false（一个是常量池，一个是堆）
System.out.println(s1.equals(s3));    // true（内容一模一样）
```

- **`==`**：比较的是**内存地址**。
    
- **`equals()`**：`String` 重写了该方法，比较的是**字符串的字面内容**。
    

> 📌 **高级后端铁律**：
> 
> 1. 在 Java 中比对字符串内容，**永远、必须、只能**使用 `equals()`。
>     
> 2. 为了防止 `NullPointerException`（空指针异常），请把**确定有值的常量（或字面量）写在前面**：
>     
>     - `if ("ADMIN".equals(userRole))` ❌ 不要写成 `if (userRole.equals("ADMIN"))`（因为 `userRole` 一旦为 `null` 直接挂掉）。
>