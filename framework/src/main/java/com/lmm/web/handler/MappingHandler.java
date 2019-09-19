package com.lmm.web.handler;

import com.lmm.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 16:28
 * @Version 1.0
 * @Description 请求处理器
 * 通过反射实现mappingHandler
 */
public class MappingHandler {
    /**
     * 请求的URI
     */
    private String uri;
    /**
     * controller对应的方法
     */
    private Method method;
    /**
     * 对应的类
     */
    private Class<?> controller;
    /**
     * 方法的参数
     */
    private String[] args;

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    /**
     *
     * @param req
     * @param res
     * @return
     */
    public boolean handler(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest)req).getRequestURI();
        //判断handler存储的URI和传入的URI是否相等
        if (!uri.equals(requestUri)){
            return false;
        }

        Object[] parameters = new Object[args.length];
        for (int i = 0;i<args.length;i++){
            parameters[i] =req.getParameter(args[i]);
        }
        //实例化controller
        Object ctl = BeanFactory.getBean(controller);
        //存储方法返回结果
        Object response = method.invoke(ctl,parameters);
        res.getWriter().println(response.toString());
        return true;
    }
}
