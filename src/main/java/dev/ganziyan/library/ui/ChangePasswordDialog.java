package dev.ganziyan.library.ui;

import dev.ganziyan.library.entity.User;
import dev.ganziyan.library.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 修改密码对话框
 * 允许用户修改自己的密码
 */
public class ChangePasswordDialog extends JDialog {
    
    private UserService userService = new UserService();
    private User currentUser;
    
    // 界面组件
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton confirmButton;
    private JButton cancelButton;
    
    /**
     * 构造方法
     * 
     * @param parent 父窗口
     * @param user 当前登录用户
     */
    public ChangePasswordDialog(Frame parent, User user) {
        super(parent, "修改密码", true);
        this.currentUser = user;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        setSize(500, 400);
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
        panel.setBackground(new Color(70, 130, 180));
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("修改密码");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
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
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 旧密码标签和输入框
        JLabel oldPasswordLabel = new JLabel("旧密码:");
        oldPasswordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel.add(oldPasswordLabel, gbc);
        
        oldPasswordField = new JPasswordField(15);
        oldPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        oldPasswordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        panel.add(oldPasswordField, gbc);
        
        // 新密码标签和输入框
        JLabel newPasswordLabel = new JLabel("新密码:");
        newPasswordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panel.add(newPasswordLabel, gbc);
        
        newPasswordField = new JPasswordField(15);
        newPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        newPasswordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        panel.add(newPasswordField, gbc);
        
        // 确认新密码标签和输入框
        JLabel confirmPasswordLabel = new JLabel("确认新密码:");
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
        
        // 添加提示信息
        JLabel tipLabel = new JLabel("<html>密码长度至少为6位</html>");
        tipLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tipLabel.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        panel.add(tipLabel, gbc);
        
        return panel;
    }
    
    /**
     * 创建按钮面板
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // 确认按钮
        confirmButton = new JButton("确认修改");
        confirmButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        confirmButton.setPreferredSize(new Dimension(130, 40));
        confirmButton.setBackground(new Color(70, 130, 180));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangePassword();
            }
        });
        panel.add(confirmButton);
        
        // 取消按钮
        cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(130, 40));
        cancelButton.setBackground(new Color(169, 169, 169));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(cancelButton);
        
        return panel;
    }
    
    /**
     * 处理修改密码
     */
    private void handleChangePassword() {
        // 1. 获取用户输入
        String oldPassword = new String(oldPasswordField.getPassword()).trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        
        // 2. 验证输入
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "所有字段都不能为空！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. 验证旧密码是否正确
        if (!oldPassword.equals(currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this, 
                "旧密码不正确！", 
                "验证失败", 
                JOptionPane.ERROR_MESSAGE);
            oldPasswordField.setText("");
            oldPasswordField.requestFocus();
            return;
        }
        
        // 4. 验证新密码长度
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "新密码长度至少为6位！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 5. 验证两次输入的新密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "两次输入的新密码不一致！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            newPasswordField.setText("");
            confirmPasswordField.setText("");
            newPasswordField.requestFocus();
            return;
        }
        
        // 6. 验证新密码不能与旧密码相同
        if (newPassword.equals(oldPassword)) {
            JOptionPane.showMessageDialog(this, 
                "新密码不能与旧密码相同！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            newPasswordField.setText("");
            confirmPasswordField.setText("");
            newPasswordField.requestFocus();
            return;
        }
        
        // 7. 更新密码
        currentUser.setPassword(newPassword);
        boolean success = userService.updateUser(currentUser);
        
        // 8. 处理更新结果
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "密码修改成功！请使用新密码登录。", 
                "成功", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "密码修改失败，请重试！", 
                "失败", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
