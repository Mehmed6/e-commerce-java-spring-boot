package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.category.CategoryDTOS;
import com.doganmehmet.app.entity.Category;
import com.doganmehmet.app.service.json.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController(JSONBeanName.JSON_CATEGORY_CONTROLLER)
@RequestMapping("json/api/")
public class CategoryController {

    private final CategoryService m_categoryService;

    public CategoryController(@Qualifier(JSONBeanName.JSON_CATEGORY_SERVICE) CategoryService categoryService)
    {
        m_categoryService = categoryService;
    }

    @PostMapping("/category/save")
    @PreAuthorize("hasRole('ADMIN')")
    public Category saveCategory(@RequestBody String categoryName)
    {
        return m_categoryService.saveCategory(categoryName);
    }

    @GetMapping("/category/find")
    public Category findCategoryJson(@RequestParam("name") String categoryName)
    {
        return m_categoryService.findCategory(categoryName);
    }

    @GetMapping("/category/find/all")
    public CategoryDTOS findAllDTO()
    {
        return m_categoryService.findAllDTO();
    }

    @DeleteMapping("/category/delete/id")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCategory(@RequestParam long id)
    {
        return m_categoryService.deleteCategoryById(id);
    }

    @DeleteMapping("/category/delete/name")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCategoryByName(@RequestParam String categoryName)
    {
        return m_categoryService.deleteCategoryByName(categoryName);
    }
}
