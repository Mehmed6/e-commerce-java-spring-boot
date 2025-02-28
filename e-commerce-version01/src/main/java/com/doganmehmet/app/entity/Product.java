package com.doganmehmet.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long productId;

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private LocalDateTime created_at = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems;

    @Override
    public boolean equals(Object other)
    {
        return other instanceof Product product &&
                Objects.equals(category, product.category) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description);

    }

    @Override
    public int hashCode()
    {
        return Objects.hash(productId, name, description, price, stock, created_at, category);
    }
}
