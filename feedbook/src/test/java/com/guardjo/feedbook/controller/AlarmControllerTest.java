package com.guardjo.feedbook.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.feedbook.config.SecurityConfig;
import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.config.auth.JwtAuthManager;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.controller.response.FeedAlarmPageDto;
import com.guardjo.feedbook.model.domain.AlarmSubscriber;
import com.guardjo.feedbook.model.domain.Feed;
import com.guardjo.feedbook.model.domain.FeedAlarm;
import com.guardjo.feedbook.model.domain.types.AlarmArgs;
import com.guardjo.feedbook.model.domain.types.AlarmType;
import com.guardjo.feedbook.service.FeedAlarmService;
import com.guardjo.feedbook.util.JwtProvider;
import com.guardjo.feedbook.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlarmController.class)
@Import(SecurityConfig.class)
class AlarmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeedAlarmService feedAlarmService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private JwtAuthManager jwtAuthManager;

    private final static String TEST_TOKEN = "Bearer testtoken";
    private final static AccountPrincipal TEST_PRINCIPAL = TestDataGenerator.accountPrincipal(1L, "Tester");

    @BeforeEach
    void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(TEST_PRINCIPAL, TEST_PRINCIPAL, TEST_PRINCIPAL.getAuthorities());

        given(jwtProvider.isExpired(eq(TEST_TOKEN))).willReturn(false);
        given(jwtProvider.getUsername(eq(TEST_TOKEN))).willReturn(TEST_PRINCIPAL.getAccount().getUsername());
        given(jwtAuthManager.authenticate(any(Authentication.class))).willReturn(authentication);
    }

    @AfterEach
    void tearDown() {
        then(jwtProvider).should().isExpired(eq(TEST_TOKEN));
        then(jwtProvider).should().getUsername(eq(TEST_TOKEN));
        then(jwtAuthManager).should().authenticate(any(Authentication.class));
    }

    @DisplayName("GET : " + UrlContext.ALARMS_URL)
    @Test
    void test_getAlarms() throws Exception {
        Feed testFeed = TestDataGenerator.feed(1L, "Test", "test", TEST_PRINCIPAL.getAccount());
        FeedAlarm feedAlarm = TestDataGenerator.feedAlarm(AlarmType.COMMENT, new AlarmArgs(2L), testFeed);
        Page<FeedAlarm> feedAlarms = new PageImpl<>(List.of(feedAlarm), Pageable.ofSize(10), 1);
        FeedAlarmPageDto expected = FeedAlarmPageDto.from(feedAlarms, TEST_PRINCIPAL.getAccount());

        given(feedAlarmService.findAllFeedAlarmByAccount(eq(TEST_PRINCIPAL.getAccount()), any(Pageable.class))).willReturn(expected);

        String response = mockMvc.perform(get(UrlContext.ALARMS_URL)
                        .param("page", "0")
                        .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, FeedAlarmPageDto.class);
        BaseResponse<FeedAlarmPageDto> actual = objectMapper.readValue(response, javaType);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.name());
        assertThat(actual.getBody()).isEqualTo(expected);

        then(feedAlarmService).should().findAllFeedAlarmByAccount(eq(TEST_PRINCIPAL.getAccount()), any(Pageable.class));
    }

    @DisplayName("DELETE : " + UrlContext.ALARMS_URL)
    @Test
    void test_deleteAllAlarms() throws Exception {
        willDoNothing().given(feedAlarmService).deleteAllFeedAlarmByAccount(eq(TEST_PRINCIPAL.getAccount()));

        String response = mockMvc.perform(delete(UrlContext.ALARMS_URL)
                        .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(BaseResponse.class, String.class);
        BaseResponse<String> actual = objectMapper.readValue(response, javaType);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(BaseResponse.defaultSuccesses());

        then(feedAlarmService).should().deleteAllFeedAlarmByAccount(eq(TEST_PRINCIPAL.getAccount()));
    }

    @DisplayName("GET : " + UrlContext.ALARM_SUB_URL)
    @Test
    void test_connectAlarms() throws Exception {
        AlarmSubscriber subscriber = new AlarmSubscriber();
        String expected = "Test Event";
        subscriber.send(expected);

        given(feedAlarmService.connectAlarmSubscriber(eq(TEST_PRINCIPAL.getAccount().getId()))).willReturn(subscriber);

        mockMvc.perform(get(UrlContext.ALARM_SUB_URL)
                        .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("data:%s\n\n", expected)));

        then(feedAlarmService).should().connectAlarmSubscriber(eq(TEST_PRINCIPAL.getAccount().getId()));
    }
}