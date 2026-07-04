## 1. 什么是“就近原则”？

就近原则（Proximity Principle）的意思是：当方法内出现一个变量名时，Java 会优先在离它“最近”的作用域里去找这个变量。
就近原则就是看离哪个声明最近，“离得近”，指的是“代码块（作用域）的嵌套层级离得近”


## 2. 什么是 `this` 关键字？

this本质上是一个引用。
this中保存的也是对象的内存地址。
this中保存的是当前对象的内存地址。


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

如果一个类有多个构造方法（构造方法重载），**为了避免重复写初始化代码**，可以用 `this()` 来调用兄弟构造方法。

> ⚠️ **硬性规定：** `this()` 必须写在构造方法内部的**第一行**！


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


**记住一句话：静态方法里绝对不能出现 `this`！**
