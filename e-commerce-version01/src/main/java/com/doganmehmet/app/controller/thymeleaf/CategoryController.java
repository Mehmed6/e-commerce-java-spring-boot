package com.doganmehmet.app.controller.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.service.thymeleaf.CategoryService;
import com.doganmehmet.app.service.SecurityControl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller(ThymeleafBeanName.THYMELEAF_CATEGORY_CONTROLLER)
@RequestMapping
public class CategoryController {

    private final CategoryService m_categoryService;
    private final SecurityControl m_securityControl;

    public CategoryController(@Qualifier(ThymeleafBeanName.THYMELEAF_CATEGORY_SERVICE) CategoryService categoryService, SecurityControl securityControl)
    {
        m_categoryService = categoryService;
        m_securityControl = securityControl;
    }

    @GetMapping("/admin/category/save/form")
    public String showCategorySaveForm()
    {
        return "category/saveCategory";
    }

    @PostMapping("/admin/category/save/form")
    public String saveCategory(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               Model model)
    {
        try {
            if(!m_categoryService.saveCategory(username, password, name)) {
                model.addAttribute("errorMessage", "Only admin can save Category");
                return "category/saveCategory";
            }
        }
        catch (ApiException ignored) {
            model.addAttribute("errorMessage", "Password Incorrect");
            return "category/saveCategory";
        }


        model.addAttribute("message", "Category saved successfully");
        model.addAttribute("categories", m_categoryService.findAll());

        return "category/categoryList";
    }

    @GetMapping("/public/show/all/categories")
    public String showCategories(Model model)
    {
        model.addAttribute("categories", m_categoryService.findAll());
        return "category/categoryList";
    }

    @GetMapping("/admin/category/delete/form")
    private String showDeleteForm()
    {
        return "category/deleteCategory";
    }

    @PostMapping("/admin/category/delete/form")
    public String deleteCategory(@RequestParam String category,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 Model model)
    {
        try {
            if (!m_securityControl.isAdmin(username, password)) {
                model.addAttribute("errorMessage", "Only admins can delete categories!");
                return "category/deleteCategory";
            }
        }
        catch (AuthenticationException e) {
            model.addAttribute("errorMessage", "Incorrect Username or Password!");
            return "category/deleteCategory";
        }

        try {
            long categoryId = Long.parseLong(category);
            model.addAttribute("message", m_categoryService.deleteCategoryById(categoryId));
        } catch (NumberFormatException e) {
            model.addAttribute("message", m_categoryService.deleteCategoryByName(category));
        }

        return "category/deleteCategory";
    }
}
