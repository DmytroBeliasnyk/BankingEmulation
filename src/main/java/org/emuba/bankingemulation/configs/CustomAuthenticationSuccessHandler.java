package org.emuba.bankingemulation.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth
                        .getAuthority().equals("ROLE_ADMIN"));

        String targetUrl = isAdmin ? "/admin" : "/";

        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}

