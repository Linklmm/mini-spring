package com.lmm.starter;

import com.lmm.beans.BeanFactory;
import com.lmm.core.ClassScanner;
import com.lmm.web.handler.HandlerManager;
import com.lmm.web.sever.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.util.List;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 14:06
 * @Version 1.0
 * @Description
 */
public class MiniApplication {
    public static void run(Class<?> cls,String[] args){
        System.out.println("Hello MiniApplication");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            //获取所有的class
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            BeanFactory.initBean(classList);
            //调用handlerManager处理类中所有的handler
            HandlerManager.resolveMappingHandler(classList);
            classList.forEach(it-> System.out.println(it));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
