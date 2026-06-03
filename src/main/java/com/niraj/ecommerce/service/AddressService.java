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

import java.util.ArrayList;
import java.util.List;

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
        address.setState(addAddressRequest.getState());
        addressRepository.save(address);

        return new ApiResponse<>(true,"Address added successfully",null);
    }

    public ApiResponse<List<AddressResponse>> getAllAddress(Long userId) {
        List<Address> allAddress=addressRepository.findAllByUserId(userId);

        List<AddressResponse> addressResponseList=allAddress.stream()
                .map(this::mapToResponse )
                .toList();

        return new ApiResponse<>(true,"Address list",addressResponseList);

    }


    public ApiResponse<Void> updateAddress(AddAddressRequest addAddressRequest, Long id, Long addressId) {
        Address address=addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if(!address.getUser().getId().equals(userRepository.findById(id).get().getId())){
            throw new ResourceNotFoundException("User not found");
        }
        address.setFullName(addAddressRequest.getFullName());
        address.setMobile(addAddressRequest.getMobile());
        address.setZipcode(addAddressRequest.getZipcode());
        address.setArea(addAddressRequest.getArea());
        address.setCity(addAddressRequest.getCity());
        address.setHouse(addAddressRequest.getHouse());
        address.setState(addAddressRequest.getState());
        addressRepository.save(address);
        return new ApiResponse<>(true,"Address updated successfully",null);
    }

    private AddressResponse mapToResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .house(address.getHouse())
                .area(address.getArea())
                .city(address.getCity())
                .state(address.getState())
                .zipcode(address.getZipcode())
                .mobile(address.getMobile())
                .build();
    }


}
