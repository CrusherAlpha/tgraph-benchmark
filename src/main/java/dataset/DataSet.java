package dataset;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;


public class DataSet {
    private final String name;
    private final Path path;
    private final static Logger log = Logger.getLogger(DataSet.class.getName());

    private DataSet(String datasetPath, String datasetName) {
        this.path = Paths.get(datasetPath);
        this.name = datasetName;
        checkState(Files.exists(this.path), "Dataset " + name + " does not exist.");
    }

    public static DataSet of(String datasetPath, String datasetName) {
        return new DataSet(datasetPath, datasetName);
    }

    private CSVReader getReader(String filePath, String info) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath));
        } catch (FileNotFoundException ignored) {
            log.info(info);
        }
        return reader;
    }

    public StaticPropertyIterator staticData() {
        CSVReader vertex;
        CSVReader edge;
        checkState(Files.exists(Paths.get(path + "vertex.csv")), "Dataset must have vertex data.");
        vertex = getReader(path + "vertex.csv", "Dataset " + name + " does not exist vertex data.");
        edge = getReader(path + "edge.csv", "Dataset " + name + " does not exist edge data.");
        return StaticPropertyIterator.of(vertex, edge);
    }

    private TemporalPropertyIterator generateTemporalPropertyIterator(int startUnixTimestamp, int endUnixTimestamp) {
        checkArgument((startUnixTimestamp < endUnixTimestamp && startUnixTimestamp >= 0) ||
                        (startUnixTimestamp == -1 && endUnixTimestamp == -1),
                "Time range is illegal.");
        CSVReader vertex;
        CSVReader edge;
        vertex = getReader(path + "vertex_temporal_data.csv", "Dataset " + name + " does not exist vertex temporal data.");
        edge = getReader(path + "edge_temporal_data.csv", "Dataset " + name + " does not exist edge temporal data.");
        return TemporalPropertyIterator.of(vertex, edge, startUnixTimestamp, endUnixTimestamp);
    }

    // return all the data.
    public TemporalPropertyIterator temporalData() {
        return generateTemporalPropertyIterator(-1, -1);
    }

    // return the data between [start, end)
    public TemporalPropertyIterator temporalData(int startUnixTimestamp, int endUnixTimestamp) {
        return generateTemporalPropertyIterator(startUnixTimestamp, endUnixTimestamp);
    }
}
