package com.fastcampus.project02.infomanagement.controller;

import com.fastcampus.project02.infomanagement.domain.Person;
import com.fastcampus.project02.infomanagement.domain.dto.PersonDTO;
import com.fastcampus.project02.infomanagement.repository.PersonRepository;
import com.fastcampus.project02.infomanagement.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id){
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //201 response = okay와 동일하게 200번대 성공을 의미. 해당 정보가 생성되었음을 좀 더 명확하게 알려주는 코드.
    public void postPerson(@RequestBody Person person){
        personService.put(person);
        log.info("person-> {}", personRepository.findAll());
    }

    @PutMapping("/{id}")
    public void modifyPerson(@PathVariable Long id, @RequestBody PersonDTO persondto){
        personService.modify(id, persondto);
        log.info("person -> {}", personRepository.findAll());
    }

    @PatchMapping("/{id}")
    public void modifyPerson(@PathVariable Long id, String name){
        personService.modify(id, name);
        log.info("person -> {}", personRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id){
        personService.delete(id);
        log.info("person -> {}", personRepository.findAll());
    }
}
