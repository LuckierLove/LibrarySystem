package dev.ganziyan.library.ui;

import dev.ganziyan.library.entity.User;
import dev.ganziyan.library.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 用户管理对话框
 * 提供用户的查看和状态管理功能（管理员专用）
 */
public class UserManagementDialog extends JDialog {
    
    private UserService userService = new UserService();
    
    // 界面组件
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton enableButton;
    private JButton disableButton;
    private JButton closeButton;
    
    /**
     * 构造方法
     */
    public UserManagementDialog(JFrame parent) {
        super(parent, "用户管理", true);
        initComponents();
        loadUserData();
        setLocationRelativeTo(parent);
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));
        
        // 顶部标题面板
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // 中央表格面板
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // 底部按钮面板
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建顶部标题面板
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(70, 130, 180));
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("用户管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        return panel;
    }
    
    /**
     * 创建中央表格面板
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建表格
        String[] columnNames = {"用户ID", "用户名", "真实姓名", "性别", "电话", 
                                "邮箱", "用户类型", "账号状态", "最大借书数", "注册时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(new Color(70, 130, 180));
        userTable.getTableHeader().setForeground(Color.BLACK);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置列宽
        userTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        userTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        userTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(9).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建底部按钮面板
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        // 刷新按钮
        refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        refreshButton.setPreferredSize(new Dimension(130, 40));
        refreshButton.setBackground(new Color(60, 179, 113));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserData();
            }
        });
        panel.add(refreshButton);
        
        // 启用账号按钮
        enableButton = new JButton("启用账号");
        enableButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        enableButton.setPreferredSize(new Dimension(130, 40));
        enableButton.setBackground(new Color(70, 130, 180));
        enableButton.setForeground(Color.BLACK);
        enableButton.setFocusPainted(false);
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEnableUser();
            }
        });
        panel.add(enableButton);
        
        // 禁用账号按钮
        disableButton = new JButton("禁用账号");
        disableButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        disableButton.setPreferredSize(new Dimension(130, 40));
        disableButton.setBackground(new Color(220, 20, 60));
        disableButton.setForeground(Color.BLACK);
        disableButton.setFocusPainted(false);
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDisableUser();
            }
        });
        panel.add(disableButton);
        
        // 关闭按钮
        closeButton = new JButton("关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        closeButton.setPreferredSize(new Dimension(130, 40));
        closeButton.setBackground(new Color(169, 169, 169));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(closeButton);
        
        return panel;
    }
    
    /**
     * 加载用户数据
     */
    private void loadUserData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 查询所有用户
        List<User> users = userService.getAllUsers();
        
        // 填充表格数据
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getRealName(),
                user.getGender(),
                user.getPhone(),
                user.getEmail(),
                user.getUserType(),
                user.getStatus(),
                user.getMaxBorrowCount(),
                user.getCreatedAt()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 处理启用用户账号
     */
    private void handleEnableUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择要启用的用户账号！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 获取选中用户的信息
        Integer userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String realName = (String) tableModel.getValueAt(selectedRow, 2);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 7);
        
        // 检查当前状态
        if ("正常".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "该账号已经是正常状态，无需启用！", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 确认启用
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确认启用账号：\n用户名：" + username + "\n姓名：" + realName + "？", 
            "确认启用", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // 更新用户状态
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setStatus("正常");
            boolean success = userService.updateUser(user);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "启用账号成功！", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "启用账号失败！", 
                    "失败", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 处理禁用用户账号
     */
    private void handleDisableUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择要禁用的用户账号！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 获取选中用户的信息
        Integer userId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String realName = (String) tableModel.getValueAt(selectedRow, 2);
        String userType = (String) tableModel.getValueAt(selectedRow, 6);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 7);
        
        // 不允许禁用管理员账号
        if ("管理员".equals(userType)) {
            JOptionPane.showMessageDialog(this, 
                "不能禁用管理员账号！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 检查当前状态
        if ("禁用".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "该账号已经是禁用状态！", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 确认禁用
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确认禁用账号：\n用户名：" + username + "\n姓名：" + realName + "？\n禁用后该用户将无法登录系统。", 
            "确认禁用", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // 更新用户状态
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setStatus("禁用");
            boolean success = userService.updateUser(user);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "禁用账号成功！", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "禁用账号失败！", 
                    "失败", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
