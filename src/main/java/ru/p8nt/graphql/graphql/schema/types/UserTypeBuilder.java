package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.graphql.relay.RelayService;
import ru.p8nt.graphql.security.SecurityService;

import java.util.Collections;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static ru.p8nt.graphql.graphql.relay.RelayService.NodeMeta.SESSION_NODE;
import static ru.p8nt.graphql.graphql.relay.RelayService.NodeMeta.USER_NODE;

@Configuration
public class UserTypeBuilder {
    @Autowired
    private SecurityService securityService;

    @Autowired
    private RelayService relayService;

    @Bean
    protected DataFetcher sessionsDataFetcher() {
        return environment -> {
            User user = (User) environment.getSource();

            if (!user.equals(securityService.getCurrentUser())) {
                return Collections.emptyList();
            }

            return user.getSessions();
        };
    }

    @Bean(name = "userType")
    public GraphQLObjectType build() {
        return newObject()
                .name(USER_NODE.name)
                .withInterface(relayService.nodeInterface())
                .field(relayService.idField(USER_NODE))
                .field(newFieldDefinition()
                        .name("nickname")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("sessions")
                        .type(new GraphQLList(new GraphQLNonNull(SESSION_NODE.reference)))
                        .dataFetcher(sessionsDataFetcher())
                        .build())
                .build();
    }
}
