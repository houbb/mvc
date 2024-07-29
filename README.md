# mvc

[mvc](https://github.com/houbb/mvc) 是一款简化版本的 spring mvc 框架，主要用于 mvc 原理的学习。

> [自己手写 spring mvc 简单实现](https://houbb.github.io/2018/09/27/java-servlet-tutorial-21-fake-mvc-simple)

[![Build Status](https://travis-ci.com/houbb/mvc.svg?branch=master)](https://travis-ci.com/houbb/mvc)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/mvc/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/mvc)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/houbb/mvc/blob/master/LICENSE.txt)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/houbb/mvc)

# 快速开始

## 准备

- JDK 1.7

- Maven 3.x+

## 下载

```
$  git clone https://github.com/houbb/mvc.git
```

## 启动

- 编译

```
$   mvc clean install
```

- 运行

mvc-test 模块，使用 tomcat 插件运行

## 访问

[http://localhost:8081/index/echo?param=1](http://localhost:8081/index/echo?param=1)

页面返回

```
Echo :1
```

# 入门使用

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>mvc-core</artifactId>
    <version>0.0.2</version>
</dependency>
```

## 使用

和 spring mvc 类似

```java
import com.github.houbb.mvc.annotation.Controller;
import com.github.houbb.mvc.annotation.RequestMapping;
import com.github.houbb.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/print")
    public void print(@RequestParam("param") String param) {
        System.out.println(param);
    }

    @RequestMapping("/echo")
    public void echo(HttpServletRequest request,
                     HttpServletResponse response,
                     @RequestParam("param") String param) {
        try {
            response.getWriter().write("Echo :" + param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
```

# 后期 Road-Map

- [ ] 全覆盖 spring-mvc 特性

- [ ] IOC 与 MVC 整合

# 拓展阅读

[Java Servlet 教程-20-自己手写实现 spring mvc 整体思路](https://houbb.github.io/2018/09/27/java-servlet-tutorial-20-fake-mvc)

[Java Servlet 教程-21-自己手写 spring mvc 简单实现](https://houbb.github.io/2018/09/27/java-servlet-tutorial-21-fake-mvc-simple)

[Spring Web MVC-00-重学 mvc 系列](https://houbb.github.io/2019/12/25/springmvc-00-index)

[mvc-01-Model-View-Controller 概览](https://houbb.github.io/2016/05/14/mvc-01-overview)

[web mvc-03-JFinal](https://houbb.github.io/2016/05/14/mvc-03-jfinal-intro)

[web mvc-04-Apache Wicket](https://houbb.github.io/2016/05/14/mvc-04-apache-whicket-intro)

[web mvc-05-JSF JavaServer Faces](https://houbb.github.io/2016/05/14/mvc-05-jsf-intro)

[web mvc-06-play framework intro](https://houbb.github.io/2016/05/14/mvc-06-play-framework-intro)

[web mvc-07-Vaadin](https://houbb.github.io/2016/05/14/mvc-07-Vaadin)

[web mvc-08-Grails](https://houbb.github.io/2016/05/14/mvc-08-Grails)

# 从零手写组件系列

[java 从零手写 spring ioc 控制反转](https://github.com/houbb/ioc)

[java 从零手写 spring mvc](https://github.com/houbb/mvc)

[java 从零手写 jdbc-pool 数据库连接池](https://github.com/houbb/jdbc-pool)

[java 从零手写 mybatis](https://github.com/houbb/mybatis)

[java 从零手写 hibernate](https://github.com/houbb/hibernate)

[java 从零手写 rpc 远程调用](https://github.com/houbb/rpc)

[java 从零手写 mq 消息组件](https://github.com/houbb/rpc)

[java 从零手写 cache 缓存](https://github.com/houbb/cache)

[java 从零手写 nginx4j](https://github.com/houbb/nginx4j)

[java 从零手写 tomcat](https://github.com/houbb/minicat)
