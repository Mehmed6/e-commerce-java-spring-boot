package com.doganmehmet.app.service;

import com.doganmehmet.app.dto.address.AddressDTO;
import com.doganmehmet.app.entity.Address;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.entity.UserAddress;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.mapper.IAddressMapper;
import com.doganmehmet.app.repository.IAddressRepository;
import com.doganmehmet.app.repository.IUserAddressRepository;
import com.doganmehmet.app.repository.IUserRepository;
import com.doganmehmet.app.request.AddressRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final IAddressRepository m_addressRepository;
    private final IAddressMapper m_addressMapper;
    private final SecurityControl m_securityControl;
    private final IUserRepository m_userRepository;
    private final IUserAddressRepository m_userAddressRepository;

    public AddressService(IAddressRepository addressRepository, IAddressMapper addressMapper, SecurityControl securityControl, IUserRepository userRepository, IUserAddressRepository userAddressRepository)
    {
        m_addressRepository = addressRepository;
        m_addressMapper = addressMapper;
        m_securityControl = securityControl;
        m_userRepository = userRepository;
        m_userAddressRepository = userAddressRepository;
    }

    private Optional<Address> existingAddress(AddressRequest addressRequest)
    {
        var matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("username", "addressId", "userAddresses");

        var example = Example.of(m_addressMapper.toAddress(addressRequest), matcher);
        return m_addressRepository.findOne(example);

    }

    private boolean existingUserAddress(User user, Address address)
    {
        return m_userAddressRepository.existsByUserAndAddress(user, address);
    }
    @Transactional
    public AddressDTO saveAddress(AddressRequest addressRequest)
    {
        var user = m_userRepository.findByUsername(addressRequest.getUsername())
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        m_securityControl.checkTokenUserMatch(addressRequest.getUsername());

        var address = new Address();

        if (existingAddress(addressRequest).isPresent())
            address = existingAddress(addressRequest).get();

        else
            address = m_addressRepository.save(m_addressMapper.toAddress(addressRequest));

        var userAddress = new UserAddress();
        if (existingUserAddress(user, address)) {
            throw new ApiException(MyError.ADDRESS_ALREADY_EXISTS);
        }

        userAddress.setUser(user);
        userAddress.setAddress(address);

        m_userAddressRepository.save(userAddress);

        return m_addressMapper.toAddressDTO(address);
    }

    public List<AddressDTO> findAddressByUsername(String username)
    {
        m_securityControl.checkTokenUserMatch(username);
        var user = m_userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        var userAddress = m_userAddressRepository.findByUser(user);

        var addresses = new ArrayList<Address>();
        for (var us : userAddress) {
            addresses.add(us.getAddress());
        }

        return m_addressMapper.toAddressDTOList(addresses);
    }

    @Transactional
    public AddressDTO updateAddress(AddressRequest addressRequest, Long addressId)
    {
        m_userRepository.findByUsername(addressRequest.getUsername())
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        m_securityControl.checkTokenUserMatch(addressRequest.getUsername());

        var address = m_addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException(MyError.ADDRESS_NOT_FOUND));

        address.setRegistrationAddress(addressRequest.getRegistrationAddress());
        address.setZipCode(addressRequest.getZipCode());
        address.setCity(addressRequest.getCity());
        address.setCountry(addressRequest.getCountry());
        var updatedAddress = m_addressRepository.save(address);

        var userAddress = m_userAddressRepository.findByAddress(updatedAddress);
        userAddress.setAddress(address);
        m_userAddressRepository.save(userAddress);

        return  m_addressMapper.toAddressDTO(updatedAddress);
    }

    @Transactional
    public void deleteByAddress(AddressRequest addressRequest)
    {
        m_securityControl.checkTokenUserMatch(addressRequest.getUsername());

        var address = existingAddress(addressRequest);
        if (address.isEmpty())
            throw new ApiException(MyError.ADDRESS_NOT_FOUND);

        m_userAddressRepository.deleteByAddress(address.get());

        m_addressRepository.delete(address.get());
    }

    @Transactional
    public void deleteAddressById(String username, Long addressId)
    {
        m_securityControl.checkTokenUserMatch(username);

        var address = m_addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException(MyError.ADDRESS_NOT_FOUND));

        m_userAddressRepository.deleteByAddress(address);

        m_addressRepository.deleteById(addressId);
    }
}
