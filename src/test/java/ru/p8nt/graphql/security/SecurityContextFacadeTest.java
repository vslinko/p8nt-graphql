package ru.p8nt.graphql.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

public class SecurityContextFacadeTest {
    @Test
    public void test() {
        Authentication authentication1 = mock(Authentication.class);
        Authentication authentication2 = mock(Authentication.class);

        SecurityContextFacade contextFacade = new SecurityContextFacade();

        contextFacade.setAuthentication(authentication1);
        assertEquals(authentication1, SecurityContextHolder.getContext().getAuthentication());

        SecurityContextHolder.getContext().setAuthentication(authentication2);
        assertEquals(authentication2, contextFacade.getAuthentication());
    }
}
