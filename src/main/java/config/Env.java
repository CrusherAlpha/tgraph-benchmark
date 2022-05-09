package config;

import common.Pair;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import request.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

// store the result of parsed env_example.yaml.
public class Env {
    private String datasetDirectory;
    private String datasetName;
    private String databaseDirectory;
    private String databaseName;
    private String outputDirectory;
    private Map<String, Map<String, String>> schema;
    private String model;

    public String getDatasetDirectory() {
        return datasetDirectory;
    }

    public void setDatasetDirectory(String datasetDirectory) {
        this.datasetDirectory = datasetDirectory;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatabaseDirectory() {
        return databaseDirectory;
    }

    public void setDatabaseDirectory(String databaseDirectory) {
        this.databaseDirectory = databaseDirectory;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Map<String, Map<String, String>> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, Map<String, String>> schema) {
        this.schema = schema;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Model Model() {
        switch (model) {
            case "RELATION":
                return Model.RELATION;
            case "GRAPH":
                return Model.GRAPH;
            case "TGRAPH":
                return Model.TGRAPH;
            default:
                return Model.UNKNOWN;
        }
    }

    private static List<Pair<String, ValueType>> getEntitySchema(Map<String, String> schema) {
        var ret = new ArrayList<Pair<String, ValueType>>();
        schema.forEach((first, second) -> {
            ValueType type = Util.String2ValueType(second);
            ret.add(Pair.of(first, type));
        });
        return ret;
    }

    public List<Pair<String, ValueType>> getVertexSchema() {
        checkState(schema.containsKey("vertex"), "schema does not contains vertex temporal property type info.");
        return getEntitySchema(schema.get("vertex"));
    }

    public List<Pair<String, ValueType>> getEdgeSchema() {
        checkState(schema.containsKey("edge"), "schema does not contains edge temporal property type info.");
        return getEntitySchema(schema.get("edge"));
    }

    public static Env load(String envFile) {
        var is = Env.class.getClassLoader().getResourceAsStream(envFile);
        checkNotNull(is, "Env config file is not exists.");
        var yaml = new Yaml(new Constructor(Env.class));
        return yaml.load(is);
    }


    public static void main(String[] args) {
        var env = Env.load("env.yaml");
        var logger = Logger.getLogger(Env.class.getName());
        logger.info(env.getDatasetDirectory());
        logger.info(env.getDatasetName());
        logger.info(env.getDatabaseDirectory());
        logger.info(env.getDatabaseName());
        logger.info(env.getOutputDirectory());
        System.out.println(env.getVertexSchema());
        System.out.println(env.getEdgeSchema());
        // remove ide complains.
        var schema = env.getSchema();
        System.out.println(schema.get("vertex"));
        env.setSchema(null);
        env.setDatabaseDirectory("");
        env.setDatabaseName("");
        env.setDatasetDirectory("");
        env.setDatasetName("");
        env.setOutputDirectory("");
        System.out.println(env.Model());
    }
}
