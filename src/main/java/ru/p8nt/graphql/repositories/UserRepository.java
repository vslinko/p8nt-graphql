package ru.p8nt.graphql.repositories;

import org.springframework.data.repository.Repository;
import ru.p8nt.graphql.domain.User;

import java.util.List;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Long> {
    List<User> findAll();
}
