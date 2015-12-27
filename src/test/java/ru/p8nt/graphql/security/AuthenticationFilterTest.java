package ru.p8nt.graphql.security;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class AuthenticationFilterTest {
    private AuthenticationManager authenticationManager;
    private SecurityContext securityContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private Authentication authentication;

    private AuthenticationFilter authenticationFilter;

    @BeforeMethod
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        securityContext = mock(SecurityContext.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        authentication = mock(Authentication.class);

        authenticationFilter = new AuthenticationFilter(authenticationManager, securityContext);
    }

    @Test
    public void testRequestWithoutAuthorizationHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authenticationFilter.doFilter(request, response, chain);

        verify(securityContext, never()).setAuthentication(any(Authentication.class));
    }

    @Test
    public void testRequestWithoutBearerToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");

        authenticationFilter.doFilter(request, response, chain);

        verify(securityContext, never()).setAuthentication(any(Authentication.class));
    }

    @Test
    public void testRequestWithToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(authenticationManager.authenticate(Mockito.any(BearerAuthenticationToken.class)))
                .thenReturn(authentication);

        authenticationFilter.doFilter(request, response, chain);

        ArgumentCaptor<BearerAuthenticationToken> argument = ArgumentCaptor.forClass(BearerAuthenticationToken.class);
        verify(authenticationManager).authenticate(argument.capture());
        assertEquals(argument.getValue().getCredentials(), "token");
        assertNull(argument.getValue().getPrincipal());

        verify(securityContext).setAuthentication(authentication);
    }

    @Test
    public void testRequestWithInvalidToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(authenticationManager.authenticate(Mockito.any(BearerAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Error message"));

        authenticationFilter.doFilter(request, response, chain);

        verify(securityContext, never()).setAuthentication(any(Authentication.class));

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error message");
    }
}
