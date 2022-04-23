package dataset;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

// We use PropertyItem to import data, we don't care
// whether the property is static or temporal, but
// we do need to judge the if the type is vertex or edge.

// the static property is followed by graph structure.
// the layout of static property is like this:
// for vertex:
// vertex_id, sp1, sp2, ..., sp3
// 1, "hello", "world", ..., 5
// for edge:
// edge_id, sv_id, ev_id, sp1, sp2, ..., sp3
// 1, 5, 6, "hello", "world", ..., 18.0

// the layout of temporal property is like this:
// unix_timestamp, entity_id, tp1, tp2, ..., tp3
// 1650320924, 1, "hello", "world", ..., 5

// We read through String array, but in some circumstances, the property value may not be
// string or even can not be represented by string, we can use Base64 to solve it.
// In benchmark, we simplify it and assume that property can be represented by string.
public class PropertyItem {
    protected EntityType type;
    private final List<String> value;

    public PropertyItem(List<String> value) {
        checkNotNull(value, "PropertyItem should not be null.");
        this.value = value;
        this.type = EntityType.UNKNOWN;
    }

    public EntityType getType() {
        return type;
    }

    public List<String> getValue() {
        return value;
    }
}
