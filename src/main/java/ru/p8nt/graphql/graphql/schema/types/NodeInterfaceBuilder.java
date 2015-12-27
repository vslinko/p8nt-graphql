package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.graphql.relay.RelayService;

@Configuration
public class NodeInterfaceBuilder {
    @Autowired
    private RelayService relayService;

    @Bean(name = "nodeInterface")
    public GraphQLInterfaceType build() {
        return GraphQLInterfaceType.newInterface()
                .name("Node")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(Scalars.GraphQLID))
                        .build())
                .typeResolver(object -> relayService.getGraphQLObjectTypeByObject(object))
                .build();

    }
}
