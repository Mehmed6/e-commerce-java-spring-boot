package com.doganmehmet.app.services.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.entity.Category;
import com.doganmehmet.app.mapper.ICategoryMapper;
import com.doganmehmet.app.repositories.ICategoryRepository;
import com.doganmehmet.app.services.SecurityControl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(ThymeleafBeanName.THYMELEAF_CATEGORY_SERVICE)
public class CategoryService {
    private final ICategoryRepository m_categoryRepository;
    private final ICategoryMapper m_categoryMapper;
    private final SecurityControl m_securityControl;

    public CategoryService(ICategoryRepository categoryRepository, ICategoryMapper categoryMapper, SecurityControl securityControl)
    {
        m_categoryRepository = categoryRepository;
        m_categoryMapper = categoryMapper;
        m_securityControl = securityControl;
    }

    @Transactional
    public boolean saveCategory(String username, String password, String categoryName)
    {
        if(m_securityControl.isAdmin(username, password)) {
            Category category = new Category();
            category.setName(categoryName);
            m_categoryRepository.save(category);
            return true;
        }
        return false;
    }

    public List<Category> findAll()
    {
        return m_categoryRepository.findAll();
    }

    public String deleteCategoryById(long id)
    {
        var category = m_categoryRepository.findById(id);

        if(category.isPresent()) {
            m_categoryRepository.delete(category.get());
            return "Category deleted";
        }

        return "Category ID not found -> " + id;
    }

    public String deleteCategoryByName(String categoryName)
    {
        var category = m_categoryRepository.findCategoryByName(categoryName);

        if(category.isPresent()) {
            m_categoryRepository.delete(category.get());
            return "Category deleted";
        }

        return "Category Name not found -> " + categoryName;
    }
}
