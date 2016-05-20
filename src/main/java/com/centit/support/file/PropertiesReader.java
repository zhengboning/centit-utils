package com.centit.support.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * Properties 文件工具类
 * 
 * @author sx
 * 
 */
public class PropertiesReader {
    private static final Log log = LogFactoryImpl.getLog(PropertiesReader.class);

    /**
     * 读取classpath下文件
     * 
     * @param fileName
     *            文件名前需要加 "/"，如 "/system.properties"
     * @param key
     * @return
     */
    public static String getClassPathProperties(String fileName, String key) {
        try {
        	InputStream in = PropertiesReader.class.getResourceAsStream(fileName);   
            return getPropertyValue(in, key);
        } catch (IOException e) {
            log.error("读取系统配置文件出错", e);
        }

        return "";
    }
	/**
     * 读取classpath下文件
     * @param clazz 任意类型
     * @param fileName
     *            文件名前需要加 "/"，如 "/system.properties"
     * @param key
     * @return
     */
    public static String getClassPathProperty(Class<?> clazz, String fileName, String key) {
        try {
        	InputStream in = clazz.getResourceAsStream(fileName);   
            return getPropertyValue(in, key);
        } catch (IOException e) {
            log.error("读取系统配置文件出错", e);
        }

        return "";
    }
    
    /**
     * 读取非classpath下文件
     * 
     * @param fileName
     *            文件全路径及文件名，文件名前需要加 "/"，如 "/system.properties"
     * @param key
     * @return
     */
    public static String getFilePathProperties(String fileName, String key) {
        try {
            return getPropertyValue(new FileInputStream(new File(fileName)), key);
        } catch (IOException e) {
            log.error("读取系统配置文件出错", e);
        }

        return "";
    }

    /**
     * 读取classpath下文件
     * 
     * @param fileName
     *            文件名，文件名前需要加 "/"，如 "/system.properties"
     * @param key
     * @return
     */
    public static Properties getClassPathProperties(String fileName) {
        try {
        	InputStream in = PropertiesReader.class.getResourceAsStream(fileName);         	
            return loadProperties(in);
        } catch (IOException e) {
            log.error("读取系统配置文件出错", e);
        }

        return null;
    }
    
    public static Properties getClassPathProperties( Class<?> clazz, String fileName) {
        try {
        	InputStream in = clazz.getResourceAsStream(fileName);         	
            return loadProperties(in);
        } catch (IOException e) {
            log.error("读取系统配置文件出错", e);
        }

        return null;
    }
   
    
    /**
     * 读取非classpath下文件
     * 
     * @param fileName
     *            文件全路径及文件名
     * @param key
     * @return
     */
    public static Properties getFilePathProperties(String fileName) {
        try {
            return loadProperties(new FileInputStream(new File(fileName)));
        } catch (IOException e) {
            log.error("读取系统配置文件出错", e);
        }

        return null;
    }

    private static String getPropertyValue(InputStream resource, String key) throws IOException {
        Properties prop = new Properties();
        prop.load(resource);

        return prop.getProperty(key);
    }

    private static Properties loadProperties(InputStream resource) throws IOException {
        Properties prop = new Properties();
        prop.load(resource);
        return prop;
    }

}
