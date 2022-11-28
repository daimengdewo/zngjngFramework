# Java后端自用框架（Spring分布式架构）

#### 介绍

Spring分布式架构示例项目，纯后端代码，预计包含详细注释。

使用到的技术包括：

1.SpringMVC
2.SpringBoot
3.JWT实现无状态token  配合gateway
4.Mybatis框架
5.MybatisPlus增强
6.Redis做验证码缓存
7.Springcloud
8.nacos 注册中心 实现负载均衡

#### 软件架构

### 1.mysql 主从部署

一主二从 主库可读写 从库只读
使用keepalived实现高可用
配合haproxy实现负载均衡

### 2.docker 容器化部署

nacos 使用容器化部署
搭建集群 并配合keepalived实现高可用

redis 使用容器化部署
三台宿主机，各部署两个实例，总共六个实例
配置redis集群

项目各服务同样使用docker进行容器化部署
三台宿主机，各部署两个实例，总共六个实例
ribbon负载均衡

更多详情可以看我的个人博客：
https://blog.csdn.net/qq_33944367?type=blog

#### 使用说明

大佬看得上随便用哈。

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
