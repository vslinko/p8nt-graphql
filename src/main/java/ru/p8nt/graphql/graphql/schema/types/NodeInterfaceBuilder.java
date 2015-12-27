package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.graphql.relay.RelayService;

import static graphql.Scalars.GraphQLID;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInterfaceType.newInterface;

@Configuration
public class NodeInterfaceBuilder {
    @Autowired
    private RelayService relayService;

    @Bean(name = "nodeInterface")
    public GraphQLInterfaceType build() {
        return newInterface()
                .name("Node")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .build())
                .typeResolver(object -> relayService.getGraphQLObjectTypeByObject(object))
                .build();

    }
}
