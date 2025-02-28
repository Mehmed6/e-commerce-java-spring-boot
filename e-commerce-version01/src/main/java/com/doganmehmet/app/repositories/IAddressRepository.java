package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.Address;
import com.doganmehmet.app.entity.UserAddress;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
    Address findByAddressId(long addressId);
}
