package com.lmm.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: mini-spring
 * @Author myFlowerYourGrass
 * @Date 2019-09-19 15:57
 * @Version 1.0
 * @Description 类扫描
 * 通过类加载机制实现类的扫描
 */
public class ClassScanner {
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        //存储扫描到的类
        List<Class<?>> classList = new ArrayList<>();
        //包名转化为文件路径
        String path = packageName.replace(".", "/");
        //获取默认的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //得到可遍历的URL资源
        Enumeration<URL> resources = classLoader.getResources(path);
        //遍历URL资源
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            //资源类型为jar包的情况处理 判断资源类型
            if (resource.getProtocol().contains("jar")) {
                //获取jar包的绝对路径
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                //jar包的路径名
                String jarFilePath = jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath, path));
            } else {
                //todo:其他的资源类型处理
            }
        }
        return classList;
    }

    /**
     * 通过jar包路径获取jar包下所有的类
     *
     * @param jarFilePath jar包路径
     * @param path 指定类文件
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        //jar包路径转化为jarfile实例
        JarFile jarFile = new JarFile(jarFilePath);
        //遍历
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            //com/lmm/test/Test.class
            String entryName = jarEntry.getName();
            //以path开头，.class结尾
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                //.替换为/，去除.class结尾
                String classFullName = entryName.replace("/", ".")
                        .substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
