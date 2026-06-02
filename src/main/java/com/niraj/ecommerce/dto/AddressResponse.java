package com.niraj.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String area;
    private String city;
    private String state;
    private String zipcode;
    private String mobile;
    private String  house;
    private String fullName;
}
