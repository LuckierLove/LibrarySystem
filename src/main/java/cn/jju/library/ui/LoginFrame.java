package cn.jju.library.ui;

import cn.jju.library.entity.User;
import cn.jju.library.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录界面
 * 提供用户登录功能
 * 
 * @author 图书管理系统开发组
 * @version 1.0.0
 */
public class LoginFrame extends JFrame {
    
    private UserService userService = new UserService();
    
    // 界面组件
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    
    /**
     * 构造方法：初始化登录界面
     */
    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null); // 窗口居中显示
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 设置窗口基本属性
        setTitle("图书借阅管理系统 - 登录");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        
        JLabel titleLabel = new JLabel("图书借阅管理系统");
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
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        panel.add(loginButton);
        
        // 重置按钮
        resetButton = new JButton("重置");
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        resetButton.setPreferredSize(new Dimension(120, 40));
        resetButton.setBackground(new Color(169, 169, 169));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReset();
            }
        });
        panel.add(resetButton);
        
        return panel;
    }
    
    /**
     * 处理登录按钮点击事件
     * 核心业务逻辑：
     * 1. 获取用户输入的用户名和密码
     * 2. 验证输入是否为空
     * 3. 调用UserService进行登录验证
     * 4. 登录成功则打开主界面，失败则提示错误信息
     */
    private void handleLogin() {
        // 1. 获取用户输入
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // 2. 验证输入
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "用户名和密码不能为空！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. 调用服务层进行登录验证
        User user = userService.login(username, password);
        
        // 4. 处理登录结果
        if (user != null) {
            // 登录成功
            JOptionPane.showMessageDialog(this, 
                "欢迎您，" + user.getRealName() + "！", 
                "登录成功", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // 打开主界面
            openMainFrame(user);
            
            // 关闭登录界面
            this.dispose();
        } else {
            // 登录失败
            JOptionPane.showMessageDialog(this, 
                "用户名或密码错误，请重新输入！", 
                "登录失败", 
                JOptionPane.ERROR_MESSAGE);
            
            // 清空密码框
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    /**
     * 处理重置按钮点击事件
     * 清空输入框内容
     */
    private void handleReset() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
    
    /**
     * 打开主界面
     * 
     * @param user 登录的用户对象
     */
    private void openMainFrame(User user) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
            }
        });
    }
    
    /**
     * 主方法：程序入口
     */
    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 启动登录界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
