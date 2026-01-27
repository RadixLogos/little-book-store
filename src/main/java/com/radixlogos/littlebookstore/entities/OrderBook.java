package com.radixlogos.littlebookstore.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_order_book")
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Double soldValue;
    private Double subTotal;
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    private BuyOrder buyOrder;
    public OrderBook() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSoldValue() {
        return soldValue;
    }

    public void setSoldValue(Double soldValue) {
        this.soldValue = soldValue;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
