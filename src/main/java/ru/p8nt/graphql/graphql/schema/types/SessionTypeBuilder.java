package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.graphql.relay.RelayService;
import ru.p8nt.graphql.security.SecurityService;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static ru.p8nt.graphql.graphql.relay.RelayService.NodeMeta.SESSION_NODE;
import static ru.p8nt.graphql.graphql.relay.RelayService.NodeMeta.USER_NODE;

@Configuration
public class SessionTypeBuilder {
    @Autowired
    private SecurityService securityService;

    @Autowired
    private RelayService relayService;

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
        return newObject()
                .name(SESSION_NODE.name)
                .withInterface(relayService.nodeInterface())
                .field(relayService.idField(SESSION_NODE))
                .field(newFieldDefinition()
                        .name("sid")
                        .type(new GraphQLNonNull(GraphQLString))
                        .build())
                .field(newFieldDefinition()
                        .name("owner")
                        .type(new GraphQLNonNull(USER_NODE.reference))
                        .dataFetcher(ownerDataFetcher())
                        .build())
                .build();
    }
}
