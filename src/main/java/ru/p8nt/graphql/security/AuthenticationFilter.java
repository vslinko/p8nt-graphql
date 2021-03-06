package ru.p8nt.graphql.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {
    private final AuthenticationManager authenticationManager;
    private final SecurityContext securityContext;

    public AuthenticationFilter(AuthenticationManager authenticationManager, SecurityContext securityContext) {
        this.authenticationManager = authenticationManager;
        this.securityContext = securityContext;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Authentication auth = authenticationManager.authenticate(new BearerAuthenticationToken(token));
                securityContext.setAuthentication(auth);
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (AuthenticationException e) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
