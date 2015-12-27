package ru.p8nt.graphql.graphql.schema.types;

import graphql.Scalars;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.p8nt.graphql.graphql.relay.GlobalId;
import ru.p8nt.graphql.graphql.relay.RelayService;
import ru.p8nt.graphql.i18n.LocalizationService;

import java.util.Collections;

@Configuration
public class QueryTypeBuilder {
    @Autowired
    private RelayService relayService;

    @Autowired
    private LocalizationService localizationService;

    @Autowired
    @Qualifier("nodeInterface")
    private GraphQLInterfaceType nodeInterface;

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

    @Bean
    protected DataFetcher nodeDataFetcher() {
        return environment -> relayService.getNodeByGlobalId(new GlobalId(environment.getArgument("id")));
    }

    @Bean(name = "queryType")
    public GraphQLObjectType build() {
        return GraphQLObjectType.newObject()
                .name("Query")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("hello")
                        .type(Scalars.GraphQLString)
                        .argument(GraphQLArgument.newArgument()
                                .name("name")
                                .type(Scalars.GraphQLString)
                                .build())
                        .dataFetcher(helloDataFetcher())
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("node")
                        .type(nodeInterface)
                        .argument(GraphQLArgument.newArgument()
                                .name("id")
                                .type(new GraphQLNonNull(Scalars.GraphQLID))
                                .build())
                        .dataFetcher(nodeDataFetcher())
                        .build())
                .field(GraphQLFieldDefinition.newFieldDefinition()
                        .name("viewer")
                        .type(new GraphQLTypeReference("Viewer"))
                        .dataFetcher(environment -> new Object())
                        .build())
                .build();

    }
}
