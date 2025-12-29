package dev.ganziyan.library.ui;

import dev.ganziyan.library.entity.Book;
import dev.ganziyan.library.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 图书管理对话框
 * 提供图书的增删改查功能
 */
public class BookManagementDialog extends JDialog {
    
    private BookService bookService = new BookService();
    
    // 界面组件
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton closeButton;
    
    /**
     * 构造方法
     */
    public BookManagementDialog(JFrame parent) {
        super(parent, "图书管理", true);
        initComponents();
        loadBookData();
        setLocationRelativeTo(parent);
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        setSize(1000, 600);
        setLayout(new BorderLayout(10, 10));
        
        // 顶部搜索面板
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
     * 创建顶部搜索面板
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        JLabel titleLabel = new JLabel("图书管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        panel.add(titleLabel);
        
        panel.add(Box.createHorizontalStrut(50));
        
        JLabel searchLabel = new JLabel("搜索:");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(searchField);
        
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
        panel.add(searchButton);
        
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
        panel.add(refreshButton);
        
        return panel;
    }
    
    /**
     * 创建中央表格面板
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // 创建表格
        String[] columnNames = {"图书ID", "书名", "作者", "出版社", "ISBN", "分类", 
                                "总数量", "可借数量", "出版日期", "价格"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        bookTable.getTableHeader().setBackground(new Color(70, 130, 180));
        bookTable.getTableHeader().setForeground(Color.BLACK);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置列宽
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(7).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(9).setPreferredWidth(70);
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 创建底部按钮面板
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        // 添加按钮
        addButton = new JButton("添加图书");
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(130, 40));
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });
        panel.add(addButton);
        
        // 编辑按钮
        editButton = new JButton("编辑图书");
        editButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        editButton.setPreferredSize(new Dimension(130, 40));
        editButton.setBackground(new Color(255, 165, 0));
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEdit();
            }
        });
        panel.add(editButton);
        
        // 删除按钮
        deleteButton = new JButton("删除图书");
        deleteButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        deleteButton.setPreferredSize(new Dimension(130, 40));
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete();
            }
        });
        panel.add(deleteButton);
        
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
     * 加载图书数据
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
                book.getIsbn(),
                book.getCategory(),
                book.getTotalQuantity(),
                book.getAvailableQuantity(),
                book.getPublishDate(),
                book.getPrice()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * 处理搜索
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
                book.getIsbn(),
                book.getCategory(),
                book.getTotalQuantity(),
                book.getAvailableQuantity(),
                book.getPublishDate(),
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
     * 处理添加图书
     */
    private void handleAdd() {
        BookFormDialog dialog = new BookFormDialog((JFrame) getOwner());
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Book book = dialog.getBook();
            boolean success = bookService.addBook(book);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "添加图书成功！", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadBookData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "添加图书失败！", 
                    "失败", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 处理编辑图书
     */
    private void handleEdit() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择要编辑的图书！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 获取选中图书的ID
        Integer bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Book book = bookService.getBookById(bookId);
        
        if (book == null) {
            JOptionPane.showMessageDialog(this, 
                "图书不存在！", 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        BookFormDialog dialog = new BookFormDialog((JFrame) getOwner(), book);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Book updatedBook = dialog.getBook();
            boolean success = bookService.updateBook(updatedBook);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "编辑图书成功！", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadBookData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "编辑图书失败！", 
                    "失败", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 处理删除图书
     */
    private void handleDelete() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "请先选择要删除的图书！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 获取选中图书的信息
        Integer bookId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String bookName = (String) tableModel.getValueAt(selectedRow, 1);
        
        // 确认删除
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确认删除图书：" + bookName + "？\n删除后将无法恢复！", 
            "确认删除", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        boolean success = bookService.deleteBook(bookId);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "删除图书成功！", 
                "成功", 
                JOptionPane.INFORMATION_MESSAGE);
            loadBookData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "删除图书失败！可能该图书存在借阅记录。", 
                "失败", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
