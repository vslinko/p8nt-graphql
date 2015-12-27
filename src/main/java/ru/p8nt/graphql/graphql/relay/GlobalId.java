package ru.p8nt.graphql.graphql.relay;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class GlobalId {
    private final String globalId;
    private final Long modelId;
    private final String typeName;

    public GlobalId(String globalId) {
        this.globalId = globalId;

        String decoded = new String(Base64.getDecoder().decode(globalId), StandardCharsets.UTF_8);
        int separatorIndex = decoded.indexOf(':');

        if (separatorIndex < 0) {
            throw new IllegalArgumentException("Invalid GraphQL node id \"" + globalId + "\"");
        }

        this.typeName = decoded.substring(0, separatorIndex);
        this.modelId = Long.parseLong(decoded.substring(separatorIndex + 1));
    }

    public GlobalId(String typeName, Long modelId) {
        this.typeName = typeName;
        this.modelId = modelId;

        this.globalId = Base64.getEncoder().encodeToString((typeName + ":" + modelId.toString()).getBytes());
    }

    public String getGlobalId() {
        return globalId;
    }

    public Long getModelId() {
        return modelId;
    }

    public String getTypeName() {
        return typeName;
    }
}
