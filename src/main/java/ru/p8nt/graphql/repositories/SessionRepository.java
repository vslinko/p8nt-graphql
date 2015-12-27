package ru.p8nt.graphql.repositories;

import ru.p8nt.graphql.domain.Session;

@org.springframework.stereotype.Repository
public interface SessionRepository extends NodeRepository<Session> {
    Session findOneBySid(String sid);
}
