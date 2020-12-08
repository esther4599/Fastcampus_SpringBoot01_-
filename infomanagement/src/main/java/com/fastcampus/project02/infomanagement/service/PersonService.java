package com.fastcampus.project02.infomanagement.service;

import com.fastcampus.project02.infomanagement.domain.Person;
import com.fastcampus.project02.infomanagement.domain.dto.PersonDTO;
import com.fastcampus.project02.infomanagement.exception.PersonNotFoundException;
import com.fastcampus.project02.infomanagement.exception.RenameNotPermittedException;
import com.fastcampus.project02.infomanagement.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getPeopleByName(String name) {
        return personRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Person getPerson(Long id) {
        return personRepository.findById(id).orElse(null); //get = optional에서 값을 가져오는 기능
    }

    @Transactional
    public void put(PersonDTO personDTO) {
        Person person = new Person();
        person.set(personDTO);
        person.setName(personDTO.getName());
        personRepository.save(person);
    }

    @Transactional
    public void modify(Long id, PersonDTO persondto) {

        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new); //null인 경우 오류 발생

        if (!person.getName().equals(persondto.getName()))
            throw new RenameNotPermittedException();

        person.set(persondto);
        personRepository.save(person);
    }

    @Transactional
    public void modify(Long id, String name) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);

        person.setName(name);

        personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setDeleted(true);
        personRepository.save(person);
    }

    public Page<Person> getAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }
}
