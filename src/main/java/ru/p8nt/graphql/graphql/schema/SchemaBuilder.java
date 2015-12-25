package ru.p8nt.graphql.graphql.schema;

import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaBuilder {
    @Autowired @Qualifier("queryType")
    private GraphQLObjectType queryType;

    @Bean
    public GraphQLSchema build() {
        return GraphQLSchema.newSchema()
                .query(queryType)
                .build();
    }
}
