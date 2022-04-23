import com.opencsv.CSVReader;
import common.Pair;
import dataset.*;
import dataset.EntityType;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FakeDataSet {
    private final String testDir = "/tmp/";
    private final String vertexSFile = "vertex.csv";
    private final String edgeSFile = "edge.csv";
    private final String vertexTFile = "vertex_temporal.csv";
    private final String edgeTFile = "edge_temporal.csv";
    private final Pair<List<String>, List<String>> headerS;
    private final Pair<List<String>, List<String>> headerT;
    private final List<PropertyItem> vertexSItems = new ArrayList<>();
    private final List<PropertyItem> edgeSItems = new ArrayList<>();
    private final List<PropertyItem> vertexTItems = new ArrayList<>();
    private final List<PropertyItem> edgeTItems = new ArrayList<>();
    private boolean staticReady = false;
    private boolean temporalReady = false;
    // we simplify this test to make all entity counts be 10.
    private final int count = 10;

    public FakeDataSet() {
        this.headerS = Pair.of(List.of("vid", "sp1", "sp2", "sp3"),
                List.of("eid", "sid", "eid", "sp1", "sp2"));
        this.headerT = Pair.of(
                List.of("unix_timestamp", "vid", "tp1", "tp2"),
                List.of("unix_timestamp", "eid", "tp1"));
    }

    private static void appendStringToFile(Path path, String message) {
        try {
            Files.writeString(path, message, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void prepareFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeDisk(Path path, List<String> header, List<PropertyItem> data) {
        prepareFile(path);
        appendStringToFile(path, String.join(",", header) + "\n");
        data.forEach(item -> appendStringToFile(path, String.join(",", item.getValue()) + "\n"));
    }

    private void prepareStatic() {
        // vertex.
        for (int i = 0; i < count; ++i) {
            ArrayList<String> list = new ArrayList<>();
            list.add(String.valueOf(i));
            list.add("hello" + i);
            list.add(String.valueOf(i));
            list.add(String.valueOf(1.5 * i));
            vertexSItems.add(new VertexPropertyItem(list));
        }
        // edge.
        for (int i = 0; i < count; ++i) {
            ArrayList<String> list = new ArrayList<>();
            list.add(String.valueOf(i));
            list.add(String.valueOf(i * 3));
            list.add(String.valueOf(i * 4));
            list.add("hello" + i);
            list.add(String.valueOf(i));
            edgeSItems.add(new EdgePropertyItem(list));
        }
        writeDisk(Paths.get(testDir, vertexSFile), headerS.first(), vertexSItems);
        writeDisk(Paths.get(testDir, edgeSFile), headerS.second(), edgeSItems);
        staticReady = true;
    }

    private void prepareTemporal() {
        // vertex.
        for (int i = 0; i < count; ++i) {
            ArrayList<String> list = new ArrayList<>();
            list.add(String.valueOf(i));
            list.add(String.valueOf(i));
            list.add("crusher" + i);
            list.add("alpha" + i);
            vertexTItems.add(new VertexPropertyItem(list));
        }
        // edge.
        for (int i = 0; i < count; ++i) {
            ArrayList<String> list = new ArrayList<>();
            list.add(String.valueOf(i));
            list.add(String.valueOf(i));
            list.add("crusher" + i);
            edgeTItems.add(new EdgePropertyItem(list));
        }
        writeDisk(Paths.get(testDir, vertexTFile), headerT.first(), vertexTItems);
        writeDisk(Paths.get(testDir, edgeTFile), headerT.second(), edgeTItems);
        temporalReady = true;
    }

    public Pair<List<String>, List<String>> getStaticHeader() {
        return headerS;
    }

    public Pair<List<String>, List<String>> getTemporalHeader() {
        return headerT;
    }

    public Pair<List<PropertyItem>, List<PropertyItem>> getStaticData() {
        if (!staticReady) prepareStatic();
        return Pair.of(vertexSItems, edgeSItems);
    }

    public Pair<List<PropertyItem>, List<PropertyItem>> getTemporalData() {
        if (!temporalReady) prepareTemporal();
        return Pair.of(vertexTItems, edgeTItems);
    }

    public Pair<String, String> getStaticFile() {
        if (!staticReady) prepareStatic();
        return Pair.of(testDir + vertexSFile, testDir + edgeSFile);
    }

    public Pair<String, String> getTemporalFile() {
        if (!temporalReady) prepareTemporal();
        return Pair.of(testDir + vertexTFile, testDir + edgeTFile);
    }

    public int getCount() {
        return count;
    }

}

public class PropertyIteratorTest {

    private final FakeDataSet dataSet = new FakeDataSet();

    private static CSVReader getReader(String filePath) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath));
        } catch (FileNotFoundException ignored) {
        }
        return reader;
    }

    @Test
    void testStaticPropertyIteratorHeader() {
        var expected = dataSet.getStaticHeader();
        var files = dataSet.getStaticFile();
        var iter = StaticPropertyIterator.of(getReader(files.first()),
                getReader(files.second()));
        var actual = iter.getHeader();
        assertTrue(actual.first().isPresent());
        assertTrue(actual.second().isPresent());
        var actualV = actual.first().get();
        var actualE = actual.second().get();
        var expectedV = expected.first();
        var expectedE = expected.second();
        assertIterableEquals(expectedV, Arrays.asList(actualV));
        assertIterableEquals(expectedE, Arrays.asList(actualE));
    }

    @Test
    void testStaticPropertyIteratorBase() {
        var files = dataSet.getStaticFile();
        var iter = StaticPropertyIterator.of(getReader(files.first()),
                getReader(files.second()));
        int actualTot = 0;
        while (iter.hasNext()) {
            iter.next();
            ++actualTot;
        }
        int expected = dataSet.getCount() * 2;
        assertEquals(expected, actualTot);
        assertFalse(iter.hasNext());
        assertTrue(iter.next().isEmpty());
    }

    @Test
    void testStaticPropertyIteratorNull() {
        // both null.
        var iter = StaticPropertyIterator.of(null, null);
        assertFalse(iter.hasNext());
        assertTrue(iter.next().isEmpty());
        // edge null.
        var files = dataSet.getStaticFile();
        iter = StaticPropertyIterator.of(getReader(files.first()), null);
        assertTrue(iter.hasNext());
        int actualTot = 0, expected = dataSet.getCount();
        while (iter.hasNext()) {
            iter.next();
            ++actualTot;
        }
        assertEquals(expected, actualTot);
        // vertex null.
        iter = StaticPropertyIterator.of(null, getReader(files.second()));
        assertTrue(iter.hasNext());
        actualTot = 0;
        while (iter.hasNext()) {
            iter.next();
            ++actualTot;
        }
        assertEquals(expected, actualTot);
    }

    @Test
    void testStaticPropertyIteratorMore() {
        var files = dataSet.getStaticFile();
        var iter = StaticPropertyIterator.of(getReader(files.first()),
                getReader(files.second()));
        var expected = dataSet.getStaticData();
        var expectedV = expected.first();
        var expectedE = expected.second();
        int count = dataSet.getCount();
        // vertex first, then edge.
        int o = 0;
        while (iter.hasNext()) {
            var actual = iter.next();
            assertTrue(actual.isPresent());
            var actualD = actual.get();
            if (o >= count) {
                assertEquals(EntityType.EDGE, actualD.getType());
                assertIterableEquals(expectedE.get(o - count).getValue(), actualD.getValue());
            } else {
                assertEquals(EntityType.VERTEX, actualD.getType());
                assertIterableEquals(expectedV.get(o).getValue(), actualD.getValue());
            }
            ++o;
        }
    }

    @Test
    void testTemporalPropertyIteratorHeader() {
        var expected = dataSet.getTemporalHeader();
        var files = dataSet.getTemporalFile();
        var iter = TemporalPropertyIterator.of(getReader(files.first()),
                getReader(files.second()));
        var actual = iter.getHeader();
        assertTrue(actual.first().isPresent());
        assertTrue(actual.second().isPresent());
        var actualV = actual.first().get();
        var actualE = actual.second().get();
        var expectedV = expected.first();
        var expectedE = expected.second();
        assertIterableEquals(expectedV, Arrays.asList(actualV));
        assertIterableEquals(expectedE, Arrays.asList(actualE));
    }

    @Test
    void testTemporalPropertyIteratorBase() {
        var files = dataSet.getTemporalFile();
        var iter = TemporalPropertyIterator.of(getReader(files.first()),
                getReader(files.second()));
        int actualTot = 0;
        while (iter.hasNext()) {
            iter.next();
            ++actualTot;
        }
        int expected = dataSet.getCount() * 2;
        assertEquals(expected, actualTot);
        assertFalse(iter.hasNext());
        assertTrue(iter.next().isEmpty());
    }

    @Test
    void testTemporalPropertyIteratorNull() {
        // both null.
        var iter = TemporalPropertyIterator.of(null, null);
        assertFalse(iter.hasNext());
        assertTrue(iter.next().isEmpty());
        // edge null.
        var files = dataSet.getTemporalFile();
        iter = TemporalPropertyIterator.of(getReader(files.first()), null);
        assertTrue(iter.hasNext());
        int actualTot = 0, expected = dataSet.getCount();
        while (iter.hasNext()) {
            iter.next();
            ++actualTot;
        }
        assertEquals(expected, actualTot);
        // vertex null.
        iter = TemporalPropertyIterator.of(null, getReader(files.second()));
        assertTrue(iter.hasNext());
        actualTot = 0;
        while (iter.hasNext()) {
            iter.next();
            ++actualTot;
        }
        assertEquals(expected, actualTot);
    }

    @Test
    void testTemporalPropertyIteratorMore() {
        var files = dataSet.getTemporalFile();
        var iter = TemporalPropertyIterator.of(getReader(files.first()),
                getReader(files.second()));
        var expected = dataSet.getTemporalData();
        var expectedV = expected.first();
        var expectedE = expected.second();
        int count = dataSet.getCount();
        // vertex first, then edge.
        int o = 0;
        while (iter.hasNext()) {
            var actual = iter.next();
            assertTrue(actual.isPresent());
            var actualD = actual.get();
            if (o >= count) {
                assertEquals(EntityType.EDGE, actualD.getType());
                assertIterableEquals(expectedE.get(o - count).getValue(), actualD.getValue());
            } else {
                assertEquals(EntityType.VERTEX, actualD.getType());
                assertIterableEquals(expectedV.get(o).getValue(), actualD.getValue());
            }
            ++o;
        }
    }

    @Test
    void testTemporalPropertyIteratorTimeFilter() {
        var files = dataSet.getTemporalFile();
        int start = 2, end = 6;
        var iter = TemporalPropertyIterator.of(getReader(files.first()),
                getReader(files.second()), start, end);
        var expected = dataSet.getTemporalData();
        var expectedV = expected.first();
        var expectedE = expected.second();
        var expectedVCount = expectedV.stream().filter(item -> {
            var list = item.getValue();
            var time = Integer.parseInt(list.get(0));
            return time >= start && time < end;
        }).count();
        var expectedECount = expectedE.stream().filter(item -> {
            var list = item.getValue();
            var time = Integer.parseInt(list.get(0));
            return time >= start && time < end;
        }).count();
        // vertex first, then edge.
        long actualVCount = 0, actualECount = 0;
        while (iter.hasNext()) {
            var actual = iter.next();
            assertTrue(actual.isPresent());
            var actualD = actual.get();
            var actualTime = Integer.parseInt(actualD.getValue().get(0));
            assertTrue(actualTime >= start && actualTime < end);
            var type = actualD.getType();
            switch (type) {
                case VERTEX:
                    ++actualVCount;
                    break;
                case EDGE:
                    ++actualECount;
                    break;
                default:
                    fail("Unknown type, Entity must be Vertex or Edge.");
            }
        }
        assertEquals(expectedVCount, actualVCount);
        assertEquals(expectedECount, actualECount);
    }
}
