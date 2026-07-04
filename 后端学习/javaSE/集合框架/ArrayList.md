**泛型参数不能是基本数据类型（Primitive Types），只能是对象（Object）。**
你不能写 `ArrayList<int>`，而必须写 `ArrayList<Integer>`。
## 一. 基本类型与包装类的对应关系

| **基本类型 (Primitive)** | **包装类 (Wrapper Class)** | **内存大小（基本类型）**    |
| -------------------- | ----------------------- | ----------------- |
| `byte`               | `Byte`                  | 1 byte            |
| `short`              | `Short`                 | 2 bytes           |
| `int`                | `Integer`               | 4 bytes           |
| `long`               | `Long`                  | 8 bytes           |
| `float`              | `Float`                 | 4 bytes           |
| `double`             | `Double`                | 8 bytes           |
| `char`               | `Character`             | 2 bytes           |
| `boolean`            | `Boolean`               | 1 bit (实际编译后可能不同) |

## 2. 自动装箱与拆箱（Autoboxing & Unboxing）

Java 5 引入了自动装箱和拆箱机制，编译器会在底层帮我们做转换：

- **自动装箱（Autoboxing）**：基本类型 $\rightarrow$ 包装类
- **自动拆箱（Unboxing）**：包装类 $\rightarrow$ 基本类型
```
List<Integer> list = new ArrayList<>();

// 1. 自动装箱：表面上存入的是 int 类型的 10
list.add(10); 
// 编译器实际转化为：list.add(Integer.valueOf(10));

// 2. 自动拆箱：表面上直接用 int 接收
int num = list.get(0); 
// 编译器实际转化为：int num = list.get(0).intValue();
```

## 3. 生产环境的三大“巨坑”

虽然自动装箱很方便，但在复杂的后端业务场景中，如果不注意，极易引发线上 Bug 或性能问题。

### 坑一：NullPointerException (空指针异常)

这是最常见也最隐蔽的崩溃点。**当把一个为 `null` 的包装类对象赋值给基本类型时，或者进行算术运算时，会触发自动拆箱，直接抛出 `NPE`。**

```
Integer count = null; // 比如从数据库查出来的某个统计字段，允许为 null

// 触发自动拆箱，底层调用 count.intValue() -> 抛出 NullPointerException
int myCount = count; 

// 同样会触发 NPE
if (count > 0) { ... } 
```

> **高级规范**：在处理 RPC 接口返回值、数据库映射对象（DO/DTO）时，如果变量可能为 `null`，必须先做非空校验，再进行业务计算。

### 坑二：`==` 与 `equals` 的缓存骗局（以 Integer 为例）

```
Integer a = 100;
Integer b = 100;
System.out.println(a == b); // true

Integer c = 200;
Integer d = 200;
System.out.println(c == d); // false !!!
```

- **原因**：`Integer` 内部有一个缓存池（**IntegerCache**），默认缓存了 **$-128$ 到 $127$** 之间的对象。通过 `Integer.valueOf()` 装箱时，如果在范围内，会直接复用缓存中的同一个对象（所以 `a == b` 为 true）；如果超出范围，则每次都 `new` 一个新对象（所以 `c == d` 为 false）。
    
- **黄金定律**：**包装类之间的比较，一律使用 `equals()` 方法，绝对不要用 `==`。** 
### 坑三：循环中的高并发性能损耗

由于自动装箱会在底层不断创建新对象，如果在高频循环或大数据量洗数时用错类型，会导致大量的内存碎片，触发频繁的 GC。

## ArrayList基本操作
### 1. 基础增删改查（CRUD）

这是最频繁使用的四大金刚：

Java

```
List<String> list = new ArrayList<>();

// 1. 增 (Add)
list.add("Java");         // 尾部插入：["Java"]
list.add("Go");           // 尾部插入：["Java", "Go"]
list.add(1, "Python");    // 指定位置插入：["Java", "Python", "Go"] （注意：会导致后续元素后移）

// 2. 查 (Get)
String language = list.get(0); // 获取索引0的元素 -> "Java"

// 3. 改 (Set)
list.set(2, "Rust");      // 将索引2的元素修改为 "Rust" -> ["Java", "Python", "Rust"]

// 4. 删 (Remove)
list.remove(1);           // 按索引删除第二个元素 -> ["Java", "Rust"]
list.remove("Rust");      // 按对象删除（若有重复，只删第一个） -> ["Java"]
```

### 2. 容量与状态检查

在对集合进行操作前，通常需要判断其状态，避免空指针或越界：

- **获取元素个数**：`list.size()`
    
    > **注意**：它返回的是逻辑上的元素数量，而不是底层数组的实际容量。
    
- **判断是否为空**：`list.isEmpty()` （比 `list.size() == 0` 更具可读性）。
    
- **清空所有元素**：`list.clear()` （清空后 `size` 变为 0，但底层数组空间不会立刻释放）。
    
- **是否包含某个元素**：`list.contains("Java")` （底层通过 `equals()` 方法比对，时间复杂度为 $O(n)$）。
    
- **查找元素位置**：
    
    - `list.indexOf("Java")`：返回第一次出现的索引，找不到返回 -1。
        
    - `list.lastIndexOf("Java")`：返回最后一次出现的索引。
### 3. 集合的四种遍历方式

在后端开发中，遍历是最常见的操作。针对不同的场景，选择不同的方式：

Java

```
List<String> list = Arrays.asList("Apple", "Banana", "Orange");

// 方式一：增强 for 循环 (最常用，单纯为了读取数据)
for (String fruit : list) {
    System.out.println(fruit);
}

// 方式二：Lambda 表达式 / forEach (Java 8+, 代码最简洁)
list.forEach(fruit -> System.out.println(fruit));
// 或者使用方法引用：list.forEach(System.out::println);

// 方式三：普通 for 循环 (需要用到索引下标时使用)
for (int i = 0; i < list.size(); i++) {
    System.out.println("索引 " + i + " 的元素是: " + list.get(i));
}

// 方式四：Iterator 迭代器 (需要在遍历过程中安全删除元素时使用)
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String fruit = iterator.next();
    if ("Banana".equals(fruit)) {
        iterator.remove(); // 隐式修改，不会触发 ConcurrentModificationException
    }
}
```

### 4. 集合之间的批量操作

处理批量业务数据时（比如批量上下架商品、批量查询），这些 API 非常高效：

- **添加另一个集合的所有元素**：`list.addAll(collection)`
    
- **求交集（保留共同元素）**：`list.retainAll(collection)`
    
- **单行初始化（Java 9+）**：`List<String> list = List.of("A", "B", "C");` （注意：这样生成的 List 是**不可变**的，不能进行 add/remove）。
    

### 💡 开发小贴士

1. **判空原则**：在对 `ArrayList` 进行 `size()`、`isEmpty()` 或遍历操作前，一定要先确保它不是 `null`，或者使用工具类 `CollectionUtils.isEmpty(list)`（来自 Spring 或 Apache Commons），它能同时帮你做非空和非 `null` 的校验。
    
2. **基本类型注意**：`ArrayList` 只能存储对象。如果要存数字，需要写 `List<Integer>` 而不是 `List<int>`，Java 会自动帮你进行装箱（Boxing）操作，但在超高频的高并发流量下，自动装箱会带来轻微的性能损耗。