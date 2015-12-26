package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.p8nt.graphql.i18n.LocalizationService;
import ru.p8nt.graphql.repositories.UserRepository;

import java.util.Collections;

@Configuration
public class QueryTypeBuilder {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocalizationService localizationService;

    @Bean
    protected DataFetcher helloDataFetcher() {
        return new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                String name = environment.getArgument("name");

                if (name == null) {
                    name = localizationService.getMessage("hello.defaultName");
                }

                return localizationService.getMessage("hello", Collections.singletonList(name));
            }
        };
    }

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
                        .name("hello")
                        .type(Scalars.GraphQLString)
                        .argument(GraphQLArgument.newArgument()
                                .name("name")
                                .type(Scalars.GraphQLString)
                                .build())
                        .dataFetcher(helloDataFetcher())
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("users")
                        .type(new GraphQLList(new GraphQLTypeReference("User")))
                        .dataFetcher(usersDataFetcher())
                        .build())
                .build();

    }
}
