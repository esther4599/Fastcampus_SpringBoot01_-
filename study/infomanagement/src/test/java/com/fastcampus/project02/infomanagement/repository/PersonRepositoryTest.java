package com.fastcampus.project02.infomanagement.repository;

import com.fastcampus.project02.infomanagement.domain.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void crud(){
        Person person = new Person();
        person.setName("황진이");
        person.setAge(24);
        person.setBloodType("B");
        person.setHobby("멍 때리기");

        personRepository.save(person);

        List<Person> personList = personRepository.findByName("황진이");

        Assertions.assertThat(personList.size() == 1);
        Assertions.assertThat(personList.get(0).getName().equals("황진이"));
        Assertions.assertThat(personList.get(0).getAge() == 24);
    }

    @Test
    void findByBloodType() {
        List<Person> result = personRepository.findByBloodType("A");

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("홍길동");
        Assertions.assertThat(result.get(1).getName()).isEqualTo("허균");
    }

    @Test
    void findByBirthdayBetween() {
        List<Person> result = personRepository.findByMonthOfBirthday(8);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("홍길동");
        Assertions.assertThat(result.get(1).getName()).isEqualTo("황진이");
    }
}