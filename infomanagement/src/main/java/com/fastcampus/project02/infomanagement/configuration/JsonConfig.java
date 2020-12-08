package com.fastcampus.project02.infomanagement.configuration;

import com.fastcampus.project02.infomanagement.configuration.serializer.BirthdaySerializer;
import com.fastcampus.project02.infomanagement.domain.dto.Birthday;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration //2개 이상의 Bean을 가짐을 의미
public class JsonConfig {

    @Bean
    //ObjectMapper를 주입하는 역할
    public MappingJackson2HttpMessageConverter MappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        return converter;
    }

    @Bean
    //실제로 customizing할 부분
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper(); //기본 맵퍼
        objectMapper.registerModule(new BirthdayModule()); //작성한 serializer 추가
        objectMapper.registerModule(new JavaTimeModule()); // 작성하지 않으면 LocalDate.now()가 엄청 복잡하게 출력된다.

        //19988-01-01 의 형식으로 출력될 수 있도록 설정
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    //모듈
    static class BirthdayModule extends SimpleModule {
        BirthdayModule(){
            super(); //부모 class의 생성자 호출

            //birthday class에 BirthdaySerializer를 사용하겠다.
            addSerializer(Birthday.class, new BirthdaySerializer());
        }
    }
}
