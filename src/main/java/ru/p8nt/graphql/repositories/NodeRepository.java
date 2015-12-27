package ru.p8nt.graphql.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import ru.p8nt.graphql.domain.Node;

@NoRepositoryBean
public interface NodeRepository<T extends Node> extends Repository<T, Long> {
    T findOne(Long id);
}
