package ru.p8nt.graphql.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class BearerAuthenticationToken extends AbstractAuthenticationToken {
    private String token;

    public BearerAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
