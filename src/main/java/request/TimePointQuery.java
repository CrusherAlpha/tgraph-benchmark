package request;

import config.RequestType;
import dataset.EntityType;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TimePointQuery extends AbstractRequest {
    private final EntityType entityType;
    private final String entityId;
    private final String property;
    private final long time;
    private final int repeat;

    public TimePointQuery(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("entityType"), "entityType not exists.");
        checkState(data.containsKey("entityId"), "entityId not exists.");
        checkState(data.containsKey("property"), "property name not exists.");
        checkState(data.containsKey("time"), "time not exists.");
        checkState(data.containsKey("repeat"), "repeat not exists.");
        this.entityType = Util.String2EntityType((String) data.get("entityType"));
        this.entityId = (String) data.get("entityId");
        this.property = (String) data.get("property");
        this.time = ((Date) data.get("time")).toInstant().getEpochSecond();
        this.repeat = (Integer) data.get("repeat");
        this.type = RequestType.TIME_POINT_QUERY;
    }

    public EntityType getEntityType() {
        return entityType;
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
