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

# 拓展框架

[template](https://github.com/houbb/template)

[cola](https://github.com/houbb/cola)

[ioc](https://github.com/houbb/ioc)