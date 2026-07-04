### SQL 通用语法
1. SQL 语句可以单行或多行书写，以分号结尾。
2. SQL 语句可以使用空格 / 缩进来增强语句的可读性。
3. MySQL 数据库的 SQL 语句不区分大小写，关键字建议使用大写。
4. 注释：
    - 单行注释: `-- 注释内容` 或 `# 注释内容`(MySQL 特有)
    - 多行注释: `/* 注释内容 */`
### SQL分类
#### 1. DDL (Data Definition Language) 数据定义语言

- **核心功能：** 用于定义或修改数据库的**结构**（如数据库、表、视图、索引等）。它是对“容器”的操作，而不是对具体数据的操作。
    
- **常用关键字：** `CREATE`, `DROP`, `ALTER`, `TRUNCATE`
    
- **常见操作：**
    
    - `CREATE TABLE`：创建一张新表。
        
    - `DROP DATABASE`：删除整个数据库。
        
    - `ALTER TABLE`：给表增加一个新字段。
        
    - `TRUNCATE TABLE`：清空表中的所有数据（但保留表结构，速度比 DELETE 快）。
        
> ⚠️ **注意：** DDL 操作通常是**自动提交**的，一旦执行，很难像撤销普通数据那样轻易回滚。
#### 2. DML (Data Manipulation Language) 数据操作语言

- **核心功能：** 用于对表中的**数据**进行增、删、改。它是开发中最常用的部分。
    
- **常用关键字：** `INSERT`, `UPDATE`, `DELETE`
    
- **常见操作：**
    
    - `INSERT INTO`：向表中插入一条新记录。
        
    - `UPDATE ... SET`：修改某条记录的某个字段值。
        
    - `DELETE FROM`：删除某些满足条件的行。
        
#### 3. DQL (Data Query Language) 数据查询语言

- **核心功能：** 用于从数据库中**检索/查询**数据。虽然很多时候 DQL 被归类在 DML 中，但因为它的使用频率极高、语法最复杂，所以通常独立出来。
    
- **常用关键字：** `SELECT`
    
- **常见操作：**
    
    - 配合 `WHERE` 过滤条件。
        
    - 配合 `GROUP BY` 和聚合函数（如 `COUNT`, `SUM`）进行统计。
        
    - 配合 `JOIN` 进行多表联查。
        
#### 4. DCL (Data Control Language) 数据控制语言

- **核心功能：** 用于管理数据库的**权限和安全**。通常由数据库管理员（DBA）使用。
    
- **常用关键字：** `GRANT`, `REVOKE`
    
- **常见操作：**
    
    - `GRANT`：给某个用户赋予查询或修改特定表的权限。
        
    - `REVOKE`：收回之前赋予用户的权限。
        
#### 5. TCL (Transaction Control Language) 事务控制语言（补充）

在涉及多步操作需要保持一致性时（例如银行转账），就需要用到 TCL 来管理**事务**。

- **核心功能：** 确保 DML 语句的操作要么全部成功，要么全部失败。
    
- **常用关键字：** `COMMIT`, `ROLLBACK`, `SAVEPOINT`
    
- **常见操作：**
    
    - `COMMIT`：提交事务，将数据的修改永久保存。
        
    - `ROLLBACK`：回滚事务，撤销所有未提交的修改。
        
#### 💡 快速总结对比表

| **分类**  | **全称**                       | **作用对象** | **核心命令**                     | **一句话记忆**      |
| ------- | ---------------------------- | -------- | ---------------------------- | -------------- |
| **DDL** | Data Definition Language     | 结构（表/库）  | `CREATE`, `DROP`, `ALTER`    | **建房子、拆房子**    |
| **DML** | Data Manipulation Language   | 数据行      | `INSERT`, `UPDATE`, `DELETE` | **搬家具、换家具**    |
| **DQL** | Data Query Language          | 数据行      | `SELECT`                     | **看房子里有什么**    |
| **DCL** | Data Control Language        | 权限/安全    | `GRANT`, `REVOKE`            | **发钥匙、收钥匙**    |
| **TCL** | Transaction Control Language | 事务       | `COMMIT`, `ROLLBACK`         | **后悔药（撤销与确认）** |