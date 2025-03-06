package com.doganmehmet.app.repository;

import com.doganmehmet.app.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
    Address findByAddressId(long addressId);
}
