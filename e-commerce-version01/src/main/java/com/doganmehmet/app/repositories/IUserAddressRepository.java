package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.Address;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.entity.UserAddress;
import com.doganmehmet.app.enums.AddressTYPE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IUserAddressRepository extends JpaRepository<UserAddress, Long> {

    boolean existsByUserAndAddress(User user, Address address);

    void deleteByAddress(Address address);

    UserAddress findByAddress(Address address);

    List<UserAddress> findByUser(User user);
}
