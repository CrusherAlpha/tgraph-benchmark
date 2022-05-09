package request;

import config.RequestType;
import config.ValueType;
import dataset.EntityType;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class UpdateProperty extends AbstractRequest {
    private final String property;
    private final long time;
    private final ValueType valueType;
    private final Object value;
    private final EntityType entityType;
    private final String entityId;

    public UpdateProperty(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("property"), "property not exists.");
        checkState(data.containsKey("time"), "time not exists.");
        checkState(data.containsKey("valueType"), "valueType not exists.");
        checkState(data.containsKey("value"), "value not exists.");
        checkState(data.containsKey("entityType"), "entityType not exists.");
        checkState(data.containsKey("entityId"), "entityId not exists.");
        this.entityId = (String) data.get("entityId");
        this.property = (String) data.get("property");
        this.valueType = Util.String2ValueType((String) data.get("valueType"));
        this.value = data.get("value");
        this.entityType = Util.String2EntityType((String) data.get("entityType"));
        this.time = ((Date) data.get("time")).toInstant().getEpochSecond();
        this.type = RequestType.UPDATE_PROPERTY;
    }

    public String getProperty() {
        return property;
    }

    public long getTime() {
        return time;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Object getValue() {
        return value;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }
}
