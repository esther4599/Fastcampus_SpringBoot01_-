package com.fastcampus.project02.infomanagement.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Embeddable //entity에 속해있는 dto 라는 것을 표시
@Data
@NoArgsConstructor
public class Birthday {
    private Integer yearOfBirthday;

    @Min(1) @Max(12)
    private Integer monthOfBirthday;

    @Min(1) @Max(31)
    private Integer dayOfBirthday;

    public Birthday(LocalDate birthday){
        this.yearOfBirthday = birthday.getYear();
        this.monthOfBirthday = birthday.getMonthValue();
        this.dayOfBirthday = birthday.getDayOfMonth();
    }
}
