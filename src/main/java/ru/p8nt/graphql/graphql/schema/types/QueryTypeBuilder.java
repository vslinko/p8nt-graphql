package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.p8nt.graphql.repositories.UserRepository;

@Configuration
public class QueryTypeBuilder {

    @Service
    @Qualifier("QueryTypeBuilder.UsersDataFetcher")
    private static class UsersDataFetcher implements DataFetcher {
        @Autowired
        private UserRepository userRepository;

        @Override
        @PreAuthorize("hasRole('ROLE_USER')")
        public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
            return userRepository.findAll();
        }
    }

    @Autowired
    @Qualifier("QueryTypeBuilder.UsersDataFetcher")
    private DataFetcher usersDataFetcher;

    @Bean(name = "queryType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("Query")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("users")
                        .type(new GraphQLList(new GraphQLTypeReference("User")))
                        .dataFetcher(usersDataFetcher)
                        .build())
                .build();

    }
}
