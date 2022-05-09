package request;

import config.RequestType;
import dataset.EntityType;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TimeRangeMaxQuery extends AbstractRequest {
    private final EntityType entityType;
    private final String entityId;
    private final String property;
    private final long startTime;
    private final long endTime;
    private final int repeat;

    public TimeRangeMaxQuery(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("entityType"), "entityType not exists.");
        checkState(data.containsKey("entityId"), "entityId not exists.");
        checkState(data.containsKey("property"), "property name not exists.");
        checkState(data.containsKey("startTime"), "startTime not exists.");
        checkState(data.containsKey("endTime"), "endTime not exists.");
        checkState(data.containsKey("repeat"), "repeat not exists.");
        this.entityType = Util.String2EntityType((String) data.get("entityType"));
        this.entityId = (String) data.get("entityId");
        this.property = (String) data.get("property");
        this.startTime = ((Date) data.get("startTime")).toInstant().getEpochSecond();
        this.endTime = ((Date) data.get("endTime")).toInstant().getEpochSecond();
        checkState(startTime < endTime, "time range is illegal, make sure startTime is small that endTime");
        this.repeat = (Integer) data.get("repeat");
        this.type = RequestType.TIME_RANGE_MAX_QUERY;
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

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getRepeat() {
        return repeat;
    }
}
