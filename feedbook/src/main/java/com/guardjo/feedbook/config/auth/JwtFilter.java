package com.guardjo.feedbook.config.auth;

import com.guardjo.feedbook.controller.UrlContext;
import com.guardjo.feedbook.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtAuthManager jwtAuthManager;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
            ServletException,
            IOException {

        if (!(CorsUtils.isPreFlightRequest(request) || request.getRequestURI().equals(UrlContext.LOGIN_URL) || request.getRequestURI().equals(UrlContext.SIGNUP_URL))) {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (!StringUtils.hasText(token)) {
                log.error("Not Found Authorization Header");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else if (jwtProvider.isExpired(token)) {
                log.warn("Expired Token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                String username = jwtProvider.getUsername(token);
                Authentication authentication = jwtAuthManager.authenticate(new UsernamePasswordAuthenticationToken(username, username));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
