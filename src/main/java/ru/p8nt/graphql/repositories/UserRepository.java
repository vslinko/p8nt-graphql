package ru.p8nt.graphql.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.p8nt.graphql.domain.User;

import java.util.List;

public interface UserRepository extends GraphRepository<User> {
    @PreAuthorize("hasRole('ROLE_USER')")
    List<User> findAll();

    User findOneByNickname(String nickname);

    @Query("MATCH (user:User)-[:OWNS]->(session:Session {sid: {0}}) RETURN user")
    User findOneByToken(String token);
}
