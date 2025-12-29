package dev.ganziyan.library.ui;

import dev.ganziyan.library.entity.Book;
import dev.ganziyan.library.entity.User;
import dev.ganziyan.library.service.BookService;
import dev.ganziyan.library.service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 主界面
 * 提供图书管理、借阅管理等功能
 */
public class MainFrame extends JFrame {
    
    private User currentUser; // 当前登录用户
    private BookService bookService = new BookService();
    private BorrowService borrowService = new BorrowService();
    
    // 界面组件
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton myRecordsButton;
    private JButton allRecordsButton;
    
    /**
     * 构造方法
     * 
     * @param user 当前登录的用户
     */
    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadBookData();
        setLocationRelativeTo(null); // 窗口居中
    }
    
    /**
     * 初始化界面组件
     */
    private void initComponents() {
        // 设置窗口基本属性
        setTitle("图书借阅管理系统 - 主界面");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // 创建顶部面板（欢迎信息和搜索）
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // 创建中央面板（图书列表表格）
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // 创建底部面板（操作按钮）
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 创建顶部面板
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 248, 255)); // 淡蓝色背景
        
        // 欢迎信息
        JLabel welcomeLabel = new JLabel("欢迎您，" + currentUser.getRealName() + 
            " (" + currentUser.getUserType() + ")");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        panel.add(welcomeLabel, BorderLayout.WEST);
        
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        JLabel searchLabel = new JLabel("搜索:");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchPanel.add(searchField);
        
        searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });
        searchPanel.add(searchButton);
        
        refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.setBackground(new Color(60, 179, 113));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBookData();
            }
        });
        searchPanel.add(refreshButton);
        
        panel.add(searchPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * 创建中央面板（表格）
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // 创建表格
        String[] columnNames = {"图书ID", "书名", "作者", "出版社", "分类", "总数量", "可借数量", "价格"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        bookTable.getTableHeader().setBackground(new Color(70, 130, 180));
        bookTable.getTableHeader().setForeground(Color.BLACK);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置列宽
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建底部按钮面板
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 借书按钮
        borrowButton = new JButton("借书");
        borrowButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        borrowButton.setPreferredSize(new Dimension(120, 40));
        borrowButton.setBackground(new Color(70, 130, 180));
        borrowButton.setForeground(Color.BLACK);
        borrowButton.setFocusPainted(false);
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBorrow();
            }
        });
        panel.add(borrowButton);
        
        // 还书按钮
        returnButton = new JButton("还书");
        returnButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        returnButton.setPreferredSize(new Dimension(120, 40));
        returnButton.setBackground(new Color(220, 20, 60));
        returnButton.setForeground(Color.BLACK);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReturn();
            }
        });
        panel.add(returnButton);
        
        // 我的借阅记录按钮
        myRecordsButton = new JButton("我的借阅记录");
        myRecordsButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        myRecordsButton.setPreferredSize(new Dimension(150, 40));
        myRecordsButton.setBackground(new Color(255, 140, 0));
        myRecordsButton.setForeground(Color.BLACK);
        myRecordsButton.setFocusPainted(false);
        myRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMyRecords();
            }
        });
        panel.add(myRecordsButton);
        
        // 如果是管理员，显示所有借阅记录按钮
        if (currentUser.isAdmin()) {
            allRecordsButton = new JButton("所有借阅记录");
            allRecordsButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            allRecordsButton.setPreferredSize(new Dimension(150, 40));
            allRecordsButton.setBackground(new Color(138, 43, 226));
            allRecordsButton.setForeground(Color.BLACK);
            allRecordsButton.setFocusPainted(false);
            allRecordsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAllRecords();
                }
            });
            panel.add(allRecordsButton);
        }
        
        return panel;
    }
    
    /**
     * 加载图书数据到表格
     */
    private void loadBookData() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 查询所有图书
        List<Book> books = bookService.getAllBooks();
        
        // 填充表格数据
        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getBookName(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCategory(),
                book.getTotalQuantity(),
                book.getAvailableQuantity(),
                book.getPrice()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 处理搜索功能
     */
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        
        // 清空表格
        tableModel.setRowCount(0);
        
        // 搜索图书
        List<Book> books = bookService.searchBooks(keyword);
        
        // 填充表格数据
        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getBookName(),
                book.getAuthor(),
                book.getPublisher(),
                book.getCategory(),
                book.getTotalQuantity(),
                book.getAvailableQuantity(),
                book.getPrice()
            };
            tableModel.addRow(row);
        }
        
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "未找到符合条件的图书！", 
                "搜索结果", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * 处理借书操作
     * 核心业务逻辑：
     * 1. 检查是否选中了图书
     * 2. 获取选中图书的ID
     * 3. 弹出对话框让用户输入借阅天数
     * 4. 调用BorrowService执行借书逻辑
     * 5. 显示操作结果并刷新表格
     */
    private void handleBorrow() {
        // 1. 检查是否选中了图书
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择要借阅的图书！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 2. 获取选中图书的ID和信息
        Integer bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String bookName = (String) tableModel.getValueAt(selectedRow, 1);
        Integer availableQuantity = (Integer) tableModel.getValueAt(selectedRow, 6);
        
        // 检查库存
        if (availableQuantity <= 0) {
            JOptionPane.showMessageDialog(this, 
                "该图书暂无库存，无法借阅！", 
                "库存不足", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. 弹出对话框输入借阅天数
        String daysStr = JOptionPane.showInputDialog(this, 
            "请输入借阅天数（建议30天）:", 
            "30");
        
        if (daysStr == null) {
            return; // 用户取消
        }
        
        int borrowDays;
        try {
            borrowDays = Integer.parseInt(daysStr);
            if (borrowDays <= 0 || borrowDays > 365) {
                JOptionPane.showMessageDialog(this, 
                    "借阅天数必须在1-365天之间！", 
                    "输入错误", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "请输入有效的数字！", 
                "输入错误", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 4. 调用服务层执行借书操作
        String errorMessage = borrowService.borrowBook(currentUser.getUserId(), bookId, borrowDays);
        
        // 5. 显示操作结果
        if (errorMessage == null) {
            JOptionPane.showMessageDialog(this, 
                "借书成功！\n图书名称：" + bookName + "\n借阅天数：" + borrowDays + "天", 
                "成功", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // 刷新表格
            loadBookData();
        } else {
            JOptionPane.showMessageDialog(this, 
                errorMessage, 
                "借书失败", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 处理还书操作
     */
    private void handleReturn() {
        // 检查是否选中了图书
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择要归还的图书！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 获取选中图书的ID和信息
        Integer bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String bookName = (String) tableModel.getValueAt(selectedRow, 1);
        
        // 确认归还
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确认归还图书：" + bookName + "？", 
            "确认归还", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // 调用服务层执行还书操作
        String errorMessage = borrowService.returnBook(currentUser.getUserId(), bookId);
        
        // 显示操作结果
        if (errorMessage == null) {
            JOptionPane.showMessageDialog(this, 
                "还书成功！", 
                "成功", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // 刷新表格
            loadBookData();
        } else {
            JOptionPane.showMessageDialog(this, 
                errorMessage, 
                "还书失败", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 显示当前用户的借阅记录
     */
    private void showMyRecords() {
        BorrowRecordDialog dialog = new BorrowRecordDialog(this, currentUser.getUserId(), false);
        dialog.setVisible(true);
    }
    
    /**
     * 显示所有用户的借阅记录（仅管理员可见）
     */
    private void showAllRecords() {
        BorrowRecordDialog dialog = new BorrowRecordDialog(this, null, true);
        dialog.setVisible(true);
    }
}
