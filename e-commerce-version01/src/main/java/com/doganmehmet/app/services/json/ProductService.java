package com.doganmehmet.app.services.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.product.ProductDTO;
import com.doganmehmet.app.dto.product.ProductDTOS;
import com.doganmehmet.app.dto.product.ProductSaveDTO;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.mapper.IProductMapper;
import com.doganmehmet.app.repositories.IProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(JSONBeanName.JSON_PRODUCT_SERVICE)
public class ProductService {

    private final IProductRepository m_productRepository;
    private final IProductMapper m_productMapper;

    public ProductService(IProductRepository productRepository, IProductMapper productMapper)
    {
        m_productRepository = productRepository;
        m_productMapper = productMapper;
    }

    private void checkProductIsExist(String productName, String categoryName, String description)
    {
        var existingProduct = m_productRepository.findProductByNameAndCategory(productName, categoryName, description);

        if (existingProduct.isPresent())
            throw new ApiException(MyError.PRODUCT_ALREADY_EXISTS);
    }

    public ProductDTO saveProduct(ProductSaveDTO productSaveDTO)
    {
        checkProductIsExist(productSaveDTO.getName(), productSaveDTO.getCategoryName(), productSaveDTO.getDescription());

        var product = m_productMapper.toProduct(productSaveDTO);
        return m_productMapper.toProductDTO(m_productRepository.save(product));
    }

    public ProductDTO findProductById(Long id)
    {
        return m_productMapper.toProductDTO(m_productRepository.findById(id)
                .orElseThrow(() -> new ApiException(MyError.PRODUCT_NOT_FOUND)));
    }

    public ProductDTOS findProductByName(String productName)
    {
        return m_productMapper.toProductDTOS(m_productRepository.findByName(productName));
    }

    public ProductDTOS findAll()
    {

        return m_productMapper.toProductDTOS(m_productRepository.findAll());
        //return products.stream().map(m_productMapper::toProductDTO).toList();
    }

    public ProductDTO updateById(Long id, ProductSaveDTO productSaveDTO)
    {
        var product = m_productRepository.findById(id)
                .orElseThrow(() -> new ApiException(MyError.PRODUCT_NOT_FOUND));

        checkProductIsExist(product.getName(), productSaveDTO.getCategoryName(), productSaveDTO.getDescription());

        var updatedProduct = m_productRepository.save(m_productMapper.updateProduct(product, productSaveDTO));
        return m_productMapper.toProductDTO(updatedProduct);
    }

    //DAHA SONRA ELE ALINACAK

//    public ProductSaveDTO updateByName(String productName, ProductSaveDTO productSaveDTO)
//    {
//        var product = m_productRepository.findByName(productName)
//                .orElseThrow(() -> new ApiException(MyError.PRODUCT_NOT_FOUND));
//
//        m_productRepository.save(m_productMapper.updateProduct(product, productSaveDTO));
//        return productSaveDTO;
//    }

    public String deleteById(Long id)
    {
        var product = m_productRepository.findById(id).
                orElseThrow(() -> new ApiException(MyError.PRODUCT_NOT_FOUND));

        m_productRepository.delete(product);
        return "Product deleted";

    }

    //DAHA SONRA ELE ALINACAK

//    @Transactional
//    public String deleteByName(String productName)
//    {
//        var product = m_productRepository.findByName(productName);
//
//        if (product.isPresent()) {
//            m_productRepository.delete(product.get());
//            return "Product deleted";
//        }
//
//        throw new ApiException(MyError.INVALID_PRODUCT_NAME);
//        //return "Product not found";
//
//    }

    public void deleteAll()
    {
        m_productRepository.deleteAll();
    }
}
