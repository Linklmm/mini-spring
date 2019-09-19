package com.lmm.beans;

import com.lmm.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 17:16
 * @Version 1.0
 * @Description
 */
public class BeanFactory {
    /**
     * bean和实体类的映射
     */
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    /**
     * 获取bean
     *
     * @param cls
     * @return
     */
    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    /**
     * 初始化bean
     *
     * @param classList
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() != 0) {
            //存储容器大小
            int remainSize = toCreate.size();
            for (int i=0;i<toCreate.size();i++){
                //是否完成bean创建，创建完成删除
                if (finishCreate(toCreate.get(i))){
                    toCreate.remove(i);
                }
            }
            //判断容器是否发生改变，如果没变化，说明发生了死循环
            if (toCreate.size()==remainSize){
                throw new Exception("cycle dependency!");
            }

        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        if (!cls.isAnnotationPresent(Bean.class)&&!cls.isAnnotationPresent(Controller.class)){
            return true;
        }
        Object bean = cls.newInstance();
        for (Field field :cls.getDeclaredFields()){
            //属性是否有被autowried注解，有则需要依赖注入
            if (field.isAnnotationPresent(AutoWired.class)){
                Class<?> fieldType = field.getType();
                //从beanFactory中获取bean
                Object reliantBean = BeanFactory.getBean(fieldType);
                if (reliantBean == null){
                    return false;
                }
                //调用私有域和方法
                field.setAccessible(true);
                field.set(bean,reliantBean);
            }
        }
        classToBean.put(cls,bean);
        return true;
    }
}
