package config;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.time.Instant;

class TsConstructor extends Constructor {
    public TsConstructor() {
        this.yamlConstructors.put(new Tag("yaml.org,2002:timestamp"),
                new ConstructTimestamp());
    }

    private class ConstructTimestamp extends AbstractConstruct {
        public Object construct(Node node) {
            String val = (String) constructScalar((ScalarNode) node);
            if ("now".equals(val)) {
                return Instant.now();
            } else {
                return Instant.parse(val);
            }
        }
    }
}

public class Requests {
    public static void main(String[] args) {
        var is = Requests.class.getClassLoader().getResourceAsStream("requests.yaml");
        var yaml = new Yaml(new TsConstructor());
        var data = yaml.load(is);
        System.out.println(data);
    }
}
