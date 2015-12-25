package ru.p8nt.graphql.graphql.schema;

import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Map;

@Configuration
public class SchemaBuilder {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public GraphQLSchema build() {
        Map<String, GraphQLObjectType> types = applicationContext.getBeansOfType(GraphQLObjectType.class);
        GraphQLObjectType queryType = types.get("queryType");

        return GraphQLSchema.newSchema()
                .query(queryType)
                .build(new HashSet<GraphQLType>(types.values()));
    }
}
