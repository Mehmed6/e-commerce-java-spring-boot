package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderItemsRepository extends JpaRepository<OrderItems, Long> {
}
