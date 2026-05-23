package oulad.analytics;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityAnalyzerTest {

  @Test
  void testAnalyze() throws IOException {

    Path dir = Files.createTempDirectory("data");
    Path out = dir.resolve("output");
    Files.createDirectories(out);

    Path f = out.resolve("AAA_2013J.csv");

    BufferedWriter writer = Files.newBufferedWriter(f);
    writer.write("date,total_clicks\n");
    writer.write("-10,5000\n");
    writer.write("-9,12000\n");
    writer.write("-8,15000\n");
    writer.close();

    ActivityAnalyzer a = new ActivityAnalyzer();
    a.analyze(dir.toString(), 10000);

    Path result = dir.resolve("activity-10000.csv");

    boolean exists = Files.exists(result);
    assertTrue(exists);

    List<String> data = Files.readAllLines(result);

    int size = data.size();
    assertEquals(3, size);

    String header = data.get(0);
    assertEquals("module_presentation,date,total_clicks", header);

    boolean found1 = data.contains("AAA_2013J,-9,12000");
    boolean found2 = data.contains("AAA_2013J,-8,15000");

    assertTrue(found1);
    assertTrue(found2);
  }

  @Test
  void testAnalyzeEdgeCases() throws IOException {

    Path dir = Files.createTempDirectory("data");
    Path out = dir.resolve("output");
    Files.createDirectories(out);

    Path file1 = out.resolve("AAA_2013J.csv");
    BufferedWriter w1 = Files.newBufferedWriter(file1);
    w1.write("date,total_clicks\n");
    w1.write("bad_line\n");
    w1.close();

    Path file2 = out.resolve("ignore.txt");
    BufferedWriter w2 = Files.newBufferedWriter(file2);
    w2.write("some random text\n");
    w2.close();

    ActivityAnalyzer a = new ActivityAnalyzer();
    a.analyze(dir.toString(), 100);

    Path result = dir.resolve("activity-100.csv");
    boolean exists = Files.exists(result);
    assertTrue(exists);

    List<String> data = Files.readAllLines(result);
    assertEquals(1, data.size());
  }

}