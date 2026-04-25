package com.radixlogos.littlebookstore.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_order_book")
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Double soldValue;
    private Double subTotal;
    private Double pixValue;
    private Double moneyValue;

    @ManyToOne
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

    public BuyOrder getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(BuyOrder buyOrder) {
        this.buyOrder = buyOrder;
    }

    public Double getPixValue() {
        return pixValue;
    }

    public void setPixValue(Double pixValue) {
        this.pixValue = pixValue;
    }

    public Double getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Double moneyValue) {
        this.moneyValue = moneyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderBook orderBook = (OrderBook) o;
        if (id == null || orderBook.id == null) {
            return false;
        }
        return Objects.equals(id, orderBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
