package ru.p8nt.graphql.graphql.relay;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;
import ru.p8nt.graphql.domain.Node;

public final class RelayUtils {
    public static GraphQLFieldDefinition idField(NodeType type) {
        return GraphQLFieldDefinition.newFieldDefinition()
                .name("id")
                .type(new GraphQLNonNull(Scalars.GraphQLID))
                .dataFetcher(environment -> {
                    Node node = (Node) environment.getSource();
                    GlobalId globalId = new GlobalId(type.getName(), node.getId());

                    return globalId.getGlobalId();
                })
                .build();
    }
}
