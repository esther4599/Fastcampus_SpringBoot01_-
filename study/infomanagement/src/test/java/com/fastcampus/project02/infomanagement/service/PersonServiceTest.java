package com.fastcampus.project02.infomanagement.service;

import com.fastcampus.project02.infomanagement.domain.Person;
import com.fastcampus.project02.infomanagement.repository.BlockRepository;
import com.fastcampus.project02.infomanagement.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Test
    void getPeopleExcludeBlocks(){
        List<Person> result = personService.getPeopleExcludeBlocks();

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("허준");
        Assertions.assertThat(result.get(1).getName()).isEqualTo("황진이");
        Assertions.assertThat(result.get(2).getName()).isEqualTo("홍길동");
    }

    @Test
    void getPeopleByName(){
        List<Person> result = personService.getPeopleByName("홍길동");
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(5L);
    }

    @Test
    @Transactional(readOnly = true)
    void getPerson(){
        Person person = personService.getPerson(2L);
        Assertions.assertThat(person.getName()).isEqualTo("허준");
    }
}