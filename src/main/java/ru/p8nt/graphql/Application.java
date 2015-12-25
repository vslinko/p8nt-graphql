package ru.p8nt.graphql;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;

@SpringBootApplication
@EnableNeo4jRepositories
public class Application {
    @Configuration
    public static class Neo4JApplicationConfiguration extends Neo4jConfiguration {
        @Bean
        @Override
        public Neo4jServer neo4jServer() {
            return new RemoteServer("http://localhost:7474");
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
