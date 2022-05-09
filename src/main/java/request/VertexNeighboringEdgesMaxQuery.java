package request;


import config.RequestType;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class VertexNeighboringEdgesMaxQuery extends AbstractRequest {
    private final String entityId;
    private final String property;
    private final long time;
    private final int repeat;

    public VertexNeighboringEdgesMaxQuery(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("entityId"), "entityId not exists.");
        checkState(data.containsKey("property"), "property name not exists.");
        checkState(data.containsKey("time"), "time not exists.");
        checkState(data.containsKey("repeat"), "repeat not exists.");
        this.entityId = (String) data.get("entityId");
        this.property = (String) data.get("property");
        this.time = ((Date) data.get("time")).toInstant().getEpochSecond();
        this.repeat = (Integer) data.get("repeat");
        this.type = RequestType.VERTEX_NEIGHBORING_EDGES_MAX_QUERY;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getProperty() {
        return property;
    }

    public long getTime() {
        return time;
    }

    public int getRepeat() {
        return repeat;
    }
}
