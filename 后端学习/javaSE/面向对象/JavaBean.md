用来描述一类事物的类，专业叫做：Javabean类。
### JavaBean 的四大标准规范

1. **类必须是公共的（`public`）**：方便任何地方都能访问它。
    
2. **所有属性必须私有化（`private`）**：实现核心的封装特性，不暴露底层变量。
    
3. **必须提供一个公共的、无参数的构造方法**：这是各类反射框架（如 Spring、Jackson）自动创建该类对象时的刚需。
    
4. **为所有私有属性提供对应的公开 `Getter` 和 `Setter` 方法**：用标准的命名规范（`getXxx` / `setXxx`）供外界读写数据。

## 为什么各大框架都要求使用 JavaBean？

这背后其实隐藏着现代框架的基石 —— **反射机制（Reflection）**。

- **为什么必须有无参构造？** 当你用 MyBatis 从数据库查出一条数据，或者前端传给 Spring 一个 JSON 时，框架需要帮你自动变成一个 Java 对象。框架底层的逻辑是：先调用 `Class.getDeclaredConstructor().newInstance()` 帮你把这个空对象 `new` 出来，然后再把数据填进去。如果类里没有无参构造，框架直接就会抛出异常。
    
- **为什么必须用标准的 `getXxx`/`setXxx` 命名？** JSON 解析工具（如 Fastjson, Jackson）在把你对象的 `username` 转成 JSON 字符串时，它不是直接去摸你的属性，而是去寻找叫 `getUsername()` 的方法。这种规范让跨框架的**自动化数据对接**成为了可能。
    

## 四、 现代开发避坑与提效指南

### 1. 成员变量类型：推荐使用“包装类”，而不是“基本类型”

在写 JavaBean 的属性时，推荐把 `int` 改为 `Integer`，把 `double` 改为 `Double`。

- **原因：** 基本类型有默认值（`int` 默认为 `0`）。如果一个新用户还没填写年龄，从数据库查出来应该是 `null`（代表未知）。如果是 `int`，它会自动变成 `0`，从而产生严重的业务歧义。而包装类可以很好地表达 `null`。
    

### 2. 解放双手的利器：Lombok 插件

在实际企业开发中，几乎没有人会手动去写这一堆 `Getter/Setter/toString`。我们通常会引入一个叫 **Lombok** 的依赖，通过在类上加注解，让编译器在编译时自动帮我们生成这些代码：

Java

```
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data             // 自动生成所有属性的 Getter, Setter, toString, hashCode, equals
@NoArgsConstructor //自动生成无参构造
@AllArgsConstructor// 自动生成全参构造
public class User {
    private Long id;
    private String username;
    private Integer age;
}
```

_短短几行，搞定一个完美的 JavaBean！_

> 📌 **核心记忆卡** **JavaBean** = `public类` + `private属性` + `无参构造` + `Getter/Setter`。它是 Java 世界里最标准、最好脾气的数据装载器。