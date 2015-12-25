package ru.p8nt.graphql.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class Controller {
    @Autowired
    private GraphQLSchema schema;

    @Autowired
    private Context context;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object handleRequest(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "operationName", required = false) String operationName,
            @RequestParam(name = "variables", required = false) String variables
    ) throws IOException {
        Map<String, Object> variablesMap;

        if (variables == null) {
            variablesMap = new HashMap<String, Object>();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            MapType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
            variablesMap = mapper.readValue(variables, type);
        }

        return new GraphQL(schema).execute(query, operationName, context, variablesMap);
    }
}
