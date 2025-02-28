package com.doganmehmet.app.controller.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.services.thymeleaf.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(ThymeleafBeanName.THYMELEAF_USER_CONTROLLER)
@RequestMapping("category")
public class UserController {

    private final CategoryService m_categoryService;

    public UserController(CategoryService categoryService)
    {
        m_categoryService = categoryService;
    }


}
