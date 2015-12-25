package ru.p8nt.graphql.graphql;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GraphQLRequestContext {
    public Authentication getAuthorization() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
