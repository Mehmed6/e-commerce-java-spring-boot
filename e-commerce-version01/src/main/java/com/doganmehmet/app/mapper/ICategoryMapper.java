package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.category.CategoryDTOS;
import com.doganmehmet.app.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CategoryMapperImpl", componentModel = "spring")
public interface ICategoryMapper {

   default CategoryDTOS toCategoryDTOS(List<Category> categoryList)
   {
       CategoryDTOS categoryDTOS = new CategoryDTOS();
       categoryDTOS.setCategories(categoryList);

       return categoryDTOS;
   }
}
