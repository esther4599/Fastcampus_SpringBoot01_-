package com.fastcampus.project02.infomanagement.domain;

import com.fastcampus.project02.infomanagement.domain.dto.Birthday;
import com.fastcampus.project02.infomanagement.domain.dto.PersonDTO;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity //entity = 데이터를 담는 객체
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Where(clause = "deleted = false")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotEmpty //String의 경우에는 notEmpty, nullable
    @Column(nullable = false)
    private String name;

    @NonNull
    @Min(1)
    private int age;

    private String hobby;

    @NonNull
    @NotEmpty
    @Column(nullable = false)
    private String bloodType;

    private String address;

    @Valid
    @Embedded
    private Birthday birthday; //예전에는 Date를 사용했지만 현재는 LocalDate를 많이 사용한다.

    private String job;

    @ToString.Exclude
    private String phoneNumber;

    @ColumnDefault("0") //0 = false
    private boolean deleted; //삭제 여부 저장

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Block block;

    public void set(PersonDTO personDto){
        if(personDto.getAge() != 0)
            this.age = personDto.getAge();

        if(!StringUtils.isEmpty(personDto.getHobby()))
            this.hobby = personDto.getHobby();

        if(!StringUtils.isEmpty(personDto.getBloodType()))
            this.bloodType = personDto.getBloodType();

        if(!StringUtils.isEmpty(personDto.getAddress()))
            this.address = personDto.getAddress();

        if(!StringUtils.isEmpty(personDto.getJob()))
            this.job = personDto.getJob();

        if(!StringUtils.isEmpty(personDto.getPhoneNumber()))
            this.phoneNumber = personDto.getPhoneNumber();

    }
}
