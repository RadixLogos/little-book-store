package com.radixlogos.littlebookstore.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_articles")
public class Article {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer stockQuantity;
    private Double price;
    private String imgUrl;

    public Article() {
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

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Article articles = (Article) o;
        return Objects.equals(id, articles.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
