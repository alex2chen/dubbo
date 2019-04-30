Spring Boot With Dubbo
===========================
主要介绍如何在Spring Boot中整合Dubbo的使用.

#### 注册中心

##### Redis
使用Redis注册中心，需要将在pom.xml中添加对应的redis客户端，代码如下：

```xml
     <dependency>
         <groupId>redis.clients</groupId>
         <artifactId>jedis</artifactId>
     </dependency>
```

对应的配置项为： spring.dubbo.registry = redis://localhost:6379

#####  ZooKeeper
使用ZooKeeper注册中心，需要在pom.xml中添加zookeeper需要的jar包，代码如下：

```xml
     <dependency>
         <groupId>org.apache.curator</groupId>
         <artifactId>curator-framework</artifactId>
         <version>2.12.0</version>
     </dependency>
     <dependency>
         <groupId>com.101tec</groupId>
         <artifactId>zkclient</artifactId>
         <version>0.10</version>
     </dependency>
```

对应的配置项为： spring.dubbo.registry = zookeeper://127.0.0.1:2181

多个zookeeper的配置项为: spring.dubbo.registry = zookeeper://192.168.0.2:2181,192.168.0.3:2181

#### 如何发布Dubbo服务?

* 首先在pom.xml中添加对spring-boot-starter-dubbo的引用

```xml     
        <dependency>
            <groupId>org.mvnsearch.spring.boot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```
       
* 在application.properties文件中添加相关的配置,如下:

```properties
        spring.dubbo.app = dubbo-Order-provider
        spring.dubbo.registry = redis://192.168.99.100:6379
        spring.dubbo.protocol = dubbo
        spring.dubbo.port= 20880
```
      
* 然后在Spring Boot Application程序中添加 @EnableDubboConfiguration,样例代码如下:

     
```java    
        @SpringBootApplication
        @EnableDubboConfiguration()
        public class SpringBootDubboServerApplication 
```

* 在Spring Bean上添加@DubboService Annotation进行Dubbo服务发布,代码如下:

```java  
    @Component
    @DubboService(interfaceClass = UicTemplate.class)
    public class UicTemplateImpl implements UicTemplate
```

#### 客户端如何引用Dubbo服务

考虑到客户端的bean引用,目前还是采用spring boot配置和传统的Xml声明方式,也就是dubbo的配置是配置的,dubbo beans是xml引用的方式.

* 首先在pom.xml中添加对spring-boot-starter-dubbo的引用

```xml        
        <dependency>
            <groupId>org.mvnsearch.spring.boot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

* 在application.properties文件中添加相关的配置,如下:

```properties
        spring.dubbo.app = dubbo-Order-consumer
        spring.dubbo.registry = redis://192.168.99.100:6379
        spring.dubbo.protocol = dubbo
```  
     
* 接下来你只需要创建一个ReferenceBean即可,代码如下。 这个也是Spring Boot推荐的做法。

```java
    @Bean
    public ReferenceBean<UicTemplate> OrderTemplate() {
        return getConsumerBean(UicTemplate.class, properties.getVersion(), properties.getTimeout());
    }
```

* 如果你不想创建上述的ReferenceBean,你也可以在在要引用的Dubbo Service Interface上添加 @DubboConsumer即可,代码如下:
```
   @DubboConsumer
   private UicTemplate OrderTemplate;
```
* 最后如果你还想用xml中声明 dubbo consumer beans的配置文件,样例如下:

```xml
       <?xml version="1.0" encoding="UTF-8"?>
       
       <beans xmlns="http://www.springframework.org/schema/beans"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
              xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
           http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
       
           <dubbo:reference id="OrderTemplate" interface="org.mvnsearch.Order.UicTemplate" timeout="1000000" />
       
       </beans>
```

* 然后在Spring Boot Application中使用@ImportResource引入dubbo consumer xml文件,如下:

```java  
       @SpringBootApplication
       @ImportResource("/Order-dubbo-consumer.xml")
       public class SpringBootDubboClientApplication
```

#### 优雅上下线&监控

* 当我们要重新发布应用时候,我们需要新停掉服务,然后稍等一段时间,等客户端连接全部切换到其他服务器上,这个时候我们才能开始部署服务。这个就是我们称之为优雅下线,
* 目前可以通过 http://localhost:8080/dubbo/offline
* Dubbo Endpint: spring-boot-starter-dubbo提供了dubbo的enpoint,通过该url可以快速了解Dubbo的运行信息
* health indicator: 对远程服务进行echo service调用进行health检查,通过 /health 进行查看

#### 如何测试

* 首先使用IntelliJ IDEA导入项目
* 调用docker-compose启动对应的注册中心: docker-compose up -d
* 启动 SpringBootDubboServerApplication
* 启动 SpringBootDubboClientApplication
* 打开浏览器访问 http://localhost:2080

#### 在 Docker 中运行

* 调用docker-compose启动对应的注册中心: docker-compose up -d
* 编译工程 `mvn clean package -Dmaven.test.skip`
* 将 server 打包成 docker image : `docker build -t dubbo-demo:latest spring-boot-dubbo-server`
* 运行 server (注意修改环境变量) : `docker run --rm --name=dubbo-demo -p 20890:20880 -e EXPORT_PORT=20890 -e EXPORT_HOST=YOUR_HOST_HERE -e ZK_HOST=YOUR_HOST_HERE dubbo-demo`
* 启动 SpringBootDubboClientApplication
* 打开浏览器访问 http://localhost:2080

#### Spring DevTools注意事项
由于Spring DevTools采用不一样的classloader的机制，所以会导致Dubbo Consumer Bean无法赋值到指定的@Component上，请使用以下规则：

在 src/main/resources/META-INF/spring-devtools.properties 在添加以下代码进行DevTools的classloader屏蔽：
```properties
restart.exclude.target-classes=/target/classes/
```
关于hotspot的模式下，相关Java代码调整后理解生效，可以考虑： http://dcevm.github.io/

如果你的应用是纯Dubbo服务，没有涉及到Web页面，不建议你添加spring-devtools，如果添加了后，
可以通过以下配置项关闭livereload服务，这样可以保证不必要的live reload服务启动。
```properties
spring.devtools.livereload.enabled=false
```

### todo

* DubboConsumerBuilder: 快速构建Dubbo Consumer
* zipkin: https://github.com/jessyZu/dubbo-zipkin-spring-starter

