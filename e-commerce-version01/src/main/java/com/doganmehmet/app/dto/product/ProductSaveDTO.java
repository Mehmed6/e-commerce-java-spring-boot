package com.doganmehmet.app.dto.product;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSaveDTO {

    @NotBlank(message = "Name cannot be empty or null!")
    private String name;

    @NotBlank(message = "Description cannot be empty or null!")
    private String description;

    @NotNull(message = "Price cannot be null!")
    @Positive(message = "Price must be greater than zero!")
    private BigDecimal price;

    @Positive(message = "Stock must be greater than zero!")
    private int stock;

    @NotBlank(message = "Category name cannot be empty!")
    private String categoryName;}
