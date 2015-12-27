package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.graphql.relay.NodeType;
import ru.p8nt.graphql.security.SecurityService;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static ru.p8nt.graphql.graphql.relay.RelayUtils.idField;

@Configuration
public class SessionTypeBuilder {
    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("nodeInterface")
    private GraphQLInterfaceType nodeInterface;

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
                .name(NodeType.SESSION.getName())
                .withInterface(nodeInterface)
                .field(idField(NodeType.SESSION))
                .field(newFieldDefinition()
                        .name("sid")
                        .type(new GraphQLNonNull(GraphQLString))
                        .build())
                .field(newFieldDefinition()
                        .name("owner")
                        .type(new GraphQLNonNull(NodeType.USER.getGraphQLTypeReference()))
                        .dataFetcher(ownerDataFetcher())
                        .build())
                .build();
    }
}
