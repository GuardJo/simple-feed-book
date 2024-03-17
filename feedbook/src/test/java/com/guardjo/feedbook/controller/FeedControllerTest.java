package com.guardjo.feedbook.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.feedbook.config.SecurityConfig;
import com.guardjo.feedbook.config.auth.AccountPrincipal;
import com.guardjo.feedbook.config.auth.JwtAuthManager;
import com.guardjo.feedbook.controller.request.FeedCreateRequest;
import com.guardjo.feedbook.controller.request.FeedModifyRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.InvalidRequestException;
import com.guardjo.feedbook.service.FeedService;
import com.guardjo.feedbook.util.JwtProvider;
import com.guardjo.feedbook.util.TestDataGenerator;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = FeedController.class)
class FeedControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private FeedService feedService;
	@MockBean
	private JwtProvider jwtProvider;
	@MockBean
	private JwtAuthManager jwtAuthManager;

	private final static AccountPrincipal TEST_PRINCIPAL = TestDataGenerator.accountPrincipal(1L, "test123");

	@BeforeEach
	void setUp() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(TEST_PRINCIPAL, TEST_PRINCIPAL,
			TEST_PRINCIPAL.getAuthorities());
		given(jwtProvider.isExpired(anyString())).willReturn(false);
		given(jwtProvider.getUsername(anyString())).willReturn(TEST_PRINCIPAL.getUsername());
		given(jwtAuthManager.authenticate(any(Authentication.class))).willReturn(authentication);
	}

	@DisplayName("POST : " + UrlContext.FEEDS_URL + " : 정상")
	@Test
	void test_createFeed() throws Exception {
		FeedCreateRequest request = new FeedCreateRequest("title", "content");
		String token = "Bearer test-token";
		String requestContent = objectMapper.writeValueAsString(request);

		willDoNothing().given(feedService).saveFeed(eq(request.title()), eq(request.content()), eq(TEST_PRINCIPAL.getAccount()));

		String response = mockMvc.perform(post(UrlContext.FEEDS_URL)
				.content(requestContent)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(BaseResponse.defaultSuccesses());

		then(feedService).should().saveFeed(eq(request.title()), eq(request.content()), eq(TEST_PRINCIPAL.getAccount()));
	}

	@DisplayName("POST : " + UrlContext.FEEDS_URL + " : 요청 데이터가 올바르지 않을 경우")
	@Test
	void test_createFeed_badRequest() throws Exception {
		FeedCreateRequest request = new FeedCreateRequest(null, "content");
		String token = "Bearer test-token";
		String requestContent = objectMapper.writeValueAsString(request);

		String response = mockMvc.perform(post(UrlContext.FEEDS_URL)
				.content(requestContent)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.name());
	}

	@DisplayName("PATCH : " + UrlContext.FEEDS_URL + " : 정상")
	@Test
	void test_updateFeed() throws Exception {
		FeedModifyRequest request = new FeedModifyRequest(1L, "title", "content");
		String token = "Bearer test-token";
		String requestContent = objectMapper.writeValueAsString(request);

		willDoNothing().given(feedService)
			.updateFeed(eq(request.feedId()), eq(request.title()), eq(request.content()), eq(TEST_PRINCIPAL.getAccount()));

		String response = mockMvc.perform(patch(UrlContext.FEEDS_URL)
				.content(requestContent)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(BaseResponse.defaultSuccesses());

		then(feedService).should().updateFeed(eq(request.feedId()), eq(request.title()), eq(request.content()), eq(TEST_PRINCIPAL.getAccount()));

	}

	@DisplayName("PATCH : " + UrlContext.FEEDS_URL + " : 잘못된 요청 데이터")
	@Test
	void test_updateFeed_BadRequest() throws Exception {
		FeedModifyRequest request = new FeedModifyRequest(null, "title", "content");
		String token = "Bearer test-token";
		String requestContent = objectMapper.writeValueAsString(request);

		String response = mockMvc.perform(patch(UrlContext.FEEDS_URL)
				.content(requestContent)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.name());
	}

	@DisplayName("PATCH : " + UrlContext.FEEDS_URL + " : 권한 없는 사용자")
	@Test
	void test_updateFeed_Forbidden() throws Exception {
		FeedModifyRequest request = new FeedModifyRequest(1L, "title", "content");
		String token = "Bearer test-token";
		String requestContent = objectMapper.writeValueAsString(request);

		willThrow(InvalidRequestException.class).given(feedService)
			.updateFeed(eq(request.feedId()), eq(request.title()), eq(request.content()), eq(TEST_PRINCIPAL.getAccount()));

		String response = mockMvc.perform(patch(UrlContext.FEEDS_URL)
				.content(requestContent)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.name());

		then(feedService).should().updateFeed(eq(request.feedId()), eq(request.title()), eq(request.content()), eq(TEST_PRINCIPAL.getAccount()));
	}

	@DisplayName("DELETE : " + UrlContext.FEEDS_URL + " : 정상")
	@Test
	void test_deleteFeed() throws Exception {
		long feedId = 1L;
		String token = "Bearer test-token";

		willDoNothing().given(feedService).deleteFeed(eq(feedId), eq(TEST_PRINCIPAL.getAccount()));

		String response = mockMvc.perform(delete(UrlContext.FEEDS_URL + "/" + feedId)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(BaseResponse.defaultSuccesses());

		then(feedService).should().deleteFeed(eq(feedId), eq(TEST_PRINCIPAL.getAccount()));
	}

	@DisplayName("DELETE : " + UrlContext.FEEDS_URL + " : 피드를 찾을 수 없는 경우")
	@Test
	void test_deleteFeed_NotFoundFeed() throws Exception {
		long feedId = 1L;
		String token = "Bearer test-token";

		willThrow(EntityNotFoundException.class).given(feedService).deleteFeed(eq(feedId), eq(TEST_PRINCIPAL.getAccount()));

		String response = mockMvc.perform(delete(UrlContext.FEEDS_URL + "/" + feedId)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.name());

		then(feedService).should().deleteFeed(eq(feedId), eq(TEST_PRINCIPAL.getAccount()));
	}

	@DisplayName("DELETE : " + UrlContext.FEEDS_URL + " : 권한 없는 사용자인 경우")
	@Test
	void test_deleteFeed_InvalidUser() throws Exception {
		long feedId = 1L;
		String token = "Bearer test-token";

		willThrow(InvalidRequestException.class).given(feedService).deleteFeed(eq(feedId), eq(TEST_PRINCIPAL.getAccount()));

		String response = mockMvc.perform(delete(UrlContext.FEEDS_URL + "/" + feedId)
				.header(HttpHeaders.AUTHORIZATION, token)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.name());

		then(feedService).should().deleteFeed(eq(feedId), eq(TEST_PRINCIPAL.getAccount()));
	}
}