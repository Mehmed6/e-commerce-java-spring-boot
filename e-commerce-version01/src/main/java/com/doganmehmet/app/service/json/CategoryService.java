package com.doganmehmet.app.service.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.category.CategoryDTOS;
import com.doganmehmet.app.entity.Category;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.mapper.ICategoryMapper;
import com.doganmehmet.app.repository.ICategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service(JSONBeanName.JSON_CATEGORY_SERVICE)
public class CategoryService {
    private final ICategoryRepository m_categoryRepository;
    private final ICategoryMapper m_categoryMapper;

    public CategoryService(ICategoryRepository categoryRepository, ICategoryMapper categoryMapper)
    {
        m_categoryRepository = categoryRepository;
        m_categoryMapper = categoryMapper;
    }

    public Category saveCategory(String categoryName)
    {
        var checkCategory = m_categoryRepository.findCategoryByName(categoryName);

        if (checkCategory.isPresent())
            throw new ApiException(MyError.CATEGORY_ALREADY_EXISTS);

        Category category = new Category();
        category.setName(categoryName);
        return m_categoryRepository.save(category);
    }

    public Category findCategory(String categoryName)
    {
        return m_categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new ApiException(MyError.INVALID_CATEGORY));
    }

    public CategoryDTOS findAllDTO()
    {
        return m_categoryMapper.toCategoryDTOS(m_categoryRepository.findAll());
    }


    @Transactional
    public String deleteCategoryById(long id)
    {
        var category = m_categoryRepository.findById(id);

        if(category.isPresent()) {
            m_categoryRepository.delete(category.get());
            return "Category deleted";
        }

        throw new ApiException(MyError.INVALID_CATEGORY_ID);
    }

    @Transactional
    public String deleteCategoryByName(String categoryName)
    {
        var category = m_categoryRepository.findCategoryByName(categoryName);

        if (category.isPresent()) {
            m_categoryRepository.delete(category.get());
            return "Category deleted";
        }

        throw new ApiException(MyError.INVALID_CATEGORY_NAME);
    }
}
