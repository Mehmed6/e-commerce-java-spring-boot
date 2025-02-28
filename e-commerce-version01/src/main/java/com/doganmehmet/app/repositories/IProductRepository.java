package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.Category;
import com.doganmehmet.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name); //for json

    @Query("select p from Product p where p.name = :name and p.category.name = :categoryName and p.description = :description")
    Optional<Product> findProductByNameAndCategory(String name, String categoryName, String description);


}
