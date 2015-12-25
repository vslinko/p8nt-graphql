package ru.p8nt.graphql.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;
import ru.p8nt.graphql.repositories.SessionRepository;

@Service
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        Session session = sessionRepository.findOneBySid(token);

        if (session == null) {
            throw new BadCredentialsException("Token is invalid");
        }

        User user = session.getOwner();

        return new UserAuthenticationToken(user, session);
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
        return authenticationClass.equals(BearerAuthenticationToken.class);
    }
}
