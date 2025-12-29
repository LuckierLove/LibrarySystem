package dev.ganziyan.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 数据库工具类
 * 功能：提供数据库连接管理、资源释放等基础功能
 *
 */
public class DBUtil {
    
    // 数据库连接参数
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    
    // 在类加载时读取配置文件并注册数据库驱动
    static {
        try {
            // 加载数据库配置文件
            Properties properties = new Properties();
            InputStream inputStream = DBUtil.class.getClassLoader()
                .getResourceAsStream("db.properties");
            
            if (inputStream == null) {
                throw new RuntimeException("无法找到数据库配置文件 db.properties");
            }
            
            properties.load(inputStream);
            
            // 读取配置参数
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            driver = properties.getProperty("db.driver");
            
            // 注册数据库驱动
            Class.forName(driver);
            
            System.out.println("数据库驱动加载成功！");
            
        } catch (ClassNotFoundException e) {
            System.err.println("数据库驱动加载失败：" + e.getMessage());
            throw new RuntimeException("数据库驱动加载失败", e);
        } catch (IOException e) {
            System.err.println("读取配置文件失败：" + e.getMessage());
            throw new RuntimeException("读取配置文件失败", e);
        }
    }
    
    /**
     * 获取数据库连接
     * 
     * @return Connection 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            System.err.println("获取数据库连接失败：" + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 关闭数据库连接
     * 
     * @param connection 数据库连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("关闭数据库连接失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 关闭 Statement 对象
     * 
     * @param statement Statement 对象
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("关闭Statement失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 关闭 PreparedStatement对象
     * 
     * @param preparedStatement PreparedStatement 对象
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.err.println("关闭PreparedStatement失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 关闭 ResultSet 对象
     * 
     * @param resultSet ResultSet 对象
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("关闭ResultSet失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 关闭所有数据库资源
     * 统一关闭连接、语句和结果集，简化资源管理
     * 
     * @param connection 数据库连接
     * @param statement SQL 语句对象
     * @param resultSet 结果集对象
     */
    public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }
    
    /**
     * 关闭数据库资源（PreparedStatement版本）
     * 
     * @param connection 数据库连接
     * @param preparedStatement 预编译 SQL 语句对象
     * @param resultSet 结果集对象
     */
    public static void closeAll(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closePreparedStatement(preparedStatement);
        closeConnection(connection);
    }
    
    /**
     * 测试数据库连接是否正常
     * 
     * @return boolean 连接成功返回true，否则返回false
     */
    public static boolean testConnection() {
        Connection connection = null;
        try {
            connection = getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("数据库连接测试成功！");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return false;
    }
    
    /**
     * 主方法：用于测试数据库连接
     */
    public static void main(String[] args) {
        // 测试数据库连接
        if (testConnection()) {
            System.out.println("数据库配置正确，可以正常使用！");
        } else {
            System.out.println("数据库配置有误，请检查配置文件！");
        }
    }
}
