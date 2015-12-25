package ru.p8nt.graphql.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import ru.p8nt.graphql.domain.Session;

public interface SessionRepository extends GraphRepository<Session> {
    Session findOneBySid(String sid);
}
