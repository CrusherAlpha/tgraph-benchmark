package request;

import config.ValueType;
import dataset.EntityType;

public class Util {
    public static ValueType String2ValueType(String type) {
        switch (type) {
            case "INT":
                return ValueType.INT;
            case "DOUBLE":
                return ValueType.DOUBLE;
            case "STRING":
                return ValueType.STRING;
            default:
                return ValueType.UNKNOWN;
        }
    }

    public static EntityType String2EntityType(String type) {
        switch (type) {
            case "VERTEX":
                return EntityType.VERTEX;
            case "EDGE":
                return EntityType.EDGE;
            default:
                return EntityType.UNKNOWN;
        }
    }
}
