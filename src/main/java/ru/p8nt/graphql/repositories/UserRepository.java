package ru.p8nt.graphql.repositories;

import ru.p8nt.graphql.domain.User;

import java.util.List;

@org.springframework.stereotype.Repository
public interface UserRepository extends NodeRepository<User> {
    List<User> findAll();
}
