package ru.p8nt.graphql.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
@Data
public class User {
    @GraphId
    private Long id;

    private String nickname;

    @Relationship(type = "OWNS", direction = "OUTGOING")
    private List<Session> sessions;
}
