package ru.p8nt.graphql.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class SecurityServiceTest {
    SecurityContext securityContext;
    UserAuthenticationToken authentication;
    User user;
    Session session;

    SecurityService securityService;

    @BeforeMethod
    public void setUp() {
        securityContext = mock(SecurityContext.class);
        authentication = mock(UserAuthenticationToken.class);
        user = mock(User.class);
        session = mock(Session.class);

        securityService = new SecurityService(securityContext);

        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.getCredentials()).thenReturn(session);
        when(authentication.getAuthorities()).thenReturn(
                new HashSet<>(Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_USER")
                ))
        );
    }

    @Test
    public void testExpressionEvaluation() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        assertTrue(securityService.evaluateExpression("hasRole('ROLE_ADMIN')"));
        assertTrue(securityService.evaluateExpression("hasRole('ROLE_USER')"));
        assertFalse(securityService.evaluateExpression("hasRole('ROLE_SUPER')"));

        when(securityContext.getAuthentication()).thenReturn(null);

        assertFalse(securityService.evaluateExpression("hasRole('ROLE_USER')"));
    }

    @Test(expectedExceptions = AccessDeniedException.class, expectedExceptionsMessageRegExp = "Custom Message")
    public void testExpressionAssertion() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityService.assertExpression("hasRole('ROLE_USER')");
        securityService.assertExpression("hasRole('ROLE_SUPER')", "Custom Message");
    }

    @Test
    public void testCurrentUserGetter() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        assertEquals(user, securityService.getCurrentUser());

        when(securityContext.getAuthentication()).thenReturn(null);

        assertNull(securityService.getCurrentUser());

        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));

        assertNull(securityService.getCurrentUser());
    }

    @Test
    public void testCurrentSessionGetter() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        assertEquals(session, securityService.getCurrentSession());

        when(securityContext.getAuthentication()).thenReturn(null);

        assertNull(securityService.getCurrentSession());

        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));

        assertNull(securityService.getCurrentSession());
    }
}
