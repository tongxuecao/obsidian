## 🛠️ 第一部分：未登录时（Windows / PowerShell 状态）

这些命令必须在 **管理员权限** 的 PowerShell 中运行。

### 1. 服务控制（启动 / 关闭）

如果你没有把 MySQL 设置为开机自启，每次用之前需要先启动它：

- **启动服务：** `net start MySQL`
    
- **停止服务：** `net stop MySQL`
    

### 2. 登录 MySQL

- **本地登录：** `mysql -u root -p` _(敲回车后会提示你输入密码 `clh123456`)_
    
- **极简一行登录：** `mysql -u root -pclh123456` _(注意：`-p` 和密码之间**不能**有空格)_
    

### 3. 查看端口占用

有时候如果遇到 MySQL 打不开，可以查查 `3306` 端口有没有被别的软件抢走：

PowerShell

```
netstat -ano | findstr 3306
```

## 🗄️ 第二部分：已登录后（进入 `mysql>` 提示符状态）

**⚠️ 铁律：** 在 `mysql>` 内部执行的 SQL 语句，**结尾必须带英文分号 `;`**，否则回车后它会一直等待你输入。

### 1. 数据库级别操作（增删改查）

- **查看所有数据库：**
  ```    SHOW DATABASES;    ```
- 
* **创建新数据库**（推荐加上 `utf8mb4` 编码，支持中文和 Emoji 表情，支持四个字符）：
```
  CREATE DATABASE my_project_db CHARACTER SET utf8mb4;
```

- **切换/进入某个数据库：** 
    ```
    USE my_project_db;
    ```
* **删除数据库**（危险操作！）：
```
  DROP DATABASE my_project_db;
```

### 2. 数据表级别操作（进入某数据库后）

- **查看当前库里的所有表：**
    ```
    SHOW TABLES;
    ```
* **查看某张表的结构 / 字段信息：**
```
  DESC 表名;
```
### 3. 常用数据增删改查 (CRUD)

- **查询数据：** `SELECT * FROM 表名;`
    
- **插入数据：** `INSERT INTO 表名 (字段1, 字段2) VALUES ('值1', '值2');`
    
- **更新数据：** `UPDATE 表名 SET 字段1 = '新值' WHERE id = 1;`
    
- **删除数据：** `DELETE FROM 表名 WHERE id = 1;`
    
### 4. 退出

- **退出 MySQL 命令行：** `exit;` 或 `quit;`