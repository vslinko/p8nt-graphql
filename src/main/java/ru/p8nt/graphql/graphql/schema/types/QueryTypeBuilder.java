package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.p8nt.graphql.repositories.UserRepository;

@Configuration
public class QueryTypeBuilder {
    @Autowired
    private UserRepository userRepository;

    @Bean
    protected DataFetcher usersDataFetcher() {
        return new DataFetcher() {
            @Override
            @PreAuthorize("hasRole('ROLE_USER')")
            public Object get(DataFetchingEnvironment environment) {
                return userRepository.findAll();
            }
        };
    }

    @Bean(name = "queryType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("Query")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("users")
                        .type(new GraphQLList(new GraphQLTypeReference("User")))
                        .dataFetcher(usersDataFetcher())
                        .build())
                .build();

    }
}
