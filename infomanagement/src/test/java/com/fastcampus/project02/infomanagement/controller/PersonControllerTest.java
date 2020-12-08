package com.fastcampus.project02.infomanagement.controller;

import com.fastcampus.project02.infomanagement.domain.Person;
import com.fastcampus.project02.infomanagement.domain.dto.Birthday;
import com.fastcampus.project02.infomanagement.domain.dto.PersonDTO;
import com.fastcampus.project02.infomanagement.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@Transactional
class PersonControllerTest {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter massageConverter;

    @BeforeEach //해당 메소드는 매 테스트 전에 먼저 실행된다.
    void beforeEach(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/person")
                        .param("page", "1") //현재 만들어질 페이지의 2번째 페이지를 받아온다. 0 = 첫 페이지
                        .param("size", "2")) //size = 한 페이지에 출력되는 data 수
                .andExpect(status().isOk())
                //"$"를 이용해 결과를 받아 사이즈를 가져와 비교한다.
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(6))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                //json에서 배열의 0번째 값 받아오기
                .andExpect(jsonPath("$.content.[0].name").value("dennis"))
                .andExpect(jsonPath("$.content.[1].name").value("sophia"));
    }

    @Test
    void getPerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/person/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("martin"))
                .andExpect(jsonPath("hobby").isEmpty()) //null check
                .andExpect(jsonPath("address").isEmpty())
                .andExpect(jsonPath("$.birthday").value("1991-08-15"))
                .andExpect(jsonPath("$.job").isEmpty())
                .andExpect(jsonPath("$.phoneNumber").isEmpty())
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.age").isNumber()) //숫자인지 확인
                .andExpect(jsonPath("$.birthdayToday").isBoolean()); //변하는 값은 타입을 확인
    }

    @Test
    void postPerson() throws Exception{ //사람 추가
        PersonDTO dto = PersonDTO.of("martin", "멍 때리기", "경기도 성남시 분당구 판교로",
                LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(dto))) //Person에 저장해야 하는데 데이터로 personDTO를 넘겨줌.
                .andExpect(status().isCreated());

        //가장 최근에 추가된 객체의 id가 가장 큼. 가장 최근의 데이터 가져오기.
        Person result = personRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals("martin", result.getName()),
                () -> Assertions.assertEquals("멍 때리기", result.getHobby()),
                () -> Assertions.assertEquals("경기도 성남시 분당구 판교로", result.getAddress()),
                () -> Assertions.assertEquals(Birthday.of(LocalDate.now()), result.getBirthday()),
                () -> Assertions.assertEquals("programmer", result.getJob()),
                () -> Assertions.assertEquals("010-1111-2222", result.getPhoneNumber())
        );
    }

    @Test //post에서 name이 null인 경우
    void postPersonIfNameIsNull() throws Exception {
        PersonDTO dto = new PersonDTO();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("name is necessary"));
    }

    @Test //personDTO의 @NotEmpty는 null과 "" 모두를 불허한다.
    void postPersonIfNameIsEmpty() throws Exception{
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(personDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("name is necessary"));
    }

    @Test
    void postPersonIfNameIsBlankSpace() throws  Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(" ");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(personDTO)))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("name is necessary"));
    }

    @Test
    void modifiedPerson() throws Exception{
        PersonDTO personDTO = PersonDTO.of("martin", "멍 때리기", "경기도 성남시 분당구 판교로",
        LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(personDTO)))
                .andExpect(status().isOk());

        Person result = personRepository.findById(1L).get();

        Assertions.assertAll(
                () -> Assertions.assertEquals("martin", result.getName()),
                () -> Assertions.assertEquals("멍 때리기", result.getHobby()),
                () -> Assertions.assertEquals("경기도 성남시 분당구 판교로", result.getAddress()),
                () -> Assertions.assertEquals(Birthday.of(LocalDate.now()), result.getBirthday()),
                () -> Assertions.assertEquals("programmer", result.getJob()),
                () -> Assertions.assertEquals("010-1111-2222", result.getPhoneNumber())
        );
    }

    @Test //이름이 잘못된 경우 오류 발생하는 코드
    void modifyPersonIfNameIsDifferent() throws Exception{
        PersonDTO personDTO = PersonDTO.of("james", "멍 때리기", "경기도 성남시 분당구 판교로",
                LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(personDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("name is different"));
    }

    @Test //id가 다른 경우
    void modifyPersonIfPersonNotFound() throws Exception {
        PersonDTO dto = PersonDTO.of("james", "programmind", "판교", LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("entity is not found"));
    }

    @Test //status에 대한 검증이 있을뿐 데이터에 대한 검증이 없다.
    void modifyName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/person/1")
                .param("name", "martinModified"))
                .andExpect(status().isOk());
        Assertions.assertTrue(personRepository.findById(1L).get().getName().equals("martinModified"));
    }

    @Test
    void deletePerson() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/person/1"))
                .andExpect(status().isOk());
        Assertions.assertTrue(personRepository.findPeopleDeleted().stream().anyMatch(person -> person.getId().equals(1L)));
    }

    private String toJsonString(PersonDTO persondto) throws JsonProcessingException{
        return objectMapper.writeValueAsString(persondto);
    }
}