package com.radixlogos.littlebookstore.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String editor;
    private Integer stockQuantity;
    private Double price;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<OrderBook> orderBooks = new HashSet<>();
    public Book() {
    }

    public Book(String name, String editor, Integer quantity, Double value) {
        this.name = name;
        this.editor = editor;
        this.stockQuantity = quantity;
        this.price = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    public void addOrderBook(OrderBook orderBook) {
        this.orderBooks.add(orderBook);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
