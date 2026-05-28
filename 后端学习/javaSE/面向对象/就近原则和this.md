## 1. 什么是“就近原则”？

就近原则（Proximity Principle）的意思是：当方法内出现一个变量名时，Java 会优先在离它“最近”的作用域里去找这个变量。
就近原则就是看离哪个声明最近，“离得近”，指的是“代码块（作用域）的嵌套层级离得近”

- **最近的：** 方法内部的局部变量（或者方法的形参）。
    
- **次远的：** 当前类的成员变量（类属性）。
    

我们来看一个最容易让人翻车的经典错误：



![[1.java]]

```
class Student {
    String name = "张三"; // 成员变量

    public void changeName(String name) { // 形参也叫 name
        // 这里的两个 name，根据就近原则，通通代表“形参name”！
        name = name; 
    }
}
```

> **翻车结果：** 在 `name = name;` 这行代码里，Java 认为你是在把“形参的值”赋值给“形参自己”，跟类里面的成员变量 `String name = "张三";` 没有任何关系。方法执行完，学生的真正名字还是“张三”，根本没改成功。

## 2. 什么是 `this` 关键字？

为了解决上面“撞名”导致的尴尬，Java 提供了 `this` 关键字。

**`this` 代表当前对象的引用（谁调用这个方法，`this` 就代表谁）。** 你可以把它理解为中文里的“我的”。

- `name` ➡️ 谁离我近，我就代表谁（通常是局部变量）。
    
- `this.name` ➡️ 明确表示：**“我的 name 属性”**（即当前对象的成员变量）。
    

### ✨ 解决撞车问题的正确姿势：

Java

```
class Student {
    String name = "张三"; // 成员变量

    public void changeName(String name) { // 形参 name
        // this.name 代表成员变量
        // 等号右边的 name 代表离它最近的形参
        this.name = name; 
    }
}
```

## 3. `this` 关键字的三大核心用法

在日常开发中，`this` 主要活跃在以下三个场景：

### 用法一：区分同名的成员变量和局部变量（最常用）

这就是上面提到的场景。在 Java 的构造方法和 Setter 方法中，为了让语义清晰，形参名通常和属性名一模一样，此时必须用 `this`。

Java

```
public class Person {
    private int age;

    // 构造方法
    public Person(int age) {
        this.age = age; // 把传进来的参数 age，赋值给当前对象的属性 age
    }
}
```

### 用法二：在构造方法中调用本类的其他构造方法

如果一个类有多个构造方法（构造方法重载），为了避免重复写初始化代码，可以用 `this()` 来调用兄弟构造方法。

> ⚠️ **硬性规定：** `this()` 必须写在构造方法内部的**第一行**！

Java

```
public class User {
    String name;
    String role;

    // 构造方法 1：只需要传名字，角色默认为 "普通会员"
    public User(String name) {
        // 必须写在第一行！调用下面的构造方法 2
        this(name, "普通会员"); 
        System.out.println("构造方法 1 被调用了");
    }

    // 构造方法 2：可以同时传名字和角色
    public User(String name, String role) {
        this.name = name;
        this.role = role;
        System.out.println("构造方法 2 被调用了");
    }
}
```

### 用法三：代表当前对象本身（用于链式调用或方法传参）

当一个方法需要把“自己整个对象”作为参数传给别人，或者希望实现**链式编程**（像 `StringBuilder.append()` 那样连续点点点）时，可以直接 `return this;`。

Java

```
public class Hero {
    private int hp = 100;

    // 受到伤害的方法，返回 Hero 对象本身
    public Hero takeDamage(int damage) {
        this.hp -= damage;
        return this; // 把当前这个正在挨打的英雄对象返回回去
    }

    public void showHp() {
        System.out.println("当前血量：" + this.hp);
    }
}

// 外部调用时，就可以愉快地玩链式连续调用了：
public class Main {
    public static void main(String[] args) {
        Hero ironMan = new Hero();
        // 连环挨打，最后看血量
        ironMan.takeDamage(10).takeDamage(20).showHp(); // 输出：当前血量：70
    }
}
```

## 🚨 避坑指南：`this` 不能用在什么地方？

初学者最容易犯的一个错误，就是把 `this` 写在 **`static`（静态）方法** 里面。

> ❌ **记住一句话：静态方法里绝对不能出现 `this`！**
> 
> **原因：** `static` 成员是属于**类**的，它在对象还没被 `new` 出来之前就存在了。而 `this` 代表的是**具体某个对象**。在一个属于全班（类）的公共广播站（静态方法）里，指着麦克风说“我的（`this`）”，Java 根本不知道这个“我的”指的是哪位同学。