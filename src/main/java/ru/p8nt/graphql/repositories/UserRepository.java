package ru.p8nt.graphql.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import ru.p8nt.graphql.domain.User;

import java.util.List;

public interface UserRepository extends GraphRepository<User> {
    List<User> findAll();
}
