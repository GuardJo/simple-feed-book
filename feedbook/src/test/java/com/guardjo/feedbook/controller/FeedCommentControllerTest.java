package com.guardjo.feedbook.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
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
import com.guardjo.feedbook.controller.request.FeedCommentCreateRequest;
import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.service.FeedCommentService;
import com.guardjo.feedbook.util.JwtProvider;
import com.guardjo.feedbook.util.TestDataGenerator;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = FeedCommentController.class)
class FeedCommentControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private JwtProvider jwtProvider;
	@MockBean
	private JwtAuthManager jwtAuthManager;
	@MockBean
	private FeedCommentService feedCommentService;

	private final static String TEST_TOKEN = "Bearer Test";
	private final static AccountPrincipal TEST_PRINCIPAL = TestDataGenerator.accountPrincipal(1L, "Tester");

	@BeforeEach
	void setUp() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(TEST_PRINCIPAL, TEST_PRINCIPAL, TEST_PRINCIPAL.getAuthorities());

		given(jwtProvider.isExpired(eq(TEST_TOKEN))).willReturn(false);
		given(jwtProvider.getUsername(eq(TEST_TOKEN))).willReturn(TEST_PRINCIPAL.getUsername());
		given(jwtAuthManager.authenticate(any(Authentication.class))).willReturn(authentication);
	}

	@AfterEach
	void tearDown() {
		then(jwtProvider).should().isExpired(eq(TEST_TOKEN));
		then(jwtProvider).should().getUsername(eq(TEST_TOKEN));
		then(jwtAuthManager).should().authenticate(any(Authentication.class));
	}

	@DisplayName("POST : " + UrlContext.FEED_COMMENTS_URL + " : 정상")
	@Test
	void test_saveFeedComment() throws Exception {
		long feedId = 1L;
		FeedCommentCreateRequest request = new FeedCommentCreateRequest(feedId, "test");
		String requestContent = objectMapper.writeValueAsString(request);

		willDoNothing().given(feedCommentService).createNewComment(eq(request.content()), eq(TEST_PRINCIPAL.getAccount()), eq(feedId));

		String response = mockMvc.perform(post(UrlContext.FEED_COMMENTS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
				.content(requestContent)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		BaseResponse<String> actual = objectMapper.readValue(response, BaseResponse.class);

		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(BaseResponse.defaultSuccesses());

		then(feedCommentService).should().createNewComment(eq(request.content()), eq(TEST_PRINCIPAL.getAccount()), eq(feedId));
	}

	@DisplayName("POST : " + UrlContext.FEED_COMMENTS_URL + " : 입력값이 잘못된 경우")
	@Test
	void test_saveFeedComment_BadRequest() throws Exception {
		long feedId = 1L;
		FeedCommentCreateRequest request = new FeedCommentCreateRequest(feedId, "");
		String requestContent = objectMapper.writeValueAsString(request);

		willDoNothing().given(feedCommentService).createNewComment(eq(request.content()), eq(TEST_PRINCIPAL.getAccount()), eq(feedId));

		String response = mockMvc.perform(post(UrlContext.FEED_COMMENTS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
				.content(requestContent)
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
}