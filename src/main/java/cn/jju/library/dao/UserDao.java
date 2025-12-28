package cn.jju.library.dao;

import cn.jju.library.entity.User;
import cn.jju.library.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象
 * 负责用户信息的数据库CRUD操作
 * 
 * @author 图书管理系统开发组
 * @version 1.0.0
 */
public class UserDao {
    
    /**
     * 用户登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，如果验证失败则返回null
     */
    public User login(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = '正常'";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("用户登录验证失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return null;
    }
    
    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM users ORDER BY user_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("查询所有用户失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return users;
    }
    
    /**
     * 根据ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    public User findById(Integer userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("根据ID查询用户失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return null;
    }
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象，如果不存在则返回null
     */
    public User findByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("根据用户名查询用户失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return null;
    }
    
    /**
     * 添加用户
     * 
     * @param user 用户对象
     * @return true表示添加成功，false表示添加失败
     */
    public boolean add(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "INSERT INTO users (username, password, real_name, gender, " +
                        "phone, email, user_type, status, max_borrow_count) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRealName());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getUserType());
            preparedStatement.setString(8, user.getStatus());
            preparedStatement.setInt(9, user.getMaxBorrowCount());
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("添加用户失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return true表示更新成功，false表示更新失败
     */
    public boolean update(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "UPDATE users SET username = ?, password = ?, real_name = ?, " +
                        "gender = ?, phone = ?, email = ?, user_type = ?, status = ?, " +
                        "max_borrow_count = ? WHERE user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRealName());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getUserType());
            preparedStatement.setString(8, user.getStatus());
            preparedStatement.setInt(9, user.getMaxBorrowCount());
            preparedStatement.setInt(10, user.getUserId());
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("更新用户失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return true表示删除成功，false表示删除失败
     */
    public boolean delete(Integer userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "DELETE FROM users WHERE user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("删除用户失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 从ResultSet中提取用户对象
     * 
     * @param resultSet 结果集
     * @return 用户对象
     * @throws SQLException SQL异常
     */
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setRealName(resultSet.getString("real_name"));
        user.setGender(resultSet.getString("gender"));
        user.setPhone(resultSet.getString("phone"));
        user.setEmail(resultSet.getString("email"));
        user.setUserType(resultSet.getString("user_type"));
        user.setStatus(resultSet.getString("status"));
        user.setMaxBorrowCount(resultSet.getInt("max_borrow_count"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return user;
    }
}
