package ru.p8nt.graphql.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@NodeEntity
@ToString(exclude = {"sessions"})
@EqualsAndHashCode(of = {"id"})
public class User implements UserDetails {
    @GraphId
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Setter
    private String role;

    @Relationship(type = "OWNS", direction = Relationship.OUTGOING)
    @Getter
    @Setter
    private List<Session> sessions;

    public String getUsername() {
        return nickname;
    }

    public String getPassword() {
        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>(Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + role)
        ));
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }
}
