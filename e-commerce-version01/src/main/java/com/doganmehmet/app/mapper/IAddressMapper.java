package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.address.AddressDTO;
import com.doganmehmet.app.entity.Address;
import com.doganmehmet.app.request.AddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "AddressMapperImpl", componentModel = "spring")
public interface IAddressMapper {

    AddressDTO toAddressDTO(Address address);

    Address toAddress(AddressRequest addressRequest);

    List<AddressDTO> toAddressDTOList(List<Address> addressList);
}
