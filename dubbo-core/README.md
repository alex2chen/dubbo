Dubbo: 分布式通讯框架
======================================
Dubbo是一个高性能的分布式RPC框架，主要包括一下部分:

* Remoting(远程通信): a network communication framework provides sync-over-async and request-response messaging.
* Clustering(集群): a remote procedure call abstraction with load-balancing/failover/clustering capabilities.
* Registry(注册中心): a service directory framework for service registration and service event publish/subscription

文档地址: http://alibaba.github.io/dubbo-doc-static/Developer+Guide-zh.htm

## 背景
由于Dubbo的版本并不是特别活跃，加上和jdk8及Spring Boot的jar包适配等问题

## dubbo-core

### Dubbo 1.x的区别
* 代码迁移到Java 8
* @Service 更改为 @DubboService
* hessian序列化支持Java 8 Optional，暂时不支持容器类，如List, Map
* 序列化调整到hessian2协议上
* zookeeper有zkClient调整到curator
* 多注册中心: 删除simple registry，新增Consul，继续支持Redis、ZooKeeper
* 通讯协议: 默认Netty4， 删除thrift，http，Grizzly，rmi等协议支持
* 容器: 取消Jetty支持，使用Spring Boot替换
* Docker: 在 Protocol 配置中增加了 exportHost 和 exportPort 参数, 区分容器内绑定的真实地址和注册到注册中心的宿主机地址

### Todo
* 精简代码Lombok
* javassist替换为byte-buddy，[参阅](https://jrebel.com/rebellabs/testing-the-performance-of-4-java-runtime-code-generators-cglib-javassist-jdk-proxy-byte-buddy/)
* Reactive支持: Reactor & RxJava

##spring-boot-dubbo

### 与Spring Boot兼容
* Metrics集成，这个在Dubbo Spring Boot中通过filter机制完成更方便
* DevOps API: 各种信息暴露

