package ru.p8nt.graphql.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
public class Session {
    @GraphId
    private Long id;

    private String sid;

    @Relationship(type = "OWNS", direction = "INGOING")
    private User owner;
}
