package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.dto.address.AddressDTO;
import com.doganmehmet.app.request.AddressRequest;
import com.doganmehmet.app.services.json.AddressService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("json/api/address")
public class AddressController {

    private final AddressService m_addressService;

    public AddressController(AddressService addressService)
    {
        m_addressService = addressService;
    }

    @PostMapping("/save")
    public AddressDTO saveAddress(@Valid @RequestBody AddressRequest addressRequest)
    {
        return m_addressService.saveAddress(addressRequest);
    }

    @GetMapping("/find")
    public List<AddressDTO> findAddressByUsername(@RequestParam String username)
    {
        return m_addressService.findAddressByUsername(username);
    }
    @PutMapping("/update")
    public AddressDTO updateAddress(@RequestParam Long addressId, @Valid @RequestBody AddressRequest addressRequest)
    {
        return m_addressService.updateAddress(addressRequest, addressId);
    }

    @DeleteMapping("/delete")
    public void deleteByAddress(@Valid @RequestBody AddressRequest addressRequest)
    {
        m_addressService.deleteByAddress(addressRequest);
    }

    @DeleteMapping("/delete/id")
    public void deleteById(@RequestParam String username,@RequestParam Long addressId)
    {
        m_addressService.deleteAddressById(username, addressId);
    }
}

