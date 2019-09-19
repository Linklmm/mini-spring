package com.lmm.web.handler;

import com.lmm.web.mvc.Controller;
import com.lmm.web.mvc.RequestMapping;
import com.lmm.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 16:32
 * @Version 1.0
 * @Description handler管理器
 */
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 解析handler
     *
     * @param classList
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            //判断是否有controller注解
            if (cls.isAnnotationPresent(Controller.class)) {
                //解析controller类
                parseHandlerFormController(cls);
            }
        }
    }

    /**
     * 解析controller类中的元素
     * 通过反射实现mappingHandler
     * @param cls
     */
    private static void parseHandlerFormController(Class<?> cls) {
        //得到类中的所有方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //得到URI
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            //得到参数数组
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            //设置handler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
