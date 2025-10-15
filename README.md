## 本地运行

1）将前后端代码下载至本地

2）修改后端数据库配置为你自己的数据库

`nutrition-platform\src\main\resources\application.yml`

~~~~~~yml
# 数据库配置
datasource:
	driver-class-name: com.mysql.cj.jdbc.Driver
	url: jdbc:mysql://localhost:3306/nutrition_platform?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
	username: 自己的账号
	password: 自己的密码
~~~~~~

3）依次执行`nutrition-platform\sql`下的`create_table.sql`和`init_date.sql`创建初始化数据

4）进入前端目录（nutrition-web）下执行`npm install`安装依赖

5）依次启动后端和前端（`npm run dev`）