package dataset;

import java.util.List;

public class EdgePropertyItem extends PropertyItem {
    public EdgePropertyItem(List<String> value) {
        super(value);
        this.type = EntityType.EDGE;
    }
}
