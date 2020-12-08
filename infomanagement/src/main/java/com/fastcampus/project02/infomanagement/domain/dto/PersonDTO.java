package com.fastcampus.project02.infomanagement.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
//birthday 객체에서 만들었듯 기본 생성자는 private로 만들고
//of 생성자를 별도로 만들어 public으로 활용한다.
public class PersonDTO {
    @NotBlank(message = "name is necessary")
    private String name;
    private String hobby;
    private String address;
    private LocalDate birthday;
    private String job;
    private String phoneNumber;
}
