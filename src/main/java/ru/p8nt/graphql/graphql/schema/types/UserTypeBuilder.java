package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.graphql.relay.NodeType;
import ru.p8nt.graphql.security.SecurityService;

import java.util.Collections;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static ru.p8nt.graphql.graphql.relay.RelayUtils.idField;

@Configuration
public class UserTypeBuilder {
    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("nodeInterface")
    private GraphQLInterfaceType nodeInterface;

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
                .name(NodeType.USER.getName())
                .withInterface(nodeInterface)
                .field(idField(NodeType.USER))
                .field(newFieldDefinition()
                        .name("nickname")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("sessions")
                        .type(new GraphQLList(new GraphQLNonNull(NodeType.SESSION.getGraphQLTypeReference())))
                        .dataFetcher(sessionsDataFetcher())
                        .build())
                .build();
    }
}
