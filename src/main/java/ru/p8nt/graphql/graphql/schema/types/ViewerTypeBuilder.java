package ru.p8nt.graphql.graphql.schema.types;

import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.graphql.relay.RelayService;
import ru.p8nt.graphql.repositories.UserRepository;
import ru.p8nt.graphql.security.SecurityService;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static ru.p8nt.graphql.graphql.relay.RelayService.NodeMeta.USER_NODE;

@Configuration
public class ViewerTypeBuilder {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RelayService relayService;

    @Bean
    protected DataFetcher usersDataFetcher() {
        return environment -> {
            securityService.assertExpression("hasRole('ROLE_USER')");

            return new SimpleListConnection(userRepository.findAll()).get(environment);
        };
    }

    @Bean(name = "viewerType")
    public GraphQLObjectType build() {
        return newObject()
                .name("Viewer")
                .field(newFieldDefinition()
                        .name("users")
                        .type(relayService.connectionType(USER_NODE))
                        .argument(relayService.connectionFieldArguments())
                        .dataFetcher(usersDataFetcher())
                        .build())
                .build();

    }
}
