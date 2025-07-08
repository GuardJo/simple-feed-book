package com.guardjo.feedbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.feedbook.config.SecurityConfig;
import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.config.auth.JwtAuthManager;
import com.guardjo.feedbook.controller.request.LoginRequest;
import com.guardjo.feedbook.controller.request.SignupRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.WrongPasswordException;
import com.guardjo.feedbook.service.AccountService;
import com.guardjo.feedbook.util.JwtProvider;
import com.guardjo.feedbook.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@Import(SecurityConfig.class)
class AccountControllerTest {
    private final static String TEST_TOKEN = "Bearer testtoken";
    private final static AccountPrincipal TEST_PRINCIPAL = TestDataGenerator.accountPrincipal(1L, "Tester");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private JwtAuthManager jwtAuthManager;

    @BeforeEach
    void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(TEST_PRINCIPAL, TEST_PRINCIPAL, TEST_PRINCIPAL.getAuthorities());

        given(jwtProvider.isExpired(eq(TEST_TOKEN))).willReturn(false);
        given(jwtProvider.getUsername(eq(TEST_TOKEN))).willReturn(TEST_PRINCIPAL.getAccount().getUsername());
        given(jwtAuthManager.authenticate(any(Authentication.class))).willReturn(authentication);
    }

    @AfterEach
    void tearDown() {
        then(jwtProvider).should(atLeast(0)).isExpired(eq(TEST_TOKEN));
        then(jwtProvider).should(atLeast(0)).getUsername(eq(TEST_TOKEN));
        then(jwtAuthManager).should(atLeast(0)).authenticate(any(Authentication.class));
    }

    @DisplayName("POST : " + UrlContext.SIGNUP_URL + ": 정상")
    @Test
    void test_signup() throws Exception {
        String username = "test";
        String nickname = "tester";
        String password = "1234";
        SignupRequest signupRequest = new SignupRequest(username, nickname, password);
        String content = objectMapper.writeValueAsString(signupRequest);
        BaseResponse<String> expected = BaseResponse.defaultSuccesses();

        willDoNothing().given(accountService).createAccount(eq(username), eq(nickname), eq(password));

        String response = mockMvc.perform(post(UrlContext.SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

        assertThat(actual).isEqualTo(expected);

        then(accountService).should().createAccount(eq(username), eq(nickname), eq(password));
    }

    @DisplayName("POST : " + UrlContext.SIGNUP_URL + ": 입력값이 올바르지 않는 경우")
    @Test
    void test_signup_badRequest() throws Exception {
        String username = "test";
        String nickname = "tester";
        String password = null;
        SignupRequest signupRequest = new SignupRequest(username, nickname, password);
        String content = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post(UrlContext.SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST : " + UrlContext.SIGNUP_URL + ": 이미 가입된 정보 요청")
    @Test
    void test_signup_duplicate() throws Exception {
        String username = "test";
        String nickname = "tester";
        String password = "1234";
        SignupRequest signupRequest = new SignupRequest(username, nickname, password);
        String content = objectMapper.writeValueAsString(signupRequest);

        willThrow(new DuplicateUsernameException(username)).given(accountService).createAccount(eq(username), eq(nickname), eq(password));

        mockMvc.perform(post(UrlContext.SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isConflict());

        then(accountService).should().createAccount(eq(username), eq(nickname), eq(password));
    }

    @DisplayName("POST : " + UrlContext.LOGIN_URL + " : 정상")
    @Test
    void test_login() throws Exception {
        String username = "test123";
        String password = "1234";
        LoginRequest loginRequest = new LoginRequest(username, password);
        String content = objectMapper.writeValueAsString(loginRequest);
        String expected = "test-token";

        given(accountService.login(eq(username), eq(password))).willReturn(expected);

        String response = mockMvc.perform(post(UrlContext.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

        assertThat(actual.getBody()).isEqualTo(expected);

        then(accountService).should().login(eq(username), eq(password));
    }

    @DisplayName("POST : " + UrlContext.LOGIN_URL + " : 아이디가 없거나 비밀번호가 틀린 경우")
    @ParameterizedTest
    @MethodSource("loginArguments")
    void test_login_not_found_or_unautorized(Class<? extends Exception> e, HttpStatus httpStatus) throws Exception {
        String username = "test123";
        String password = "1234";
        LoginRequest loginRequest = new LoginRequest(username, password);
        String content = objectMapper.writeValueAsString(loginRequest);

        given(accountService.login(eq(username), eq(password))).willThrow(e);

        mockMvc.perform(post(UrlContext.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().is(httpStatus.value()));

        then(accountService).should().login(eq(username), eq(password));
    }

    @DisplayName("POST : " + UrlContext.LOGIN_URL + " : 입력값이 올바르지 않는 경우")
    @Test
    void test_login_bad_request() throws Exception {
        String username = "test123";
        LoginRequest loginRequest = new LoginRequest(username, null);
        String content = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post(UrlContext.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("GET : " + UrlContext.AUTH_URL + " : 정상")
    @Test
    void test_authenticate() throws Exception {
        mockMvc.perform(get(UrlContext.AUTH_URL)
                        .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("GET : " + UrlContext.AUTH_URL + " : 검증 실패")
    @Test
    void test_authenticate_unAuthorization() throws Exception {
        mockMvc.perform(get(UrlContext.AUTH_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("POST : " + UrlContext.LOGOUT_URL + " : 정상")
    @Test
    void test_logout() throws Exception {
        willDoNothing().given(accountService).logout(eq(TEST_PRINCIPAL));

        mockMvc.perform(post(UrlContext.LOGOUT_URL)
                        .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        then(accountService).should().logout(eq(TEST_PRINCIPAL));
    }

    private static Stream<Arguments> loginArguments() {
        return Stream.of(
                Arguments.of(EntityNotFoundException.class, HttpStatus.NOT_FOUND),
                Arguments.of(WrongPasswordException.class, HttpStatus.UNAUTHORIZED)
        );
    }
}