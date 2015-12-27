package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.repositories.UserRepository;
import ru.p8nt.graphql.security.SecurityService;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@Configuration
public class ViewerTypeBuilder {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Bean
    protected DataFetcher usersDataFetcher() {
        return environment -> {
            securityService.assertExpression("hasRole('ROLE_USER')");

            return userRepository.findAll();
        };
    }

    @Bean(name = "viewerType")
    public GraphQLObjectType build() {
        return newObject()
                .name("Viewer")
                .field(newFieldDefinition()
                        .name("users")
                        .type(new GraphQLList(new GraphQLNonNull(new GraphQLTypeReference("User"))))
                        .dataFetcher(usersDataFetcher())
                        .build())
                .build();

    }
}
