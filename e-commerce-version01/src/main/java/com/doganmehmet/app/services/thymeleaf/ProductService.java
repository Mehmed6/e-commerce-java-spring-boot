package com.doganmehmet.app.services.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.dto.product.ProductSaveDTO;
import com.doganmehmet.app.entity.Product;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repositories.ICategoryRepository;
import com.doganmehmet.app.repositories.IProductRepository;
import com.doganmehmet.app.services.SecurityControl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(ThymeleafBeanName.THYMELEAF_PRODUCT_SERVICE)
public class ProductService {

    private final IProductRepository m_productRepository;
    private final SecurityControl m_securityControl;
    private final ICategoryRepository m_categoryRepository;

    public ProductService(IProductRepository productRepository, SecurityControl securityControl, ICategoryRepository categoryRepository)
    {
        m_productRepository = productRepository;
        m_securityControl = securityControl;
        m_categoryRepository = categoryRepository;
    }

    private Product creatProduct(ProductSaveDTO productSaveDTO)
    {
        Product product = new Product();
        product.setName(productSaveDTO.getName());
        product.setDescription(productSaveDTO.getDescription());
        product.setPrice(productSaveDTO.getPrice());
        product.setStock(productSaveDTO.getStock());

        var category = m_categoryRepository.findCategoryByName(productSaveDTO.getCategoryName());
        product.setCategory(category.orElseThrow(() -> new ApiException(MyError.INVALID_CATEGORY)));

        return product;

    }

    public boolean saveProduct(String username, String password, ProductSaveDTO productSaveDTO)
    {
        if (m_securityControl.isAdmin(username, password)) {
            m_productRepository.save(creatProduct(productSaveDTO));
            return true;
        }
        return false;
    }

    public List<Product> findAll()
    {
        return m_productRepository.findAll();
    }

    public boolean deleteProductById(Long id)
    {
        var product = m_productRepository.findById(id);

        if (product.isPresent()) {
            m_productRepository.delete(product.get());
            return true;
        }

        return false;
    }

    //DAHA SONRA BAKILACAK
//    public String deleteProductByName(String productName)
//    {
//        var product = m_productRepository.findByName(productName);
//
//        if (product.isPresent()) {
//            m_productRepository.delete(product.get());
//            return "Product deleted";
//        }
//        return "Product not found -> " + productName;
//    }

}
