package com.github.houbb.mvc.controller;

import com.github.houbb.mvc.annotation.Controller;
import com.github.houbb.mvc.annotation.RequestMapping;
import com.github.houbb.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * index 控制器
 *
 * @author binbin.hou
 * @since 1.0.0
 */
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
