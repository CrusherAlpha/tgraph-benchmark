package config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class Env {
    private String datasetDirectory;
    private String datasetName;
    private String databaseDirectory;
    private String databaseName;
    private String outputDirectory;

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
        // remove ide complains.
        env.setDatabaseDirectory("");
        env.setDatabaseName("");
        env.setDatasetDirectory("");
        env.setDatasetName("");
        env.setOutputDirectory("");
    }
}
