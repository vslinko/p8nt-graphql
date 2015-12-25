package ru.p8nt.graphql.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.*;

@RestController
public class Controller {
    @Autowired
    private GraphQLSchema schema;

    @Autowired
    private Context context;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object handleGetRequest(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "operationName", required = false) String operationName,
            @RequestParam(name = "variables", required = false) String variables
    ) {
        return processRequest(query, operationName, variables);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded"})
    public Object handleFormRequest(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "operationName", required = false) String operationName,
            @RequestParam(name = "variables", required = false) String variables
    ) {
        return processRequest(query, operationName, variables);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {"application/json"})
    public Object handleJsonRequest(
            @RequestBody Map<String, String> body
    ) {
        return processRequest(body.get("query"), body.get("operationName"), body.get("variables"));
    }

    private Object processRequest(String query, String operationName, String variables) {
        if (query == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Must provide query string.");
        }

        Map<String, Object> variablesMap;

        if (variables == null) {
            variablesMap = new HashMap<String, Object>();
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                MapType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
                variablesMap = mapper.readValue(variables, type);
            } catch (IOException e) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Variables are invalid JSON.");
            }
        }

        return processRequest(query, operationName, variablesMap);
    }

    private Object processRequest(String query, String operationName, Map<String, Object> variables) {
        return new GraphQL(schema).execute(query, operationName, context, variables);
    }
}
