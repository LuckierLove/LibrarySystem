package cn.jju.library.service;

import cn.jju.library.dao.UserDao;
import cn.jju.library.entity.User;

import java.util.List;

/**
 * 用户服务类
 * 实现用户管理和登录验证的业务逻辑
 * 
 * @author 图书管理系统开发组
 * @version 1.0.0
 */
public class UserService {
    
    private UserDao userDao = new UserDao();
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回null
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDao.login(username.trim(), password);
    }
    
    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    
    /**
     * 根据ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserById(Integer userId) {
        return userDao.findById(userId);
    }
    
    /**
     * 添加用户
     * 
     * @param user 用户对象
     * @return true表示成功，false表示失败
     */
    public boolean addUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        // 检查用户名是否已存在
        User existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false; // 用户名已存在
        }
        return userDao.add(user);
    }
    
    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return true表示成功，false表示失败
     */
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }
        return userDao.update(user);
    }
    
    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return true表示成功，false表示失败
     */
    public boolean deleteUser(Integer userId) {
        if (userId == null) {
            return false;
        }
        return userDao.delete(userId);
    }
}
