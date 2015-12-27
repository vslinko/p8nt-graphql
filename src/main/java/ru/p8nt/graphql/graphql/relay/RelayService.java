package ru.p8nt.graphql.graphql.relay;

import graphql.relay.Relay;
import graphql.relay.Relay.ResolvedGlobalId;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.p8nt.graphql.domain.Node;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.repositories.NodeRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static graphql.Scalars.GraphQLID;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

@Service
public class RelayService {
    public enum NodeMeta {
        USER_NODE("User"),
        SESSION_NODE("Session");

        public final String name;
        public final GraphQLTypeReference reference;
        private final String typeBeanName;
        private final String repositoryBeanName;

        NodeMeta(String name) {
            this.name = name;
            this.typeBeanName = name.substring(0, 1).toLowerCase() + name.substring(1) + "Type";
            this.repositoryBeanName = name.substring(0, 1).toLowerCase() + name.substring(1) + "Repository";

            reference = new GraphQLTypeReference(name);
        }

        private static NodeMeta getByName(String name) {
            switch (name) {
                case "User":
                    return USER_NODE;
                case "Session":
                    return SESSION_NODE;
                default:
                    throw new RuntimeException("");
            }
        }

        private static NodeMeta getByObject(Object object) {
            if (object instanceof User) {
                return USER_NODE;
            } else if (object instanceof Session) {
                return SESSION_NODE;
            } else {
                throw new RuntimeException("");
            }
        }
    }

    private final ApplicationContext applicationContext;
    private final Relay relay;
    private final GraphQLInterfaceType nodeInterface;
    private final GraphQLFieldDefinition nodeField;

    private HashMap<NodeMeta, GraphQLObjectType> edgeTypes = new HashMap<>();
    private HashMap<NodeMeta, GraphQLObjectType> connectionTypes = new HashMap<>();

    @Autowired
    public RelayService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        relay = new Relay();
        nodeInterface = relay.nodeInterface(this::nodeInterfaceTypeResolver);
        nodeField = relay.nodeField(nodeInterface, this::nodeFieldDataFetcher);
    }

    public GraphQLInterfaceType nodeInterface() {
        return nodeInterface;
    }

    public GraphQLFieldDefinition nodeField() {
        return nodeField;
    }

    public GraphQLObjectType edgeType(NodeMeta type) {
        if (!edgeTypes.containsKey(type)) {
            edgeTypes.put(type, relay.edgeType(type.name, getGraphQLObjectType(type), nodeInterface, Collections.emptyList()));
        }

        return edgeTypes.get(type);
    }

    public GraphQLObjectType connectionType(NodeMeta type) {
        if (!connectionTypes.containsKey(type)) {
            connectionTypes.put(type, relay.connectionType(type.name, edgeType(type), Collections.emptyList()));
        }

        return connectionTypes.get(type);
    }

    public List<GraphQLArgument> connectionFieldArguments() {
        return relay.getConnectionFieldArguments();
    }

    public GraphQLFieldDefinition idField(NodeMeta type) {
        return newFieldDefinition()
                .name("id")
                .type(new GraphQLNonNull(GraphQLID))
                .dataFetcher(environment -> relay.toGlobalId(type.name, ((Node) environment.getSource()).getId().toString()))
                .build();
    }

    private GraphQLObjectType nodeInterfaceTypeResolver(Object object) {
        return getGraphQLObjectType(NodeMeta.getByObject(object));
    }

    private Node nodeFieldDataFetcher(DataFetchingEnvironment environment) {
        String globalId = environment.getArgument("id");
        ResolvedGlobalId resolvedGlobalId = relay.fromGlobalId(globalId);

        NodeMeta type = NodeMeta.getByName(resolvedGlobalId.type);
        NodeRepository repository = (NodeRepository) applicationContext.getBean(type.repositoryBeanName);

        return repository.findOne(Long.parseLong(resolvedGlobalId.id));
    }

    private GraphQLObjectType getGraphQLObjectType(NodeMeta type) {
        return (GraphQLObjectType) applicationContext.getBean(type.typeBeanName);
    }
}
