# Java自用后端方案（Spring分布式架构）

#### 介绍

使用到的技术包括：

- SpringMVC

- SpringBoot

- JWT实现无状态token

- gateway网关

- Mybatis框架

- MybatisPlus增强

- Redis做验证码缓存

- Springcloud

- Nacos 服务注册中心 实现负载均衡

- SpringAMQP

如果对安全性有一定的需求，可以考虑以下思路：

- 利用gateway网关做参数加解密

- jwt荷载加解密


#### 环境

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

#### 说明

### 1.接口权限声明注解

首先在配置类中注册拦截器ProjectInterceptor

```
    protected void addInterceptors(InterceptorRegistry registry) {
        //这里可以根据自己的具体需求来
        registry.addInterceptor(projectInterceptor).addPathPatterns("/**");
    }
```
随后便可在接口上方使用该注解@ShiroCheck(roles = "")

```
    @GetMapping("/{id}")
    @ShiroCheck(roles = "test")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }
```

#### 最近修改

2022-12-09 新增tcp引擎
