package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.product.ProductDTO;
import com.doganmehmet.app.dto.product.ProductDTOS;
import com.doganmehmet.app.dto.product.ProductSaveDTO;
import com.doganmehmet.app.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(implementationName = "ProductMapperImpl", componentModel = "spring", uses = {MapperHelper.class})
public interface IProductMapper {

    @Mapping(target = "category", source = "categoryName", qualifiedByName = "findCategoryForMapper")
    Product updateProduct(@MappingTarget Product product, ProductSaveDTO productSaveDTO);

    @Mapping(target = "category", source = "categoryName", qualifiedByName = "findCategoryForMapper")
    Product toProduct(ProductSaveDTO productSaveDTO);

    @Mapping(source = "category.name", target = "categoryName")
    ProductDTO toProductDTO(Product product);

    List<ProductDTO> toProductDTOList(List<Product> productList);

    default ProductDTOS toProductDTOS(List<Product> productList)
    {
        var dtos = new ProductDTOS();
        dtos.setProducts(toProductDTOList(productList));
        return dtos;
    }


}
