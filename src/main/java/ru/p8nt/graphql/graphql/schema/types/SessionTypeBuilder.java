package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.security.SecurityService;

@Configuration
public class SessionTypeBuilder {
    @Autowired
    private SecurityService securityService;

    @Bean
    protected DataFetcher ownerDataFetcher() {
        return environment -> {
            Session session = (Session) environment.getSource();

            if (!session.equals(securityService.getCurrentSession())) {
                return null;
            }

            return session.getOwner();
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
