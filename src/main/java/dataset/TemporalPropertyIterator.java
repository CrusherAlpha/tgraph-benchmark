package dataset;

import com.opencsv.CSVReader;
import common.Pair;

import java.util.*;
import java.util.logging.Logger;


// We preprocess every dataset, thus we can assume that datasets are all in right form.
// Temporal data come from a separate csv, thus for vertex or edge, we only record entity id.
// We read through String array, but in some circumstances, the property value may not be
// string or even can not be represented by string, we can use Base64 to solve it.
// In benchmark, we simplify it and assume that property can be represented by string.
// See unit test to get more example.
// The implementation of CSVReader use BufferedReader, meaning that we do not need to
// add additional buffer.

// When startUnixTimestamp == -1 and endUnixTimestamp == -1, we fetch all the data items.
public class TemporalPropertyIterator implements Iterator<Optional<PropertyItem>>, AutoCloseable {
    private final CSVReader v;
    private final CSVReader e;
    private Iterator<String[]> vertex = null;
    private Iterator<String[]> edge = null;
    private final int startUnixTimestamp;
    private final int endUnixTimestamp;
    private String[] vertexHeader = null;
    private String[] edgeHeader = null;
    private String[] currentV = null;
    private String[] currentE = null;
    private final static Logger log = Logger.getLogger(TemporalPropertyIterator.class.getName());

    // If user pass null vertex and null edge, the value may be null, thus we use optional.
    private TemporalPropertyIterator(CSVReader vertex, CSVReader edge, int startUnixTimestamp, int endUnixTimestamp) {
        this.v = vertex;
        this.e = edge;
        if (vertex != null) {
            this.vertex = vertex.iterator();
            if (this.vertex.hasNext()) vertexHeader = this.vertex.next();
        }
        if (edge != null) {
            this.edge = edge.iterator();
            if (this.edge.hasNext()) edgeHeader = this.edge.next();
        }
        this.startUnixTimestamp = startUnixTimestamp;
        this.endUnixTimestamp = endUnixTimestamp;
    }

    public static TemporalPropertyIterator of(CSVReader vertex, CSVReader edge, int startUnixTimestamp, int endUnixTimestamp) {
        return new TemporalPropertyIterator(vertex, edge, startUnixTimestamp, endUnixTimestamp);
    }

    public static TemporalPropertyIterator of(CSVReader vertex, CSVReader edge) {
        return new TemporalPropertyIterator(vertex, edge, -1, -1);
    }

    // call this function after iterator is constructed.
    public Pair<Optional<String[]>, Optional<String[]>> getHeader() {
        return Pair.of(Optional.of(vertexHeader), Optional.of(edgeHeader));
    }

    private boolean satisfy(String[] val) {
        if (startUnixTimestamp == -1 && endUnixTimestamp == -1) return true;
        var timestamp = Integer.parseInt(val[0]);
        return startUnixTimestamp <= timestamp && endUnixTimestamp > timestamp;
    }

    private PropertyItem parseEntity(String[] current, EntityType type) {
        List<String> list = new ArrayList<>(Arrays.asList(current));
        return type == EntityType.VERTEX ? new VertexPropertyItem(list) : new EdgePropertyItem(list);
    }

    private PropertyItem parseVertex() {
        var ret = parseEntity(currentV, EntityType.VERTEX);
        currentV = null;
        return ret;
    }

    private PropertyItem parseEdge() {
        var ret = parseEntity(currentE, EntityType.EDGE);
        currentE = null;
        return ret;
    }

    private Optional<String[]> seekToNext(Iterator<String[]> iter) {
        while (iter != null && iter.hasNext()) {
            var current = iter.next();
            // at least one temporal property.
            // timestamp, id, tp1
            assert current.length >= 3;
            if (satisfy(current)) return Optional.of(current);
        }
        return Optional.empty();
    }

    @Override
    public boolean hasNext() {
        if (currentV != null || currentE != null) return true;
        var v = seekToNext(vertex);
        currentV = v.orElse(null);
        if (v.isPresent()) return true;
        var e = seekToNext(edge);
        currentE = e.orElse(null);
        return e.isPresent();
    }

    @Override
    public Optional<PropertyItem> next() {
        if (hasNext()) {
            if (currentV != null) return Optional.of(parseVertex());
            if (currentE != null) return Optional.of(parseEdge());
        }
        // the usage convention of iterator indicates that we don't come here.
        log.warning("Forget to call hasNext first, will return Optional.empty()");
        return Optional.empty();
    }

    @Override
    public void close() throws Exception {
        if (v != null) v.close();
        if (e != null) e.close();
    }
}
