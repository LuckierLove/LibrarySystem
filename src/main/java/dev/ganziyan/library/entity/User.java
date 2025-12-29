package dev.ganziyan.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 用户实体类
 * 对应数据库表：users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    /**
     * 用户ID（主键）
     */
    private Integer userId;
    
    /**
     * 用户名（登录账号）
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 性别
     */
    private String gender;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 电子邮箱
     */
    private String email;
    
    /**
     * 用户类型（管理员/读者）
     */
    private String userType;
    
    /**
     * 账号状态（正常/禁用）
     */
    private String status;
    
    /**
     * 最大可借数量
     */
    private Integer maxBorrowCount;
    
    /**
     * 注册时间
     */
    private Timestamp createdAt;
    
    /**
     * 更新时间
     */
    private Timestamp updatedAt;
    
    /**
     * 构造方法：用于新用户注册时使用
     */
    public User(String username, String password, String realName, 
                String gender, String phone, String email, String userType) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
        this.status = "正常"; // 默认状态为正常
        this.maxBorrowCount = 5; // 默认最大借书数量为5本
    }
    
    /**
     * 判断是否为管理员
     * 
     * @return true表示管理员，false表示普通读者
     */
    public boolean isAdmin() {
        return "管理员".equals(this.userType);
    }
    
    /**
     * 判断账号是否正常
     * 
     * @return true表示账号正常，false表示账号被禁用
     */
    public boolean isActive() {
        return "正常".equals(this.status);
    }
}
