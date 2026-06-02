package com.niraj.ecommerce.service;

import com.niraj.ecommerce.dto.AddAddressRequest;
import com.niraj.ecommerce.dto.AddressResponse;
import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.exception.ResourceNotFoundException;
import com.niraj.ecommerce.model.Address;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.repository.AddressRepository;
import com.niraj.ecommerce.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    public ApiResponse<Void> addAddress(Long userId, AddAddressRequest addAddressRequest) {

        User user=userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        Address address=new Address();
        address.setUser(user);
        address.setArea(addAddressRequest.getArea());
        address.setCity(addAddressRequest.getCity());
        address.setHouse(addAddressRequest.getHouse());
        address.setFullName(addAddressRequest.getFullName());
        address.setMobile(addAddressRequest.getMobile());
        address.setZipcode(addAddressRequest.getZipcode());


        addressRepository.save(address);
        return new ApiResponse<>(true,"Address added successfully",null);
    }
}
