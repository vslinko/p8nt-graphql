package ru.p8nt.graphql.repositories;

import org.springframework.data.repository.Repository;
import ru.p8nt.graphql.domain.Session;

@SuppressWarnings("unused")
public interface SessionRepository extends Repository<Session, Long> {
    Session findOneBySid(String sid);
}
