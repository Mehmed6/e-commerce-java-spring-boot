package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.product.ProductDTO;
import com.doganmehmet.app.dto.product.ProductDTOS;
import com.doganmehmet.app.dto.product.ProductSaveDTO;
import com.doganmehmet.app.service.json.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController(JSONBeanName.JSON_PRODUCT_CONTROLLER)
@RequestMapping("json/api/")
public class ProductController {
    private final ProductService m_productService;

    public ProductController(@Qualifier(JSONBeanName.JSON_PRODUCT_SERVICE) ProductService productService)
    {
        m_productService = productService;
    }

    @PostMapping("/product/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO saveProduct(@Valid @RequestBody ProductSaveDTO productSaveDTO)
    {
        return m_productService.saveProduct(productSaveDTO);
    }

    @GetMapping("/product/find/id")
    public ProductDTO findById(@RequestParam Long id)
    {
        return m_productService.findProductById(id);
    }

    @GetMapping("/product/find/name")
    public ProductDTOS findByName(@RequestParam("name") String productName)
    {
        return m_productService.findProductByName(productName);
    }

    @GetMapping("/product/find/all")
    public ProductDTOS findAll()
    {
        return m_productService.findAll();
    }

    @PutMapping("/product/update/id")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO updateById(@RequestParam Long id,
                                 @Valid @RequestBody ProductSaveDTO productSaveDTO)
    {
        return m_productService.updateById(id, productSaveDTO);
    }

    @DeleteMapping("/product/delete/id")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteById(@RequestParam Long id)
    {
        return m_productService.deleteById(id);
    }

    @DeleteMapping("/product/delete/all")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAll()
    {
        m_productService.deleteAll();
    }
}
