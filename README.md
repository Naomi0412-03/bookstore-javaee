#在线图书系统商城

##项目介绍
一个基于Java EE的B/S架构在线图书商城系统，采用SSM（Spring + Spring MVC + MyBatis）框架进行开发，实现了用户注册登录、图书浏览、搜索、购物车管理、订单生成与查询等核心功能
本项目为个人独立完成的课程设计，涵盖前后端完整代码，包含用户管理、图书浏览、购物车、订单管理、结算支付等核心功能模块。目前核心功能均已实现并测试通过

##技术栈
后端架构：Spring+SpringMVC+Mybatis
前端：HTML+CSS+JavaScript
数据库：Mysql8.0
服务器：Tomcat9
开发工具： IDEA

##主要功能
-用户注册与登录：表单验证、MD5加密、Session会话管理
-图书浏览与搜索：分页展示、按分类筛选以及关键词搜索
-图书详情：查看完整的图书信息、库存状态
-购物车管理：添加、删除商品、修改数量、批量删除
-订单模块：生成、查看订单列表与详情
-结算支付：收货地址选择、支付方式选择、模拟支付

##项目结构
bookstore/
├── src/main/java/com/bookstore/
│ ├── controller/ #控制器层
│ │ ├── UserController.java
│ │ ├── BookController.java
│ │ ├── CartController.java
│ │ ├── OrderController.java
│ │ └── PageController.java
│ ├── service/ #业务逻辑层
│ │ ├── UserService.java
│ │ ├── BookService.java
│ │ ├── CartService.java
│ │ └── OrderService.java
│ ├── dao/ #数据访问层
│ │ ├── UserMapper.java
│ │ ├── BookMapper.java
│ │ ├── CartMapper.java
│ │ └── OrderMapper.java
│ └── entity/ #实体类
│ ├── User.java
│ ├── Book.java
│ ├── Cart.java
│ └── Orders.java
├── src/main/resources/
│ ├── application.yml #配置文件
│ └── mapper/ # MyBatis XML映射文件
├── src/main/webapp/
└── WEB-INF/views/ #JSP视图文件


##实现步骤

###配置环境
-JDK 1.8及以上
-Mysql 8.0
-Maven3.6+
-Tomcat9（也可以使用SpringBoot内置）


###导入数据库
-创建数据库bookstore
-执行database/bookstore.sql的创建表语句

###修改数据库配置
-在开发工具中scr/main/resources/application.yml目录下修改username和psaaword为自己的数据库账号

###启动项目
-IDEA中导入Maven项目
-配置Tomcat9运行环境
-启动后，先尝试访问http://localhost:8080/api

###功能展示
-用户注册
-用户登录
-图书列表
-图书详情
-购物车
-订单确认

##核心代码示例

###用户注册（使用MD5密码加密）
//UserServiceImpl.java
public User register(User user){
//检查用户是否已经存在
if(UserMapper.findByUsername(user.getUserName())!=null){
throw new RuntimeException("用户名已存在");
}
//MD5加密   
String encryptedPassword=DigestUtil.md5DigestAsHex(
user.getPassword().getBytes());
user.setPassword(encryptedPassword);
user.setCreatedAt(new Data());
userMapper.insert(user);
return user;
}

###购物车添加商品
// CartServiceImpl.java
public Cart addToCart(Integer userId, Integer bookId, Integer quantity) {
// 检查是否已在购物车
Cart existingCart = cartMapper.findByUserIdAndBookId(userId, bookId);
if (existingCart != null) {
// 已存在则更新数量
int newQuantity = existingCart.getQuantity() + quantity;
cartMapper.updateQuantity(existingCart.getId(), newQuantity);
existingCart.setQuantity(newQuantity);
return existingCart;
} else {
// 不存在则新增
Cart cart = new Cart();
cart.setUserId(userId);
cart.setBookId(bookId);
cart.setQuantity(quantity);
cart.setSelected(true);
cartMapper.insert(cart);
return cart;
}
}

##待优化
-完善页面跳转逻辑
-在后续的添加中可以添加管理员后台功能
-添加图片上传功能


##学习收获
通过本次项目，我掌握了以下技能：
1.SSM框架整合：理解SpringMVC请求处理，Mybatis持久化机制
2.数据库设计：独立完成用户表、图书表、购物车表、订单表的设计与关联
3.密码安全：使用MD5对用户密码进行加密存储


##关于作者
-学校：广州航海需恶缘
-专业：计算机科学与技术
-姓名：徐诺
-GitHub：https://github.com/Naomi0412-04
-许可证：本项目仅用于学习交流，未经许可请勿用于商业用途












