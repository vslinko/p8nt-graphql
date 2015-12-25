package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.User;

@Configuration
public class UserTypeBuilder {
    @Bean(name = "userType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("User")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("nickname")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("sessions")
                        .type(new GraphQLList(new GraphQLTypeReference("Session")))
                        .dataFetcher(new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
                                return ((User) dataFetchingEnvironment.getSource()).getSessions();
                            }
                        })
                        .build())
                .build();
    }
}
