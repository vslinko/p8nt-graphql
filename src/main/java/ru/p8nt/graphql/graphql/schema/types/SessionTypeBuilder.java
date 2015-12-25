package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionTypeBuilder {
    @Bean(name = "sessionType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("Session")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("sid")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("owner")
                        .type(new GraphQLTypeReference("User"))
                        .build())
                .build();
    }
}
