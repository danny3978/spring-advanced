//package org.example.expert.domain.client;
//
//import org.example.expert.client.WeatherClient;
//import org.example.expert.client.dto.WeatherDto;
//import org.example.expert.domain.common.exception.ServerException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.boot.test.mock.mockito.SpyBeans;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.net.URI;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@RestClientTest(WeatherClient.class)
//public class WeatherClientTest {
//    private final String uri = "https://f-api.github.io";
//    private final String path = "/f-api/weather.json";
//
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private RestTemplateBuilder restTemplateBuilder;
//
//    @Autowired
//    private MockRestServiceServer mockRestServiceServer;
//
//    @Autowired
//    private WeatherClient weatherClient;
//
//    @BeforeEach
//    void setUp() {
//        this.restTemplate = restTemplateBuilder.build();
//        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
//    }
//
//    @Test
//    void 오늘_날씨_데이터_오류_테스트(){
//
//        WeatherDto[] dto = {
//                new WeatherDto("date","weather"),
//                new WeatherDto("date2","weather2")
//        };
//        ResponseEntity<WeatherDto[]> dtos = new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
//
//        given(restTemplate.getForEntity(any(URI.class),WeatherDto[].class)).willReturn(dtos);
//
//        ServerException exception = assertThrows(ServerException.class,() ->
//                weatherClient.getTodayWeather()
//        );
//
//        assertEquals("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: ", exception.getMessage());
//
//        외부 api 테스트 하는 법 찾아봐도 잘 모르겠음
//
//    }
//
//}
