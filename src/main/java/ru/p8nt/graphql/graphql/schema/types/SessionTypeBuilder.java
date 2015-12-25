package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.domain.Session;

@Configuration
public class SessionTypeBuilder {
    @Bean(name = "sessionType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("Session")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("sid")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("owner")
                        .type(new GraphQLTypeReference("User"))
                        .dataFetcher(new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
                                return ((Session) dataFetchingEnvironment.getSource()).getOwner();
                            }
                        })
                        .build())
                .build();
    }
}
