package com.radixlogos.littlebookstore.entities;

import com.radixlogos.littlebookstore.entities.enums.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_buy_order")
public class BuyOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private Double total;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDate orderDate;
    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "buyOrder")
    private Set<OrderBook> orderBooks = new HashSet<>();

    public BuyOrder(Client client, PaymentType paymentType, Double total) {
        this.client = client;
        this.paymentType = paymentType;
        this.total = total;
    }

    public BuyOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Set<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    public void addOrderBooks(OrderBook orderBooks) {
        this.orderBooks.add(orderBooks);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BuyOrder order = (BuyOrder) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
