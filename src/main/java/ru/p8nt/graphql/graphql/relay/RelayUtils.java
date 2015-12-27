package ru.p8nt.graphql.graphql.relay;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import ru.p8nt.graphql.domain.Node;

import static graphql.Scalars.GraphQLID;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

public final class RelayUtils {
    public static GraphQLFieldDefinition idField(NodeType type) {
        return newFieldDefinition()
                .name("id")
                .type(new GraphQLNonNull(GraphQLID))
                .dataFetcher(environment -> {
                    Node node = (Node) environment.getSource();
                    GlobalId globalId = new GlobalId(type.getName(), node.getId());

                    return globalId.getGlobalId();
                })
                .build();
    }
}
