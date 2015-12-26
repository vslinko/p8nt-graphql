package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.graphql.GraphQLRequestContext;

@Configuration
public class SessionTypeBuilder {
    @Bean
    protected DataFetcher ownerDataFetcher() {
        return new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                Session session = (Session) environment.getSource();
                GraphQLRequestContext context = (GraphQLRequestContext) environment.getContext();
                Authentication authentication = context.getAuthorization();

                if (!session.equals(authentication.getCredentials())) {
                    return null;
                }

                return session.getOwner();
            }
        };
    }

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
                        .dataFetcher(ownerDataFetcher())
                        .build())
                .build();
    }
}
