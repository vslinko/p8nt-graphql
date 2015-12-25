package ru.p8nt.graphql.domain;

import lombok.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
@ToString
@EqualsAndHashCode(of = {"id"})
public class User {
    @GraphId
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nickname;

    @Relationship(type = "OWNS", direction = Relationship.OUTGOING)
    @Getter
    @Setter
    private List<Session> sessions;
}
