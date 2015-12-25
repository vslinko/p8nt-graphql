package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.User;

import java.util.Collections;

@Configuration
public class QueryTypeBuilder {
    @Autowired @Qualifier("userType")
    private GraphQLObjectType userType;

    @Bean(name = "queryType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("Query")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("users")
                        .type(new GraphQLList(userType))
                        .dataFetcher(new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
                                return Collections.singletonList(new User("vslinko"));
                            }
                        })
                        .build())
                .build();

    }
}
