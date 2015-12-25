package ru.p8nt.graphql.graphql.schema;

import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class SchemaBuilder {
    @Autowired
    @Qualifier("userType")
    private GraphQLObjectType userType;

    @Autowired
    @Qualifier("sessionType")
    private GraphQLObjectType sessionType;

    @Autowired
    @Qualifier("queryType")
    private GraphQLObjectType queryType;

    @Bean
    public GraphQLSchema build() {
        return GraphQLSchema.newSchema()
                .query(queryType)
                .build(new HashSet<GraphQLType>(Arrays.asList(
                        userType,
                        sessionType
                )));
    }
}
