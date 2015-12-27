package ru.p8nt.graphql.graphql.schema.types;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.graphql.relay.RelayService;
import ru.p8nt.graphql.i18n.LocalizationService;

import java.util.Collections;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@Configuration
public class QueryTypeBuilder {
    @Autowired
    private RelayService relayService;

    @Autowired
    private LocalizationService localizationService;

    @Bean
    protected DataFetcher helloDataFetcher() {
        return environment -> {
            String name = environment.getArgument("name");

            if (name == null) {
                name = localizationService.getMessage("hello.defaultName");
            }

            return localizationService.getMessage("hello", Collections.singletonList(name));
        };
    }

    @Bean(name = "queryType")
    public GraphQLObjectType build() {
        return newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("hello")
                        .type(new GraphQLNonNull(GraphQLString))
                        .argument(newArgument()
                                .name("name")
                                .type(GraphQLString)
                                .build())
                        .dataFetcher(helloDataFetcher())
                        .build())
                .field(relayService.nodeField())
                .field(newFieldDefinition()
                        .name("viewer")
                        .type(new GraphQLTypeReference("Viewer"))
                        .dataFetcher(environment -> new Object())
                        .build())
                .build();
    }
}
