package ru.p8nt.graphql.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@ToString
@EqualsAndHashCode(of = {"id"})
public class Session {
    @GraphId
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String sid;

    @Relationship(type = "OWNS", direction = Relationship.INCOMING)
    @Getter
    @Setter
    private User owner;
}
