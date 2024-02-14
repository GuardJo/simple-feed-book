package com.guardjo.feedbook.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.feedbook.config.TestSecurityConfig;
import com.guardjo.feedbook.controller.request.SignupRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.service.AccountService;

@WebMvcTest(controllers = AccountController.class)
@Import(TestSecurityConfig.class)
class AccountControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AccountService accountService;

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

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.name());
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

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.CONFLICT.name());

		then(accountService).should().createAccount(eq(username), eq(nickname), eq(password));
	}
}