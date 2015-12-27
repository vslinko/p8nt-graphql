package ru.p8nt.graphql.graphql.relay;

import graphql.schema.GraphQLTypeReference;
import lombok.Getter;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.repositories.SessionRepository;
import ru.p8nt.graphql.repositories.UserRepository;

public enum NodeType {
    USER(
            "User",
            "userType",
            UserRepository.class
    ),
    SESSION(
            "Session",
            "sessionType",
            SessionRepository.class
    );

    @Getter
    private final String name;
    @Getter
    private final String typeBeanName;
    @Getter
    private final Class repositoryClass;

    NodeType(String name, String typeBeanName, Class repositoryClass) {
        this.name = name;
        this.typeBeanName = typeBeanName;
        this.repositoryClass = repositoryClass;
    }

    public GraphQLTypeReference getGraphQLTypeReference() {
        return new GraphQLTypeReference(name);
    }

    public static NodeType getByName(String name) {
        switch (name) {
            case "User":
                return USER;
            case "Session":
                return SESSION;
            default:
                return null;
        }
    }

    public static NodeType getByObject(Object object) {
        if (object instanceof User) {
            return USER;
        } else if (object instanceof Session) {
            return SESSION;
        } else {
            return null;
        }
    }
}
