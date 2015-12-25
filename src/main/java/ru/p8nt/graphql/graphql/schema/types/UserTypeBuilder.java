package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeBuilder {
    @Bean(name = "userType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("User")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("username")
                        .type(Scalars.GraphQLString)
                        .build())
                .build();
    }
}
