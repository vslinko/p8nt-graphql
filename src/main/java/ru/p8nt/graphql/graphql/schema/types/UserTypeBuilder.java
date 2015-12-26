package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import ru.p8nt.graphql.domain.User;

import java.util.Collections;

@Configuration
public class UserTypeBuilder {
    @Autowired
    private SecurityContext securityContext;

    @Bean
    protected DataFetcher sessionsDataFetcher() {
        return new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();

                if (!user.equals(securityContext.getAuthentication().getPrincipal())) {
                    return Collections.emptyList();
                }

                return user.getSessions();
            }
        };
    }

    @Bean(name = "userType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("User")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("nickname")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("sessions")
                        .type(new GraphQLList(new GraphQLTypeReference("Session")))
                        .dataFetcher(sessionsDataFetcher())
                        .build())
                .build();
    }
}
