package com.doganmehmet.app.entity;

import com.doganmehmet.app.enums.AddressTYPE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private long addressId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Column(name = "registration_address")
    private String registrationAddress;
    @Column(name = "zip_code")
    private String zipCode;
    private String city;
    private String country;
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressTYPE addressType;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> userAddresses;
}
