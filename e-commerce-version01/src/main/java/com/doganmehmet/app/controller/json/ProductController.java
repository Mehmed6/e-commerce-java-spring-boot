package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.product.ProductDTO;
import com.doganmehmet.app.dto.product.ProductDTOS;
import com.doganmehmet.app.dto.product.ProductSaveDTO;
import com.doganmehmet.app.services.json.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(JSONBeanName.JSON_PRODUCT_CONTROLLER)
@RequestMapping("json/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {
    private final ProductService m_productService;

    public ProductController(@Qualifier(JSONBeanName.JSON_PRODUCT_SERVICE) ProductService productService)
    {
        m_productService = productService;
    }

    @PostMapping("/product/save")
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
    public ProductDTO updateById(@RequestParam Long id,
                                 @Valid @RequestBody ProductSaveDTO productSaveDTO)
    {
        return m_productService.updateById(id, productSaveDTO);
    }

    //DAHA SONRA ELE ALINACAK
//    @PutMapping("/product/update/name")
//    public ProductSaveDTO updateByName(@RequestParam("name") String productName,
//                                       @RequestBody ProductSaveDTO productSaveDTO)
//    {
//        return m_productService.updateByName(productName, productSaveDTO);
//    }

    @DeleteMapping("/product/delete/id")
    public String deleteById(@RequestParam Long id)
    {
        return m_productService.deleteById(id);
    }

    //DAHA SONRA ELE ALINACAK
//    @DeleteMapping("/product/delete/name")
//    public String deleteByName(@RequestParam("name") String productName)
//    {
//        return m_productService.deleteByName(productName);
//    }

    @DeleteMapping("/product/delete/all")
    public void deleteAll()
    {
        m_productService.deleteAll();
    }
}
