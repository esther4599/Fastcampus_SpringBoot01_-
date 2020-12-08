package com.fastcampus.project02.infomanagement.service;

import com.fastcampus.project02.infomanagement.domain.Person;
import com.fastcampus.project02.infomanagement.domain.dto.Birthday;
import com.fastcampus.project02.infomanagement.domain.dto.PersonDTO;
import com.fastcampus.project02.infomanagement.exception.PersonNotFoundException;
import com.fastcampus.project02.infomanagement.exception.RenameNotPermittedException;
import com.fastcampus.project02.infomanagement.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    //아래의 주석들은 자동으로 Mock을 만들어 주입
    @InjectMocks //테스트의 대상이 되는 class에 붙는 주석
    private PersonService personService;

    @Mock //테스트하는 class에서 @Autowired하고 있는 class에 붙는 주석
    private PersonRepository personRepository;

    @Test
    void getAll() {
        when(personRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Lists.newArrayList(new Person("martin"), new Person("dennis"), new Person("tony"))));

        //Pageable은 interface. 이를 적용시켜 구현한 것이 PageRequest
        Page<Person> result = personService.getAll(PageRequest.of(0, 3));

        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(3);
        Assertions.assertThat(result.getContent().get(0).getName()).isEqualTo("martin");
        Assertions.assertThat(result.getContent().get(1).getName()).isEqualTo("dennis");
        Assertions.assertThat(result.getContent().get(2).getName()).isEqualTo("tony");
    }

    //get
    @Test
    void getPeopleByName(){
        //when == if : findByName이 호출되면
        when(personRepository.findByName("martin"))
        //thenReturn : 파라미터를 return 한다.
                .thenReturn(Lists.newArrayList(new Person("martin")));
        //=> 실제로 findByName이 호출되는 것이 아니라 호출되었다고 가정을 하는 것이다.

        List<Person> result = personService.getPeopleByName("martin");

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("martin");
    }

    @Test
    void getPerson(){
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));
        Person person = personService.getPerson(1L);

        Assertions.assertThat(person.getName()).isEqualTo("martin");
    }

    @Test
    void getPersonIfNotFound(){
        when(personRepository.findById(1L)).thenReturn(Optional.empty()); //null인 Optional 반환

        Person person = personService.getPerson(1L);
        Assertions.assertThat(person).isNull();
    }

    //put
    @Test
    void put(){
        personService.put(mockPersonDto());

        Mockito.verify(personRepository, times(1)).save(argThat(new IsPersonWillBeInserted()));
    }

    //modify(Long id, PersonDTO persondto)
    @Test
    void modifyIfPersonNotFound(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.empty());

        //실행을 시키면 RuntimeException 예외가 발생할 것

        assertThrows(PersonNotFoundException.class, () -> personService.modify(1L, mockPersonDto()));
    }

    @Test
    void modifyPersonIfNameIsDifferent(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("tony"))); //이름이 다른 경우 테스트이니 DTO와 다른 이름 입력.
        assertThrows(RenameNotPermittedException.class, () -> personService.modify(1L, mockPersonDto()));
    }

    @Test
    void modify(){
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new Person("martin"))); //이름이 같은 경우 test

        personService.modify(1L, mockPersonDto());
//        Mockito.verify(personRepository, times(1)).save(Mockito.any(Person.class));

        //argThat() => argument에 대한 검증을 하는 함수
        Mockito.verify(personRepository, times(1)).save(argThat(new IsPersonWillBeUpdated()));
    }

    //modify(Long id, String name)
    @Test
    void modifyByNameIfPersonNotFound(){
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.modify(1L, "daniel"));
    }

    @Test
    void modifyByName(){
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));

        personService.modify(1L, "daniel");
        Mockito.verify(personRepository, times(1)).save(argThat(new IsNameWillBeUpdated()));
    }

    //delete
    @Test
    void deleteIfPersonNotFound(){
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.delete(1L));
    }

    @Test
    void delete(){
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));
        personService.delete(1L);

        //아래 delete인 이유는 service의 delete는 flag를 true로 바꾸는 코드
        Mockito.verify(personRepository, times(1)).save(argThat(new IsPersonWillBeDeleted()));
    }

    //put() matcher
    private static class IsPersonWillBeInserted implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person) {
            return person.getName().equals("martin")
                    && person.getHobby().equals("programming")
                    && person.getAddress().equals("판교시")
                    && person.getBirthday().equals(Birthday.of(LocalDate.now()))
                    && person.getJob().equals("programmer")
                    && person.getPhoneNumber().equals("010-1111-2222");
        }
    }

    //modify(Long id, PersonDTO persondto)의 matcher
    private static class IsPersonWillBeUpdated implements ArgumentMatcher<Person>{
        @Override
        public boolean matches(Person person) {
            return person.getName().equals("martin")
                    && person.getHobby().equals("programming")
                    && person.getAddress().equals("판교시")
                    && person.getBirthday().equals(Birthday.of(LocalDate.now()))
                    && person.getJob().equals("programmer")
                    && person.getPhoneNumber().equals("010-1111-2222");
        }
    }

    //modify(Long id, String name)의 matcher
    private static class IsNameWillBeUpdated implements ArgumentMatcher<Person> {
        @Override
        public boolean matches(Person person) {
            return person.getName().equals("daniel");
        }
    }

    //delete(Long id) matcher
    private static class IsPersonWillBeDeleted implements ArgumentMatcher<Person> {
        @Override
        public boolean matches(Person person) {
            return person.isDeleted(); //true = 삭제됨
        }
    }

    private PersonDTO mockPersonDto(){
        return PersonDTO.of("martin", "programming", "판교시",
                LocalDate.now(), "programmer", "010-1111-2222");
    }
}