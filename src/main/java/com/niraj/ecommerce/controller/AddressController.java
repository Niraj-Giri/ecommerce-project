package com.niraj.ecommerce.controller;

import com.niraj.ecommerce.dto.AddAddressRequest;
import com.niraj.ecommerce.dto.AddressResponse;
import com.niraj.ecommerce.dto.ApiResponse;
import com.niraj.ecommerce.model.User;
import com.niraj.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<ApiResponse<Void>> addAddress(@PathVariable Long userId, @RequestBody AddAddressRequest addAddressRequest){
        ApiResponse<Void> addressResponse=addressService.addAddress(userId,addAddressRequest);
         return ResponseEntity.ok(addressResponse);

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAllAddress() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<List<AddressResponse>> allAddress=addressService.getAllAddress(user.getId());
        return ResponseEntity.ok(allAddress);

    }
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> updateAddress(@RequestBody AddAddressRequest addAddressRequest,@PathVariable Long addressId) {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<Void> apiResponse=addressService.updateAddress(addAddressRequest,user.getId(),addressId);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long addressId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse<Void> apiResponse = addressService.deleteAddress(addressId, user.getId());
        return ResponseEntity.ok(apiResponse);
    }
}
