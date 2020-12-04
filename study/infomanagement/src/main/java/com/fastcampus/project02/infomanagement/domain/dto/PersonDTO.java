package com.fastcampus.project02.infomanagement.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonDTO {
    private String name;
    private int age;
    private String hobby;
    private String bloodType;
    private String address;
    private LocalDate birthday;
    private String job;
    private String phoneNumber;
}
