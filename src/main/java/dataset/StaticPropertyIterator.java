package dataset;

import com.opencsv.CSVReader;
import common.Pair;

import java.util.*;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;


// We preprocess every dataset, thus we can assume that datasets are all in right form.
// Dataset(like generation) may only have graph structure and do not have static property.
// We read through String array, but in some circumstances, the property value may not be
// string or even can not be represented by string, we can use Base64 to solve it.
// In benchmark, we simplify it and assume that property can be represented by string.
// See unit test to get more example.
// The implementation of CSVReader use BufferedReader, meaning that we do not need to
// add additional buffer.

// If user pass null vertex and null edge, the value may be null, thus we use optional.
public class StaticPropertyIterator implements Iterator<Optional<PropertyItem>>, AutoCloseable {

    private final CSVReader v;
    private final CSVReader e;
    private Iterator<String[]> vertex = null;
    private Iterator<String[]> edge = null;
    private String[] vertexHeader = null;
    private String[] edgeHeader = null;
    private final static Logger log = Logger.getLogger(StaticPropertyIterator.class.getName());

    private StaticPropertyIterator(CSVReader vertex, CSVReader edge) {
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
    }

    public static StaticPropertyIterator of(CSVReader vertex, CSVReader edge) {
        return new StaticPropertyIterator(vertex, edge);
    }

    // call this function after iterator is constructed.
    public Pair<Optional<String[]>, Optional<String[]>> getHeader() {
        return Pair.of(Optional.of(vertexHeader), Optional.of(edgeHeader));
    }

    private VertexPropertyItem parseVertex() {
        var val = vertex.next();
        checkState(val.length >= 1, "Vertex needs vertex id.");
        List<String> list = new ArrayList<>(Arrays.asList(val));
        return new VertexPropertyItem(list);
    }

    private EdgePropertyItem parseEdge() {
        var val = edge.next();
        checkState(val.length >= 3, "Edge needs edge id, start and end vertex id.");
        List<String> list = new ArrayList<>(Arrays.asList(val));
        return new EdgePropertyItem(list);
    }

    @Override
    public boolean hasNext() {
        return (vertex != null && vertex.hasNext()) ||
                (edge != null && edge.hasNext());
    }

    @Override
    public Optional<PropertyItem> next() {
        // parse vertex first.
        if (vertex != null && vertex.hasNext()) return Optional.of(parseVertex());
        if (edge != null && edge.hasNext()) return Optional.of(parseEdge());
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
