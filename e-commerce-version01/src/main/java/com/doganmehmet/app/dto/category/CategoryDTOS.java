package com.doganmehmet.app.dto.category;

import com.doganmehmet.app.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDTOS {
    private List<Category> categories;
}
