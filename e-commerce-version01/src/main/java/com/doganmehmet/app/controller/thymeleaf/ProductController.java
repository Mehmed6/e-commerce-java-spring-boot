package com.doganmehmet.app.controller.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.dto.product.ProductSaveDTO;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.services.thymeleaf.ProductService;
import com.doganmehmet.app.services.SecurityControl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller(ThymeleafBeanName.THYMELEAF_PRODUCT_CONTROLLER)
@RequestMapping("/admin")
public class ProductController {

    private final ProductService m_productService;
    private final SecurityControl m_securityControl;

    public ProductController(ProductService productService, SecurityControl securityControl)
    {
        m_productService = productService;
        m_securityControl = securityControl;
    }

    @GetMapping("/product/save/form")
    public String showSaveForm(Model model)
    {
        return "product/saveProduct";
    }

    @PostMapping("/product/save/form")
    public String saveProduct(@RequestParam String username,
                              @RequestParam String password,
                              @ModelAttribute ProductSaveDTO productSaveDTO,
                              Model model)
    {
        if (productSaveDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("errorMessage", "Price must be greater than 0");
            return "product/saveProduct";
        }

        if (productSaveDTO.getStock() <= 0) {
            model.addAttribute("errorMessage", "Stock must be greater than 0");
            return "product/saveProduct";
        }

        try {
            if (!m_productService.saveProduct(username, password, productSaveDTO)) {
                model.addAttribute("errorMessage", "Only admins can save products");
                return "product/saveProduct";
            }
        }
        catch (AuthenticationException e) {
            model.addAttribute("errorMessage", "Invalid password");
            return "product/saveProduct";
        }
        catch (ApiException ex) {
            model.addAttribute("errorMessage", "Invalid category");
            return "product/saveProduct";
        }

        model.addAttribute("message", "Product saved successfully");
        model.addAttribute("products", m_productService.findAll());

        return "product/productList";
    }

    @GetMapping("/show/all/products")
    public String showProducts(Model model)
    {
        model.addAttribute("products", m_productService.findAll());
        return "product/productList";
    }

    @GetMapping("/product/delete/form")
    public String showDeleteForm()
    {
        return "product/deleteProduct";
    }

    @PostMapping("product/delete/form")
    public String deleteProduct(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam Long productId,
                                Model model)
    {
        try {
            if (!m_securityControl.isAdmin(username, password)) {
                model.addAttribute("errorMessage", "Only admins can delete products");
                return "product/deleteProduct";
            }
        }
        catch (AuthenticationException e) {
            model.addAttribute("errorMessage", "Kullanıcı adı yada şifre yanlıs");
            return "product/deleteProduct";
        }

        if (m_productService.deleteProductById(productId))
            model.addAttribute("message", "Product deleted successfully");
        else
            model.addAttribute("errorMessage", "Product not found -> " + productId);

        return "product/deleteProduct";
    }
}
