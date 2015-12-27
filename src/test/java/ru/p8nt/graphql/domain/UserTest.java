package ru.p8nt.graphql.domain;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

public class UserTest {
    @Test
    public void test() {
        User user = new User();
        List<Session> sessions = Arrays.asList(mock(Session.class), mock(Session.class));

        user.setId(1L);
        user.setNickname("nickname");
        user.setRole("ADMIN");
        user.setSessions(sessions);

        assertEquals(user.getId(), new Long(1L));
        assertEquals(user.getNickname(), "nickname");
        assertEquals(user.getRole(), "ADMIN");
        assertEquals(user.getSessions(), sessions);

        assertEquals(user.toString(), "User(id=1, nickname=nickname, role=ADMIN)");

        User user1 = new User();
        user1.setId(1L);
        assertTrue(user.equals(user1));

        User user2 = new User();
        user2.setId(2L);
        assertFalse(user.equals(user2));

        assertEquals(user.getAuthorities().toString(), "[ROLE_USER, ROLE_ADMIN]");
        user.setRole("USER");
        assertEquals(user.getAuthorities().toString(), "[ROLE_USER]");

        assertEquals(user.getUsername(), "nickname");
        assertNull(user.getPassword());
        assertTrue(user.isEnabled());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isAccountNonExpired());
    }
}
