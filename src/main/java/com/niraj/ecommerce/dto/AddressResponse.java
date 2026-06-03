package com.niraj.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private Long id;
    private String area;
    private String city;
    private String state;
    private String zipcode;
    private String mobile;
    private String  house;
    private String fullName;
}
