package com.doganmehmet.app.mapper;

import com.doganmehmet.app.entity.Category;
import com.doganmehmet.app.service.json.CategoryService;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class MapperHelper {
    private final CategoryService m_categoryService;

    public MapperHelper(CategoryService categoryService)
    {
        m_categoryService = categoryService;
    }

    @Named("findCategoryForMapper")
    public Category findCategoryForMapper(String categoryName)
    {
        return m_categoryService.findCategory(categoryName);
    }
}
