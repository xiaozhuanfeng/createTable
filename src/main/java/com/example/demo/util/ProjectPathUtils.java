package com.example.demo.util;

import java.io.File;

public class ProjectPathUtils {
    //获取项目的根路径
    public final static String classPath;

    static {
        //获取的是classpath路径，适用于读取resources下资源
        classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    private ProjectPathUtils(){
        throw new AssertionError();
    }

    /**
     * 项目根目录
     */
    public static String getRootPath() {
        return rootPath("");
    }

    /**
     * 自定义追加路径
     */
    public static String getRootPath(String u_path) {
        return rootPath("/" + u_path);
    }

    /**
     * 私有处理方法
     */
    private static String rootPath(String u_path) {
        String rootPath = "";
        //windows下
        if ("\\".equals(File.separator)) {
            //System.out.println(classPath);
            rootPath = classPath + u_path;
            rootPath = rootPath.replaceAll("/", "\\\\");
            if (rootPath.substring(0, 1).equals("\\")) {
                rootPath = rootPath.substring(1);
            }
        }
        //linux下
        if ("/".equals(File.separator)) {
            //System.out.println(classPath);
            rootPath = classPath + u_path;
            rootPath = rootPath.replaceAll("\\\\", "/");
        }
        return rootPath;
    }

    public static void main(String[] args) {
        System.out.println(getRootPath());
    }
}
