package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.AddAddressRequest;
import com.niraj.ecommerce.dto.AddressResponse;
import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<ApiResponse<Void>> addAddress(@PathVariable Long userId, AddAddressRequest addAddressRequest){
        ApiResponse<Void> addressResponse=addressService.addAddress(userId,addAddressRequest);
         return ResponseEntity.ok(addressResponse);

    }
}
