package request;

import config.RequestType;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TimePointMultiDegreeQuery extends AbstractRequest {
    private final String entityId;
    private final int degree;
    private final String property;
    private final long time;
    private final int repeat;

    public TimePointMultiDegreeQuery(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("entityId"), "entityId not exists.");
        checkState(data.containsKey("degree"), "degree not exists.");
        checkState(data.containsKey("property"), "property name not exists.");
        checkState(data.containsKey("time"), "time not exists.");
        checkState(data.containsKey("repeat"), "repeat not exists.");
        this.entityId = (String) data.get("entityId");
        this.degree = (Integer) data.get("degree");
        this.property = (String) data.get("property");
        this.time = ((Date) data.get("time")).toInstant().getEpochSecond();
        this.repeat = (Integer) data.get("repeat");
        this.type = RequestType.TIME_POINT_MULTI_DEGREE_QUERY;
    }

    public String getEntityId() {
        return entityId;
    }

    public int getDegree() {
        return degree;
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
