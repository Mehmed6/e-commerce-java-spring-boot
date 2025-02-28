package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.Order;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findAllByStatus(Status status);
}
