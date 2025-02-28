package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.Address;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.entity.UserAddress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IUserAddressRepository extends JpaRepository<UserAddress, Long> {

    boolean existsByUserAndAddress(User user, Address address);

    @Transactional
    void deleteByAddress(Address address);

    UserAddress findByAddress(Address address);

    List<UserAddress> findByUser(User user);
}
