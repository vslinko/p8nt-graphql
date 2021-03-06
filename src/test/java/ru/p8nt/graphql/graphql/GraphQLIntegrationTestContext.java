package ru.p8nt.graphql.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import ru.p8nt.graphql.Application;
import ru.p8nt.graphql.graphql.mvc.GraphQLController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class})
public class GraphQLIntegrationTestContext extends AbstractTestNGSpringContextTests {
    public static class QueryBuilder {
        private final MockHttpServletRequestBuilder requestBuilder;
        private final MockMvc mvc;

        public QueryBuilder(MockHttpServletRequestBuilder requestBuilder, MockMvc mvc) {
            this.requestBuilder = requestBuilder;
            this.mvc = mvc;
        }

        public QueryBuilder withParams(String params) {
            requestBuilder.param("variables", params);
            return this;
        }

        public ResultActions query(String query) throws Exception {
            requestBuilder.param("query", query);

            return mvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Autowired
    private GraphQLController controller;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    protected MockMvc mvc;

    @BeforeMethod
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).addFilter(springSecurityFilterChain).build();
    }

    protected QueryBuilder asAnonymous() {
        return new QueryBuilder(get("/"), mvc);
    }

    protected QueryBuilder asUser() {
        return new QueryBuilder(get("/").header("Authorization", "Bearer user"), mvc);
    }
}
