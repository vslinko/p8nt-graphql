package ru.p8nt.graphql.graphql.relay;

import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.p8nt.graphql.domain.Node;
import ru.p8nt.graphql.repositories.NodeRepository;

import java.util.Collection;

@Service
public class RelayService {
    private final ApplicationContext applicationContext;

    @Autowired
    public RelayService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public GraphQLObjectType getGraphQLObjectTypeByObject(Object object) {
        NodeType type = NodeType.getByObject(object);

        return (GraphQLObjectType) applicationContext.getBean(type.getTypeBeanName());
    }

    public Node getNodeByGlobalId(GlobalId globalId) {
        NodeType type = NodeType.getByName(globalId.getTypeName());
        NodeRepository repository = getNodeRepositoryByType(type);

        return repository.findOne(globalId.getModelId());
    }

    private NodeRepository getNodeRepositoryByType(NodeType type) {
        Collection<?> repositories = applicationContext.getBeansOfType(type.getRepositoryClass()).values();

        if (repositories.size() != 1) {
            throw new RuntimeException("Unable to found exactly one repository of type \"" + type.getName() + "\"");
        }

        return (NodeRepository) repositories.toArray()[0];
    }
}
