class Student {
    String name = "张三"; // 成员变量

    public void changeName(String name) { // 形参也叫 name
        // 这里的两个 name，根据就近原则，通通代表“形参name”！
        name = name; 
    }
}