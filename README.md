# DBeaver密码破解工具

> DBeaver密码破解工具，我的密码必须由我做主。

DBeaver客户端非常强大，可以连接一百多种数据库。但是最难受的地方是不允许方便的查看已保存的密码，如果一定要查看密码，必须先设定一个主密码，而且每次打开客户端都需要验证主密码，所以我拒绝。于是找了一下网络上有同样困惑的人，然后整理重构为此项目。希望可以更方便的拿到自己的明文密码。

## 项目简介

这是一个用于破解DBeaver数据库连接密码的Spring Boot应用程序。DBeaver将数据库连接密码加密存储在本地配置文件中，本工具可以解密这些密码。

## 功能特性

- 🔓 解密DBeaver存储的数据库连接密码
- 🌐 提供Web API接口
- 📁 支持上传credentials-config.json文件进行解密
- 🔧 支持自定义密钥配置
- 📖 集成Swagger API文档

## 技术栈

- **Java**: 1.8
- **Spring Boot**: 2.3.7.RELEASE
- **Maven**: 项目构建工具
- **Hutool**: Java工具库
- **Knife4j**: API文档生成
- **Lombok**: 简化Java代码

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+

### 本地运行

1. 克隆项目
```bash
git clone <repository-url>
cd crack-dbeaver-password
```

2. 编译打包
```bash
mvn clean package -DskipTests
```

3. 运行应用
```bash
java -jar target/crack-dbeaver-password-1.1.7.jar
```

4. 访问应用
- 应用地址: http://localhost:8080
- API文档: http://localhost:8080/doc.html

### Docker运行

1. 构建镜像
```bash
docker build -t crack-dbeaver-password:latest .
```

2. 运行容器
```bash
docker run -d -p 8080:8080 --name crack-dbeaver-password crack-dbeaver-password:latest
```

## API接口

### 1. 默认解密
```
GET /decrypt/default
```
使用默认配置文件路径进行解密。

### 2. 上传文件解密
```
POST /decrypt/upload
```
上传DBeaver的`credentials-config.json`文件进行解密。

**参数:**
- `file`: MultipartFile类型，DBeaver的credentials-config.json文件

### 3. 组合连接解密
```
POST /decrypt/upload2
```
结合之前解密的密码和新上传的配置文件进行组合解密。

## 配置说明

### 应用配置

可以通过环境变量或application.properties配置以下参数：

- `LOCAL_KEY_CACHE`: 自定义解密密钥
- `CREDENTIALS_CONFIG`: DBeaver配置文件路径

### DBeaver配置文件位置

默认配置文件路径：
- Windows: `C:/Users/{用户名}/AppData/Roaming/DBeaverData/workspace6/General/.dbeaver/credentials-config.json`
- macOS: `~/Library/DBeaverData/workspace6/General/.dbeaver/credentials-config.json`
- Linux: `~/.local/share/DBeaverData/workspace6/General/.dbeaver/credentials-config.json`

## 使用说明

1. **找到DBeaver配置文件**: 在上述路径中找到`credentials-config.json`文件
2. **上传文件**: 通过`/decrypt/upload`接口上传配置文件
3. **获取密码**: 接口返回解密后的数据库连接信息

## 注意事项

⚠️ **重要提醒**:
- 本工具仅用于合法的密码恢复场景
- 请确保您有权限访问相关的DBeaver配置文件
- 不要将此工具用于非法目的
- 解密后的密码信息请妥善保管

## 开发说明

### 项目结构
```
src/main/java/cn/java666/tools/crackdbeaverpassword/
├── Main.java                 # 应用启动类
├── JsonCombUtil.java         # JSON组合工具类
├── config/
│   └── APIConfig.java        # API配置
├── controller/
│   └── MyController.java     # 控制器
└── service/
    └── MyService.java        # 解密服务
```

### 构建部署

```bash
# 编译打包
mvn clean package -DskipTests

# Docker构建
docker build -t crack-dbeaver-password:latest .

# Docker推送
docker push your-registry/crack-dbeaver-password:latest
```

## 许可证

本项目仅供学习和合法使用，请在24小时内删除，使用者需自行承担使用风险。

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

---

**免责声明**: 本工具仅用于教育和合法的密码恢复目的。使用者应确保遵守当地法律法规，作者不承担任何法律责任。
