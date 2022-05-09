package request;

import config.RequestType;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ImportTemporalProperty extends AbstractRequest {
    private final boolean all;
    private final long startTime;
    private final long endTime;
    private final int batch;

    public ImportTemporalProperty(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("batch"), "batch not exists.");
        batch = (Integer) data.get("batch");
        checkState(data.containsKey("all"), "all not exists.");
        all = (Boolean) data.get("all");
        if (all) {
            startTime = -1;
            endTime = -1;
        } else {
            checkState(data.containsKey("startTime"), "all is false and startTime does not exist.");
            checkState(data.containsKey("endTime"), "all is false and endTime does not exist.");
            var start = ((Date) data.get("startTime")).toInstant().getEpochSecond();
            var end = ((Date) data.get("endTime")).toInstant().getEpochSecond();
            checkState(start < end, "time range is illegal.");
            startTime = start;
            endTime = end;
        }
        type = RequestType.IMPORT_TEMPORAL_PROPERTY;
    }

    public boolean isAll() {
        return all;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getBatch() {
        return batch;
    }
}
