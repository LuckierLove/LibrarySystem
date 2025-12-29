package dev.ganziyan.library.ui;

import dev.ganziyan.library.entity.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 图书表单对话框
 * 用于添加和编辑图书信息
 */
public class BookFormDialog extends JDialog {
    
    private Book book; // 当前编辑的图书，如果为null则是新增
    private boolean confirmed = false; // 用户是否确认提交
    
    // 表单字段
    private JTextField bookNameField;
    private JTextField authorField;
    private JTextField publisherField;
    private JTextField isbnField;
    private JTextField categoryField;
    private JTextField totalQuantityField;
    private JTextField availableQuantityField;
    private JTextField publishDateField;
    private JTextField priceField;
    private JTextArea descriptionArea;
    
    /**
     * 构造方法 - 新增图书
     */
    public BookFormDialog(JFrame parent) {
        this(parent, null);
    }
    
    /**
     * 构造方法 - 编辑图书
     */
    public BookFormDialog(JFrame parent, Book book) {
        super(parent, book == null ? "添加图书" : "编辑图书", true);
        this.book = book;
        initComponents();
        if (book != null) {
            fillFormData();
        }
        setLocationRelativeTo(parent);
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        setSize(500, 600);
        setLayout(new BorderLayout(10, 10));
        
        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 书名
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("书名:*"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        bookNameField = new JTextField(20);
        formPanel.add(bookNameField, gbc);
        
        // 作者
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("作者:*"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        authorField = new JTextField(20);
        formPanel.add(authorField, gbc);
        
        // 出版社
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("出版社:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        publisherField = new JTextField(20);
        formPanel.add(publisherField, gbc);
        
        // ISBN
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("ISBN:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        isbnField = new JTextField(20);
        formPanel.add(isbnField, gbc);
        
        // 分类
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("分类:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        categoryField = new JTextField(20);
        formPanel.add(categoryField, gbc);
        
        // 总数量
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("总数量:*"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        totalQuantityField = new JTextField(20);
        formPanel.add(totalQuantityField, gbc);
        
        // 可借数量
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("可借数量:*"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        availableQuantityField = new JTextField(20);
        formPanel.add(availableQuantityField, gbc);
        
        // 出版日期
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("出版日期(yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        publishDateField = new JTextField(20);
        formPanel.add(publishDateField, gbc);
        
        // 价格
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("价格:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);
        
        // 简介
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("简介:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JButton confirmButton = new JButton("确认");
        confirmButton.setPreferredSize(new Dimension(100, 35));
        confirmButton.setBackground(new Color(70, 130, 180));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateAndSave()) {
                    confirmed = true;
                    dispose();
                }
            }
        });
        buttonPanel.add(confirmButton);
        
        JButton cancelButton = new JButton("取消");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(169, 169, 169));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 填充表单数据（编辑模式）
     */
    private void fillFormData() {
        bookNameField.setText(book.getBookName());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        isbnField.setText(book.getIsbn());
        categoryField.setText(book.getCategory());
        totalQuantityField.setText(String.valueOf(book.getTotalQuantity()));
        availableQuantityField.setText(String.valueOf(book.getAvailableQuantity()));
        
        if (book.getPublishDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            publishDateField.setText(sdf.format(book.getPublishDate()));
        }
        
        if (book.getPrice() != null) {
            priceField.setText(book.getPrice().toString());
        }
        
        descriptionArea.setText(book.getDescription());
    }
    
    /**
     * 验证并保存数据
     */
    private boolean validateAndSave() {
        // 验证必填项
        String bookName = bookNameField.getText().trim();
        if (bookName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入书名！", "验证错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String author = authorField.getText().trim();
        if (author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入作者！", "验证错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证数量
        int totalQuantity;
        try {
            totalQuantity = Integer.parseInt(totalQuantityField.getText().trim());
            if (totalQuantity < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "总数量必须是非负整数！", "验证错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        int availableQuantity;
        try {
            availableQuantity = Integer.parseInt(availableQuantityField.getText().trim());
            if (availableQuantity < 0 || availableQuantity > totalQuantity) {
                JOptionPane.showMessageDialog(this, "可借数量必须在0到总数量之间！", "验证错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "可借数量必须是非负整数！", "验证错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证日期
        Date publishDate = null;
        String dateStr = publishDateField.getText().trim();
        if (!dateStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(dateStr);
                publishDate = new Date(utilDate.getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "出版日期格式错误，请使用yyyy-MM-dd格式！", "验证错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // 验证价格
        BigDecimal price = null;
        String priceStr = priceField.getText().trim();
        if (!priceStr.isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "价格必须是非负数字！", "验证错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // 保存数据到book对象
        if (book == null) {
            book = new Book();
        }
        
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setPublisher(publisherField.getText().trim());
        book.setIsbn(isbnField.getText().trim());
        book.setCategory(categoryField.getText().trim());
        book.setTotalQuantity(totalQuantity);
        book.setAvailableQuantity(availableQuantity);
        book.setPublishDate(publishDate);
        book.setPrice(price);
        book.setDescription(descriptionArea.getText().trim());
        
        return true;
    }
    
    /**
     * 获取图书对象
     */
    public Book getBook() {
        return confirmed ? book : null;
    }
    
    /**
     * 是否确认提交
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
