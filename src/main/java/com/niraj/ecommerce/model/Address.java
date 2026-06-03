package com.niraj.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String area;
    private String city;
    private String state;
    private String zipcode;
    private String mobile;
    private String  house;
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
