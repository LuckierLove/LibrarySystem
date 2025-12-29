package dev.ganziyan.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 图书实体类
 * 对应数据库表：books
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    
    /**
     * 图书ID（主键）
     */
    private Integer bookId;
    
    /**
     * 图书名称
     */
    private String bookName;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 出版社
     */
    private String publisher;
    
    /**
     * ISBN编号
     */
    private String isbn;
    
    /**
     * 图书分类
     */
    private String category;
    
    /**
     * 图书总数量
     */
    private Integer totalQuantity;
    
    /**
     * 可借数量
     */
    private Integer availableQuantity;
    
    /**
     * 出版日期
     */
    private Date publishDate;
    
    /**
     * 图书价格
     */
    private BigDecimal price;
    
    /**
     * 图书简介
     */
    private String description;
    
    /**
     * 创建时间
     */
    private Timestamp createdAt;
    
    /**
     * 更新时间
     */
    private Timestamp updatedAt;
    
    /**
     * 构造方法：用于新增图书时使用（不包含自增ID和时间戳）
     */
    public Book(String bookName, String author, String publisher, String isbn, 
                String category, Integer totalQuantity, Integer availableQuantity, 
                Date publishDate, BigDecimal price, String description) {
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.publishDate = publishDate;
        this.price = price;
        this.description = description;
    }
    
    /**
     * 判断图书是否可借
     * 
     * @return true表示有库存可借，false表示库存不足
     */
    public boolean isAvailable() {
        return availableQuantity != null && availableQuantity > 0;
    }
    
    /**
     * 减少可借数量（借书操作）
     */
    public void decreaseAvailableQuantity() {
        if (isAvailable()) {
            this.availableQuantity--;
        }
    }
    
    /**
     * 增加可借数量（还书操作）
     */
    public void increaseAvailableQuantity() {
        if (availableQuantity < totalQuantity) {
            this.availableQuantity++;
        }
    }
}
