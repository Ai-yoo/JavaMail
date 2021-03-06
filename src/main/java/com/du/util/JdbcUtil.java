package com.du.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtil {
    private Properties properties = new Properties();
    private static String dirverName;
    private static String url;
    private static String username;
    private static String password;

    private static ComboPooledDataSource dataSource;
    private static JdbcUtil jdbcUtil = new JdbcUtil();

    private JdbcUtil() {
        try {
            InputStream inputStream = JdbcUtil.class.getClassLoader()
                    .getResourceAsStream("datebase.properties");
//            从输入字节流读取属性列表（键和元素对）
            properties.load(inputStream);
//            用此属性列表中指定的键搜索属性，获取驱动，url，username，password
            dirverName = properties.getProperty("driverName").trim();
            url = properties.getProperty("url").trim();
            username = properties.getProperty("username").trim();
            password = properties.getProperty("password").trim();

            dataSource = new ComboPooledDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setJdbcUrl(url);
            dataSource.setDriverClass(dirverName);
            dataSource.setInitialPoolSize(5); //初始化连接数
            dataSource.setMinPoolSize(1);//最小连接数
            dataSource.setMaxPoolSize(20);//最大连接数
            dataSource.setMaxStatements(50);//最长等待时间
            dataSource.setMaxIdleTime(60);//最大空闲时间，单位毫秒
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JdbcUtil getInstance(){
        return jdbcUtil;
    }

    public synchronized Connection getConnection() {
        Connection conn = null;
        try {
            conn=dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //    利用静态块i，在类加载的时候就被执行
//    static {
//        try {
////            用流读入properties配置文件
//            InputStream inputStream = JdbcUtil.class.getClassLoader()
//                    .getResourceAsStream("datebase.properties");
//            Properties properties = new Properties();
////            从输入字节流读取属性列表（键和元素对）
//            properties.load(inputStream);
////            用此属性列表中指定的键搜索属性，获取驱动，url，username，password
//            dirverName = properties.getProperty("driverName").trim();
//            url = properties.getProperty("url").trim();
//            username = properties.getProperty("username").trim();
//            password = properties.getProperty("password").trim();
//            System.out.println(dirverName);
//            System.out.println(url);
//            System.out.println(username);
//            System.out.println(password);
////            加载驱动
//            Class.forName(dirverName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

//    获取数据库连接
//    public static Connection getConnection() {
//        Connection conn=null;
//        try {
//            conn=DriverManager.getConnection(url, username, password);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }

//关闭连接工具方法
    public static void releaseResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
