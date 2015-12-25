package ru.p8nt.graphql.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;

public class UserAuthenticationToken extends AbstractAuthenticationToken {
    private User principal;
    private Session credentials;

    public UserAuthenticationToken(User user, Session session) {
        super(user.getAuthorities());
        this.principal = user;
        this.credentials = session;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }
}
