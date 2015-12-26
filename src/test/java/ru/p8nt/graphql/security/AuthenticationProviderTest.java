package ru.p8nt.graphql.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.repositories.SessionRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class AuthenticationProviderTest {
    private SessionRepository sessionRepository;
    private BearerAuthenticationToken bearerAuthenticationToken;
    private Session session;
    private User user;

    private AuthenticationProvider authenticationProvider;

    @BeforeMethod
    public void setUp() {
        sessionRepository = mock(SessionRepository.class);
        bearerAuthenticationToken = mock(BearerAuthenticationToken.class);
        session = mock(Session.class);
        user = mock(User.class);

        authenticationProvider = new AuthenticationProvider(sessionRepository);

        when(bearerAuthenticationToken.getCredentials()).thenReturn("token");
        when(session.getOwner()).thenReturn(user);
    }

    @Test
    public void testValidToken() {
        when(sessionRepository.findOneBySid("token")).thenReturn(session);

        Authentication token = authenticationProvider.authenticate(bearerAuthenticationToken);

        assertEquals(session, token.getCredentials());
        assertEquals(user, token.getPrincipal());
        assertEquals(user.getAuthorities(), token.getAuthorities());
    }

    @Test(expectedExceptions = BadCredentialsException.class, expectedExceptionsMessageRegExp = "Token is invalid")
    public void testInvalidToken() {
        when(sessionRepository.findOneBySid("token")).thenReturn(null);

        authenticationProvider.authenticate(bearerAuthenticationToken);
    }

    @Test
    public void testSupport() {
        assertTrue(authenticationProvider.supports(BearerAuthenticationToken.class));
        assertFalse(authenticationProvider.supports(UserAuthenticationToken.class));
    }
}
