package com.fastcampus.project02.infomanagement.domain;

import com.fastcampus.project02.infomanagement.domain.dto.Birthday;
import com.fastcampus.project02.infomanagement.domain.dto.PersonDTO;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

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

    private String hobby;

    private String address;

    @Valid
    @Embedded
    private Birthday birthday; //예전에는 Date를 사용했지만 현재는 LocalDate를 많이 사용한다.

    private String job;

    private String phoneNumber;

    @ColumnDefault("0") //0 = false
    private boolean deleted; //삭제 여부 저장

    public void set(PersonDTO personDto){
        if(!StringUtils.isEmpty(personDto.getHobby()))
            this.setHobby(personDto.getHobby());

        if(!StringUtils.isEmpty(personDto.getAddress()))
            this.setAddress(personDto.getAddress());

        if(!StringUtils.isEmpty(personDto.getJob()))
            this.setJob(personDto.getJob());

        if(!StringUtils.isEmpty(personDto.getPhoneNumber()))
            this.setPhoneNumber(personDto.getPhoneNumber());

        if(personDto.getBirthday() != null) {
            this.setBirthday(Birthday.of(personDto.getBirthday()));
        }
    }

    //나이와 같이 주기적으로 변경되는 값은 알아서계산해 가져오도록 변경한다.
    //아래에서 Birthday가 null인지 확인하는 이유는 test에서는 birthday 객체를 입력받지 않기 때문이다.
    public Integer getAge(){
        if (this.birthday != null)
            return LocalDate.now().getYear() - this.birthday.getYearOfBirthday() + 1;
        else
            return null;
    }

    //생일인지 아닌지 여부를 알려주는 함수
    public boolean isBirthdayToday(){
        return LocalDate.now().equals(LocalDate.of(this.birthday.getYearOfBirthday(), this.birthday.getMonthOfBirthday(), this.birthday.getDayOfBirthday()));
    }
}