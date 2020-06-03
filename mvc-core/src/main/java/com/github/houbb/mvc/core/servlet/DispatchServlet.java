package com.github.houbb.mvc.core.servlet;

import com.github.houbb.mvc.annotation.Controller;
import com.github.houbb.mvc.annotation.RequestMapping;
import com.github.houbb.mvc.annotation.RequestParam;
import com.github.houbb.mvc.core.exception.MvcRuntimeException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class DispatchServlet extends HttpServlet {

    /**
     * 实例 Map
     *
     * @since 0.0.1
     */
    private Map<String, Object> controllerInstanceMap = new HashMap<>();

    /**
     * 请求方法 map
     *
     * @since 0.0.1
     */
    private Map<String, Method> requestMethodMap = new HashMap<>();

    /**
     * 配置文件
     *
     * @since 0.0.1
     */
    private Properties properties = new Properties();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();

        //1. 加载配置文件信息
        initConfig(config);

        //2. 根据配置信息进行相关处理
        String basePackage = properties.getProperty("basePackage");
        initInstance(basePackage);

        //3. 初始化映射关系
        initRequestMappingMap();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    /**
     * 初始化配置信息
     * （1）spring-mvc 一般是指定一个 xml 文件。
     * 至于各种 classpath，我们也可以对其进行特殊处理，暂时简单化。
     *
     * @param config 配置信息
     * @since 0.0.1
     */
    private void initConfig(final ServletConfig config) {
        final String configPath = config.getInitParameter("contextConfigLocation");

        //把web.xml中的contextConfigLocation对应value值的文件加载到流里面
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(configPath)) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new MvcRuntimeException(e);
        }
    }


    /**
     * 初始化对象实例
     *
     * @param basePackage 基本包
     * @since 0.0.1
     */
    private void initInstance(final String basePackage) {
        String path = basePackage.replaceAll("\\.", "/");
        URL url = this.getClass().getClassLoader().getResource(path);
        if (null == url) {
            throw new MvcRuntimeException("base package can't loaded!");
        }

        File dir = new File(url.getFile());
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归读取包
                    initInstance(basePackage + "." + file.getName());
                } else {
                    String className = basePackage + "." + file.getName().replace(".class", "");

                    //实例化处理
                    try {
                        Class clazz = Class.forName(className);

                        if (clazz.isAnnotationPresent(Controller.class)) {
                            Object instance = clazz.newInstance();
                            controllerInstanceMap.put(className, instance);
                        }
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        throw new MvcRuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * 初始化 {@link com.github.houbb.mvc.annotation.RequestMapping} 的方法映射
     *
     * @since 0.0.1
     */
    private void initRequestMappingMap() {
        for (Map.Entry<String, Object> entry : controllerInstanceMap.entrySet()) {
            Object instance = entry.getValue();
            String prefix = "/";

            final Class controllerClass = instance.getClass();
            if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                prefix = requestMapping.value();
            }

            // 暂时只处理当前类的方法
            Method[] methods = controllerClass.getDeclaredMethods();

            // 为了简单，只有注解处理的方法才被作为映射。
            // 当然这里可以加一些限制，比如只处理 public 方法等。
            // 可以加一些严格的判重，暂不处理。
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    String methodUrl = requestMapping.value();
                    String fullUrl = prefix + methodUrl;

                    requestMethodMap.put(fullUrl, method);
                }
            }
        }
    }

    /**
     * 执行消息的分发
     *
     * @param req  请求
     * @param resp 响应
     * @since 0.0.1
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (requestMethodMap.isEmpty()) {
                return;
            }

            // 请求信息 url 的处理
            String requestUrl = req.getRequestURI();

            String contextPath = req.getContextPath();
            // 直接替换掉 contextPath，感觉这样不太好。可以选择去掉这种开头。
            requestUrl = requestUrl.replace(contextPath, "");


            //404
            // 这里应该有各种响应码的处理，暂时简单点。
            if (!requestMethodMap.keySet().contains(requestUrl)) {
                resp.getWriter().write("404 for request " + requestUrl);
                return;
            }

            Method method = requestMethodMap.get(requestUrl);
            // 参数，这里其实要处理各种基本类型等
            // 还有各种信息的类型转换
            Class<?>[] paramTypes = method.getParameterTypes();
            // 参数为空的情况
            final Object instance = controllerInstanceMap.get(method.getDeclaringClass().getName());
            if (paramTypes.length <= 0) {
                method.invoke(instance);
            }

            // 这里实际上需要对各种类型的参数加以转换，目前只支持 String
            Object[] paramValues = new Object[paramTypes.length];
            List<String> paramNames = getParamNames(method);

            Map<String, String[]> requestParamMap = req.getParameterMap();
            for (int i = 0; i < paramTypes.length; i++) {
                // 类的简称
                String typeName = paramTypes[i].getSimpleName();

                if ("HttpServletRequest".equals(typeName)) {
                    //参数类型已明确，这边强转类型
                    paramValues[i] = req;
                } else if ("HttpServletResponse".equals(typeName)) {
                    paramValues[i] = resp;
                } else if("String".endsWith(typeName)) {
                    // 为什么是数组 https://www.cnblogs.com/wscit/p/5800147.html
                    String paramName = paramNames.get(i);
                    String[] strings = requestParamMap.get(paramName);

                    // 简单处理，只取第一个。
                    paramValues[i] = strings[0];
                } else {
                    throw new MvcRuntimeException("Not support type for " + typeName);
                }
            }

            // 反射调用
            method.invoke(instance, paramValues);
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            resp.getWriter().write("500");
        }
    }

    /**
     * 获取参数的名称
     * （1）后期可以结合 asm，对于没有注解的也可以处理。
     *
     * @param method 方法
     * @return 结果
     * @since 0.0.1
     */
    private List<String> getParamNames(final Method method) {
        Annotation[][] paramAnnos = method.getParameterAnnotations();
        List<String> paramNames = new ArrayList<>(paramAnnos.length);

        final int paramSize = paramAnnos.length;
        for(int i = 0; i < paramSize; i++) {
            Annotation[] annotations = paramAnnos[i];
            String paramName = getParamName(i, annotations);
            paramNames.add(paramName);
        }

        return paramNames;
    }

    /**
     * 获取参数名称
     *
     * TODO: 直接使用 asm-tool 获取真实的名称
     * @param index 参数的下标
     * @param annotations 注解信息
     * @return 参数名称
     * @since 0.0.1
     */
    private static String getParamName(final int index, final Annotation[] annotations) {
        final String defaultName = "arg"+index;
        if(annotations == null) {
            return defaultName;
        }

        for(Annotation annotation : annotations) {
            if(annotation.annotationType().equals(RequestParam.class)) {
                RequestParam param = (RequestParam)annotation;
                return param.value();
            }
        }

        return defaultName;
    }

}
