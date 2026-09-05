package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record ArticleDTO(Long id,
                    @NotBlank(message = "Must inform the name")
                    String name,
                    @NotNull(message = "Must inform the price")
                    @PositiveOrZero(message = "The price must be a positive value")
                    Double price,
                    @NotNull(message = "Must inform the stock")
                    @PositiveOrZero(message = "The value must be positive or zero")
                    Integer stockQuantity,
                    String imgUrl
                      ) {

    public static ArticleDTO fromArticle(Article article){
      return new ArticleDTO(article.getId(), article.getName(),article.getPrice(), article.getStockQuantity(), article.getImgUrl());
    }
}
