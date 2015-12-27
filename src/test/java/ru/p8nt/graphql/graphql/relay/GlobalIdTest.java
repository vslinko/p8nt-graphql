package ru.p8nt.graphql.graphql.relay;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class GlobalIdTest {
    @Test
    public void test() {
        assertEquals(new GlobalId("User", 1L).getGlobalId(), "VXNlcjox");
        assertEquals(new GlobalId("U2Vzc2lvbjoy").getTypeName(), "Session");
        assertEquals(new GlobalId("U2Vzc2lvbjoy").getModelId(), (Long) 2L);
    }
}
