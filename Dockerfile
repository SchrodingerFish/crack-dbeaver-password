# 多阶段构建 - 构建阶段
FROM maven:3.6.3-openjdk-8-slim AS build

# 设置工作目录
WORKDIR /app

# 复制pom.xml和Maven配置文件
COPY pom.xml .
COPY .m2/settings.xml /root/.m2/settings.xml

# 复制源代码
COPY src ./src

# 编译打包，跳过测试
RUN mvn clean package -DskipTests -T 4C

# 运行阶段
FROM openjdk:8-jre-slim

# 设置维护者信息
LABEL maintainer="https://github.com/schrodingerfish"
LABEL description="DBeaver密码破解工具"
LABEL version="1.1.7"

# 设置环境变量
ENV LANG=zh_CN.UTF-8
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Duser.timezone=GMT+08 -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

# 设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用目录
WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=build /app/target/crack-dbeaver-password-*.jar app.jar

# 暴露8080端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
