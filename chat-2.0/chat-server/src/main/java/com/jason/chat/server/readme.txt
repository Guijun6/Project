聊天室
版本:2.0.0
功能介绍：
    1、实现用户使用用户名和密码进行注册
    2、只需一次注册，后面上线只需登录
    3、该账号只要用户不进行注销，则会永久保存
    4、其他用户上线会有友情上线提示
    5、可以对在线用户进行私聊
    6、可以对在线所有用户群发消息
    7、随时可以进行下线操作
开发介绍：
    客户端 + 服务端
    开发环境：JDK1.8 + IDEA + MySQL + JDBC
    依赖技术：使用Socket ，TCP 网络编程
              在线人数缓存使用 HashMap<socket, userName>
              数据库连接使用 JDBC
    数据库表结构：
    create table if not exists user_register_info(
      id int primary key auto_increment comment '用户ID',
      name varchar(32) not null unique comment '用户名',
      password varchar(32) not null comment '用户密码',
      signature varchar(64) comment '个性签名',
      addr varchar(32) not null comment '用户主机IP',
      port int not null comment '用户端口号',
      localport int not null comment '本机端口号',
      create_time timestamp not null comment '用户注册时间',
      modify_time timestamp not null default now() comment '更新时间'
    ) engine innodb;