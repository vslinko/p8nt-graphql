package ru.p8nt.graphql.domain;

import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

public class SessionTest {
    @Test
    public void test() {
        Session session = new Session();
        User user = mock(User.class);

        session.setId(1L);
        session.setSid("sid");
        session.setOwner(user);

        assertEquals(session.getId(), new Long(1L));
        assertEquals(session.getSid(), "sid");
        assertEquals(session.getOwner(), user);

        assertEquals(session.toString(), "Session(id=1, sid=sid)");

        Session session1 = new Session();
        session1.setId(1L);
        assertTrue(session.equals(session1));

        Session session2 = new Session();
        session2.setId(2L);
        assertFalse(session.equals(session2));
    }
}
