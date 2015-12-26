package ru.p8nt.graphql.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import ru.p8nt.graphql.domain.Session;
import ru.p8nt.graphql.domain.User;

@Service
public class SecurityService {
    private class SimpleSecurityExpressionRoot extends SecurityExpressionRoot {
        public SimpleSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }
    }

    private final SecurityContext securityContext;

    @Autowired
    public SecurityService(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public void assertExpression(String expression) {
        assertExpression(expression, "Access Denied");
    }

    public void assertExpression(String expression, String errorMessage) {
        if (!evaluateExpression(expression)) {
            throw new AccessDeniedException(errorMessage);
        }
    }

    public Boolean evaluateExpression(String expression) {
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return false;
        }

        ExpressionParser parser = new SpelExpressionParser();

        return ExpressionUtils.evaluateAsBoolean(
                parser.parseExpression(expression),
                new StandardEvaluationContext(
                        new SimpleSecurityExpressionRoot(
                                authentication
                        )
                )
        );
    }

    public Session getCurrentSession() {
        Authentication authentication = securityContext.getAuthentication();

        if (!(authentication instanceof UserAuthenticationToken)) {
            return null;
        }

        return (Session) authentication.getCredentials();
    }

    public User getCurrentUser() {
        Authentication authentication = securityContext.getAuthentication();

        if (!(authentication instanceof UserAuthenticationToken)) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }
}
