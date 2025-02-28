package com.doganmehmet.app.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class ProductDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String categoryName;
    private LocalDateTime created_at = LocalDateTime.now();

    @Override
    public boolean equals(Object other)
    {
        return other instanceof ProductDTO product &&
                Objects.equals(categoryName, product.categoryName) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description);

    }

    @Override
    public int hashCode()
    {
        return Objects.hash( name, description, price, stock, created_at, categoryName);
    }
}

