

# 概述 #
	淘淘商城主要参照网上已有开源的项目及其前端，自己实现所有后台逻辑及其上线。

## 商城功能描述 ##
	

- 用户可以注册、登录商城
- 用户可以浏览商品，下订单以及参加各种活动；
- 商家可以在后台上架、下架自己商品
- 商家可以在后台管理订单出货

## 项目架构 ##
![](http://i.imgur.com/I8uA1V4.png)

采用分布式，解决了如下问题
1. 开发、为代码的困难
2. 系统内部的功能模块的耦合度问题
3. 无法针对每个模块的特征做优化
4. 无法做到服务器的水平扩展
5. 无法海量存储数据

但是即使是分布式系统设计，还是会有新的问题：
- 系统间的服务调用变得复杂
- 需要借助服务注册中心的来解决复杂的调用关系

ps:项目当前仍是采用分布式架构，后期会逐渐转变为zookeeper+dubbo的分布式SOA架构

# 各个系统说明 #

- 后台管理系统(完成)
- 前台系统(大部分完成)
- 会员系统(未完成)
- 订单系统(部分完成)
- 搜索系统(完成主要的搜索功能)
- 单点登录系统(完成)

## 所用到的技术 ##
- SSM三大框架以及javaEE周边技术
- Redis(缓存)
- Solr(搜索)
- httpclient(调用系统服务)
- Mysql(关系数据库)
- Nginx(web服务器)
- Rabbitmq(消息队列)
- zookeeper+dubbo(服务中间件)
- Roketmq(后期取代Rabbitmq)

## 开发工具及环境 ##

- IntelliJ IDEA
- Mysql 5.7
- Maven 3
- Tomcat 7
- JDK 8
- Redis 3.0
- win 10
- 阿里云ECS

## 项目结构 ##
![](http://i.imgur.com/GM6yxZk.png)

其中：
	taotao-jd-crawler模块用于爬取京东数据
	taotao-solr用于导入Solr初始数据

依赖关系
	
	|-taotao-parent
	|
	|-taotao-common
	   	|
	   	|-taotao-cart
	  	|
	   	|-taotao-order
	   	|
	    |-taotao-search
		|
    	|-taotao-solr
		|
		|-taotao-sso
		|
		|-taotao-web
		|
		|-taotao-manager
			|
			|-taotao-manager-mapper
			|
			|-taotao-manager-pojo
			|
			|-taotao-manager-service
			|
			|-taotao-manager-web

	