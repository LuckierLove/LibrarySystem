package dev.ganziyan.library.ui;

import dev.ganziyan.library.entity.User;
import dev.ganziyan.library.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 注册界面
 * 提供用户注册功能
 */
public class RegisterFrame extends JFrame {
    
    private UserService userService = new UserService();
    private LoginFrame loginFrame;
    
    // 界面组件
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField realNameField;
    private JComboBox<String> genderComboBox;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton cancelButton;
    
    /**
     * 构造方法：初始化注册界面
     * 
     * @param loginFrame 登录界面引用
     */
    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        initComponents();
        setLocationRelativeTo(null); // 窗口居中显示
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 设置窗口基本属性
        setTitle("图书借阅管理系统 - 用户注册");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // 创建标题面板
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // 创建表单面板
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建标题面板
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(70, 130, 180)); // 钢蓝色背景
        panel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        return panel;
    }
    
    /**
     * 创建表单面板
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        panel.add(usernameField, gbc);
        
        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        panel.add(passwordField, gbc);
        
        // 确认密码标签和输入框
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panel.add(confirmPasswordLabel, gbc);
        
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        panel.add(confirmPasswordField, gbc);
        
        // 真实姓名标签和输入框
        JLabel realNameLabel = new JLabel("真实姓名:");
        realNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        panel.add(realNameLabel, gbc);
        
        realNameField = new JTextField(15);
        realNameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        realNameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        panel.add(realNameField, gbc);
        
        // 性别标签和下拉框
        JLabel genderLabel = new JLabel("性别:");
        genderLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        panel.add(genderLabel, gbc);
        
        String[] genders = {"男", "女"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        genderComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.7;
        panel.add(genderComboBox, gbc);
        
        // 电话标签和输入框
        JLabel phoneLabel = new JLabel("联系电话:");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        panel.add(phoneLabel, gbc);
        
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.7;
        panel.add(phoneField, gbc);
        
        // 邮箱标签和输入框
        JLabel emailLabel = new JLabel("电子邮箱:");
        emailLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        panel.add(emailLabel, gbc);
        
        emailField = new JTextField(15);
        emailField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.7;
        panel.add(emailField, gbc);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        panel.add(registerButton);
        
        // 取消按钮
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(new Color(169, 169, 169));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * 处理注册按钮点击事件
     */
    private void handleRegister() {
        // 1. 获取用户输入
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String realName = realNameField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        
        // 2. 验证输入
        if (username.isEmpty() || password.isEmpty() || realName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "用户名、密码和真实姓名不能为空！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "两次输入的密码不一致！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "密码长度至少为6位！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. 创建用户对象（默认注册为读者）
        User newUser = new User(username, password, realName, gender, phone, email, "读者");
        
        // 4. 调用服务层进行注册
        boolean success = userService.addUser(newUser);
        
        // 5. 处理注册结果
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "注册成功！请使用您的账号登录。", 
                "注册成功", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // 返回登录界面
            handleCancel();
        } else {
            JOptionPane.showMessageDialog(this, 
                "注册失败！用户名可能已存在，请更换用户名后重试。", 
                "注册失败", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 处理取消按钮点击事件
     */
    private void handleCancel() {
        this.dispose();
        loginFrame.setVisible(true);
    }
}
