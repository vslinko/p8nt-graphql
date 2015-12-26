package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import ru.p8nt.graphql.domain.Session;

@Configuration
public class SessionTypeBuilder {
    @Autowired
    private SecurityContext securityContext;

    @Bean
    protected DataFetcher ownerDataFetcher() {
        return new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                Session session = (Session) environment.getSource();

                if (!session.equals(securityContext.getAuthentication().getCredentials())) {
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
