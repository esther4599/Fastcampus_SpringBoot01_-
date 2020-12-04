package com.fastcampus.project02.infomanagement.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class helloWorldControllerTest {

    @Autowired //spring에서 알아서 bean 주입. singleton pattern
    private HelloWorldController helloWorldController;

    private MockMvc mockMvc;

    @Test
    //아래와 같이 아무것도 적지 않으면 default 접근제한자. 동일한 패키지에서만 접근이 가능하다.
    void helloWorld(){ //junit5 에서는 public 외에 접근제한자를 사용할 수 있다.

        //intelli j 기본코드 템플릿으로 sout을 지원한다. sout 치면 자동으로 변환됨
        // control + shift + r = 실행
        System.out.println(helloWorldController.helloWorld());

        //string boot starter test에 포함된 Assert 이용해서 검증하기
        Assertions.assertThat(helloWorldController.helloWorld()).isEqualTo("Hello World");
        // isEqualTo() 의 매개변수와 helloWorld()의 return 값이 다르면 오류 발생

    }

    // spring에서 제공하는 class 이용 = MockMvc test. http 유효성 검사 가능
    @Test
    void MockMvcTest() throws Exception {
        // 설정
        mockMvc = MockMvcBuilders.standaloneSetup(helloWorldController).build();

        //실제로 동작하도록 요청
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/helloWorld")
        ) //여기까지 작성하고 실행시 test 성공은 뜨지만 내용이 보이지는 않는다.
        .andDo( MockMvcResultHandlers.print() ) //실행 결과가 함께 나타난다.
        /*  아래는 실행 결과의 일부
            MockHttpServletResponse:
            Status = 200
            Error message = null
            Headers = [Content-Type:"text/plain;charset=ISO-8859-1", Content-Length:"11"]
            Content type = text/plain;charset=ISO-8859-1
            Body = Hello World
            Forwarded URL = null
            Redirected URL = null
            Cookies = []
        * */
        .andExpect(MockMvcResultMatchers.status().isOk()) //예외처리 : status가 OK인지 확인
        .andExpect(MockMvcResultMatchers.content().string("Hello World")); //받아온 결과의 body 내용 확인
    }
}