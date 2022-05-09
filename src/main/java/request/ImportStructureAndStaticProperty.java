package request;

import config.RequestType;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class ImportStructureAndStaticProperty extends AbstractRequest {
    private final int batch;

    // Used for yaml parsed.
    public ImportStructureAndStaticProperty(Map<Object, Object> data) {
        checkNotNull(data);
        checkState(data.containsKey("batch"), "batch not exists.");
        this.batch = (Integer) data.get("batch");
        this.type = RequestType.IMPORT_STRUCTURE_AND_STATIC_PROPERTY;
    }

    public int getBatch() {
        return batch;
    }

    public static void main(String[] args) {
        var mp = new HashMap<>();
        var it = new ImportStructureAndStaticProperty(mp);
        System.out.println(it.getBatch());
        System.out.println(it.batch);
    }
}
