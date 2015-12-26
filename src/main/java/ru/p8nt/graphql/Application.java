package ru.p8nt.graphql;

import lombok.Setter;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.p8nt.graphql.security.AuthenticationFilter;
import ru.p8nt.graphql.security.AuthenticationProvider;

@SpringBootApplication
@EnableNeo4jRepositories
@EnableConfigurationProperties
@PropertySources({
        @PropertySource(value = "classpath:p8nt.properties"),
        @PropertySource(value = "file:p8nt.properties", ignoreResourceNotFound = true)
})
public class Application {

    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationProvider authenticationProvider;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
            http.csrf().disable();
            http.logout().disable();
            http.sessionManagement().disable();
        }
    }

    @Configuration
    @ConfigurationProperties(prefix = "neo4j")
    public static class Neo4JApplicationConfiguration extends Neo4jConfiguration {
        @Setter
        private String url;

        @Bean
        @Override
        public Neo4jServer neo4jServer() {
            return new RemoteServer(url);
        }

        @Bean
        @Override
        public SessionFactory getSessionFactory() {
            return new SessionFactory("ru.p8nt.graphql");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
