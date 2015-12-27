package ru.p8nt.graphql.graphql.schema.types;


import org.testng.annotations.Test;
import ru.p8nt.graphql.graphql.GraphQLIntegrationTestContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class QueryTypeBuilderTest extends GraphQLIntegrationTestContext {
    @Test
    public void testAnonymousCantGetUsers() throws Exception {
        asAnonymous()
                .query("{viewer {users {edges {node {nickname}}}}}")
                .andExpect(jsonPath(".errors[0].message")
                        .value("Exception while fetching data: org.springframework.security.access.AccessDeniedException: Access Denied"))
                .andExpect(jsonPath(".data.viewer.users.edges[0]").doesNotExist());

        asUser()
                .query("{viewer {users {edges {node {nickname}}}}}")
                .andExpect(jsonPath(".errors[0]").doesNotExist())
                .andExpect(jsonPath(".data.viewer.users.edges[0].node.nickname").isNotEmpty());
    }
}
