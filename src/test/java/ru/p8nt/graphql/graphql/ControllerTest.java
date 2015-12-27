package ru.p8nt.graphql.graphql;

import org.springframework.http.MediaType;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControllerTest extends GraphQLTest {
    @Test
    public void testGetRequest() throws Exception {
        mvc.perform(get("/").param("query", "query q($name: String!) {hello(name: $name)}").param("variables", "{\"name\":\"John\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data.hello").value("Hello John!"))
                .andExpect(jsonPath(".errors").isArray())
                .andExpect(jsonPath(".errors[0]").doesNotExist());

        mvc.perform(get("/"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data").doesNotExist())
                .andExpect(jsonPath(".errors[0].message").value("Must provide query string."));

        mvc.perform(get("/").param("query", "{hello}").param("variables", "invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data").doesNotExist())
                .andExpect(jsonPath(".errors[0].message").value("Variables are invalid JSON."));
    }

    @Test
    public void testFormRequest() throws Exception {
        mvc.perform(post("/").param("query", "{hello}").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data.hello").value("Hello World!"))
                .andExpect(jsonPath(".errors").isArray())
                .andExpect(jsonPath(".errors[0]").doesNotExist());

        mvc.perform(post("/").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data").doesNotExist())
                .andExpect(jsonPath(".errors[0].message").value("Must provide query string."));

        mvc.perform(post("/").param("query", "{hello}").param("variables", "invalid json").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data").doesNotExist())
                .andExpect(jsonPath(".errors[0].message").value("Variables are invalid JSON."));
    }

    @Test
    public void testJsonRequest() throws Exception {
        mvc.perform(post("/").content("{\"query\":\"{hello}\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data.hello").value("Hello World!"))
                .andExpect(jsonPath(".errors").isArray())
                .andExpect(jsonPath(".errors[0]").doesNotExist());

        mvc.perform(post("/").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data").doesNotExist())
                .andExpect(jsonPath(".errors[0].message").value("Must provide query string."));

        mvc.perform(post("/").content("{\"query\":\"{hello}\",\"variables\":\"invalid json\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath(".data").doesNotExist())
                .andExpect(jsonPath(".errors[0].message").value("Variables are invalid JSON."));
    }
}
