package dataset;

import java.util.List;

public class VertexPropertyItem extends PropertyItem {
    public VertexPropertyItem(List<String> value) {
        super(value);
        this.type = EntityType.VERTEX;
    }
}
