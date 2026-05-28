package com.niraj.ecommerce.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
}