package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.graphql.GraphQLRequestContext;

import java.util.Collections;

@Configuration
public class UserTypeBuilder {
    @Bean
    protected DataFetcher sessionsDataFetcher() {
        return new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                User user = (User) environment.getSource();
                GraphQLRequestContext context = (GraphQLRequestContext) environment.getContext();
                Authentication authentication = context.getAuthorization();

                if (!user.equals(authentication.getPrincipal())) {
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
