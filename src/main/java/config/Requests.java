package config;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.*;
import request.*;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

class RequestsConstructor extends Constructor {


    public RequestsConstructor(Class<?> theRoot) {
        super(theRoot);
        this.yamlConstructors.put(new Tag("!CurrentTime"), new CurrentDateConstruct());
        this.yamlConstructors.put(new Tag("!ImportStructureAndStaticProperty"), new ImportStructureAndStaticPropertyConstruct());
        this.yamlConstructors.put(new Tag("!ImportTemporalProperty"), new ImportTemporalPropertyConstruct());
        this.yamlConstructors.put(new Tag("!UpdateProperty"), new UpdatePropertyConstruct());
        this.yamlConstructors.put(new Tag("!TimePointQuery"), new TimePointQueryConstruct());
        this.yamlConstructors.put(new Tag("!TimeRangeMaxQuery"), new TimeRangeMaxQueryConstruct());
        this.yamlConstructors.put(new Tag("!TimePointMultiDegreeQuery"), new TimePointMultiDegreeQueryConstruct());
        this.yamlConstructors.put(new Tag("!VertexNeighboringEdgesMaxQuery"), new VertexNeighboringEdgesMaxQueryConstruct());
    }

    private class CurrentDateConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            String val = constructScalar((ScalarNode) node);
            checkState("Now".equals(val), "!CurrentTime must receive Now as argument.");
            return new Date();
        }
    }

    private class ImportStructureAndStaticPropertyConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("ImportStructureAndStaticProperty")) {
                return new ImportStructureAndStaticProperty(val);
            }
            return null;
        }
    }

    private class ImportTemporalPropertyConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("ImportTemporalProperty")) {
                return new ImportTemporalProperty(val);
            }
            return null;
        }
    }

    private class UpdatePropertyConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("UpdateProperty")) {
                return new UpdateProperty(val);
            }
            return null;
        }
    }

    private class TimePointQueryConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("TimePointQuery")) {
                return new TimePointQuery(val);
            }
            return null;
        }
    }

    private class TimeRangeMaxQueryConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("TimeRangeMaxQuery")) {
                return new TimeRangeMaxQuery(val);
            }
            return null;
        }
    }

    private class TimePointMultiDegreeQueryConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("TimePointMultiDegreeQuery")) {
                return new TimePointMultiDegreeQuery(val);
            }
            return null;
        }
    }

    private class VertexNeighboringEdgesMaxQueryConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            var val = constructMapping((MappingNode) node);
            checkState(val.containsKey("txn"));
            var txn = val.get("txn");
            if (txn.equals("VertexNeighboringEdgesMaxQuery")) {
                return new VertexNeighboringEdgesMaxQuery(val);
            }
            return null;
        }
    }
}

public class Requests {

    private Date executeDate;
    private String description;

    private List<AbstractRequest> requests;

    public List<AbstractRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<AbstractRequest> requests) {
        this.requests = requests;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Requests load(String configFile) {
        var is = Requests.class.getClassLoader().getResourceAsStream(configFile);
        checkNotNull(is, "Requests config file is not exists.");
        var yaml = new Yaml(new RequestsConstructor(Requests.class));
        return yaml.load(is);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        var is = Requests.class.getClassLoader().getResourceAsStream("requests.yaml");
        var yaml = new Yaml(new RequestsConstructor(Requests.class));
        Requests data = yaml.load(is);
        System.out.println(data.getExecuteDate());
        System.out.println(data.getDescription());
        for (var req : data.getRequests()) {
            System.out.println(req.getType());
            switch (req.getType()) {
                case IMPORT_STRUCTURE_AND_STATIC_PROPERTY:
                    var sta = (ImportStructureAndStaticProperty) req;
                    System.out.println(sta.getBatch());
                    break;
                case IMPORT_TEMPORAL_PROPERTY:
                    var temporal = (ImportTemporalProperty) req;
                    System.out.println(temporal.getBatch());
                    System.out.println(temporal.getStartTime());
                    System.out.println(temporal.getEndTime());
                    break;
                case UPDATE_PROPERTY:
                    var update = (UpdateProperty) req;
                    System.out.println(update.getEntityId());
                    System.out.println(update.getTime());
                    break;
                case TIME_POINT_QUERY:
                    var tp = (TimePointQuery) req;
                    System.out.println(tp.getTime());
                    break;
                case TIME_RANGE_MAX_QUERY:
                    var r = (TimeRangeMaxQuery) req;
                    System.out.println(r.getEndTime());
                    break;
                case TIME_POINT_MULTI_DEGREE_QUERY:
                    var d = (TimePointMultiDegreeQuery) req;
                    System.out.println(d.getDegree());
                    break;
                case VERTEX_NEIGHBORING_EDGES_MAX_QUERY:
                    var v = (VertexNeighboringEdgesMaxQuery) req;
                    System.out.println(v.getTime());
                    break;
                default:
                    System.out.println("do not support type.");
            }
        }
        System.out.println(data.getClass().getName());
    }
}
