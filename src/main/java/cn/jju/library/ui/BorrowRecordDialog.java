package cn.jju.library.ui;

import cn.jju.library.entity.BorrowRecord;
import cn.jju.library.service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 借阅记录对话框
 * 用于显示用户的借阅记录
 * 
 * @author 图书管理系统开发组
 * @version 1.0.0
 */
public class BorrowRecordDialog extends JDialog {
    
    private BorrowService borrowService = new BorrowService();
    private Integer userId; // 用户ID，为null表示查询所有记录
    private boolean showAll; // 是否显示所有用户的记录
    
    private JTable recordTable;
    private DefaultTableModel tableModel;
    
    /**
     * 构造方法
     * 
     * @param parent 父窗口
     * @param userId 用户ID（null表示查询所有）
     * @param showAll 是否显示所有记录
     */
    public BorrowRecordDialog(Frame parent, Integer userId, boolean showAll) {
        super(parent, showAll ? "所有借阅记录" : "我的借阅记录", true);
        this.userId = userId;
        this.showAll = showAll;
        
        initComponents();
        loadRecordData();
        
        setSize(900, 500);
        setLocationRelativeTo(parent);
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // 创建标题面板
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setPreferredSize(new Dimension(0, 50));
        
        JLabel titleLabel = new JLabel(showAll ? "所有借阅记录" : "我的借阅记录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton closeButton = new JButton("关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建表格面板
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 定义表格列
        String[] columnNames;
        if (showAll) {
            columnNames = new String[]{"记录ID", "用户名", "真实姓名", "图书名称", "作者", 
                                       "借阅日期", "应还日期", "归还日期", "状态"};
        } else {
            columnNames = new String[]{"记录ID", "图书名称", "作者", 
                                       "借阅日期", "应还日期", "归还日期", "状态"};
        }
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recordTable = new JTable(tableModel);
        recordTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        recordTable.setRowHeight(25);
        recordTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        recordTable.getTableHeader().setBackground(new Color(70, 130, 180));
        recordTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(recordTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 加载借阅记录数据
     */
    private void loadRecordData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 查询借阅记录
        List<BorrowRecord> records;
        if (showAll) {
            records = borrowService.getAllBorrowRecords();
        } else {
            records = borrowService.getUserBorrowRecords(userId);
        }
        
        // 日期格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // 填充表格数据
        for (BorrowRecord record : records) {
            Object[] row;
            
            String borrowDate = record.getBorrowDate() != null ? 
                dateTimeFormat.format(record.getBorrowDate()) : "";
            String dueDate = record.getDueDate() != null ? 
                dateFormat.format(record.getDueDate()) : "";
            String returnDate = record.getReturnDate() != null ? 
                dateTimeFormat.format(record.getReturnDate()) : "未归还";
            
            if (showAll) {
                row = new Object[]{
                    record.getRecordId(),
                    record.getUsername(),
                    record.getRealName(),
                    record.getBookName(),
                    record.getAuthor(),
                    borrowDate,
                    dueDate,
                    returnDate,
                    record.getStatus()
                };
            } else {
                row = new Object[]{
                    record.getRecordId(),
                    record.getBookName(),
                    record.getAuthor(),
                    borrowDate,
                    dueDate,
                    returnDate,
                    record.getStatus()
                };
            }
            
            tableModel.addRow(row);
        }
        
        // 如果没有记录，显示提示
        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "暂无借阅记录！", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
