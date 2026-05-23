package oulad.concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


public class ResultWriterTest {

  @TempDir
  File tempDir;

  @Test
  public void testWriteAllCreatesCsvFiles() throws IOException {
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    ConcurrentMap<Integer, Long> aaa2013 = new ConcurrentHashMap<>();
    aaa2013.put(-10, 12L);
    aaa2013.put(5, 9L);
    clicksMap.put("AAA_2013J", aaa2013);

    ConcurrentMap<Integer, Long> bbb2013 = new ConcurrentHashMap<>();
    bbb2013.put(-5, 11L);
    clicksMap.put("BBB_2013J", bbb2013);

    String outputDir = tempDir.getAbsolutePath() + File.separator + "output";
    ResultWriter writer = new ResultWriter(outputDir);
    writer.writeAll(clicksMap);

    assertTrue(new File(outputDir, "AAA_2013J.csv").exists());
    assertTrue(new File(outputDir, "BBB_2013J.csv").exists());
  }

  @Test
  public void testWriteAllCorrectContent() throws IOException {
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    ConcurrentMap<Integer, Long> aaa2013 = new ConcurrentHashMap<>();
    aaa2013.put(-10, 12L);
    aaa2013.put(5, 9L);
    clicksMap.put("AAA_2013J", aaa2013);

    String outputDir = tempDir.getAbsolutePath() + File.separator + "output";
    ResultWriter writer = new ResultWriter(outputDir);
    writer.writeAll(clicksMap);

    File outputFile = new File(outputDir, "AAA_2013J.csv");
    try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
      assertEquals("date,total_clicks", reader.readLine());
      assertEquals("-10,12", reader.readLine());
      assertEquals("5,9", reader.readLine());
      assertNull(reader.readLine());
    }
  }

  @Test
  public void testWriteAllSortsByDate() throws IOException {
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    ConcurrentMap<Integer, Long> dateMap = new ConcurrentHashMap<>();
    dateMap.put(10, 100L);
    dateMap.put(-5, 50L);
    dateMap.put(0, 75L);
    clicksMap.put("CCC_2013J", dateMap);

    String outputDir = tempDir.getAbsolutePath() + File.separator + "output";
    ResultWriter writer = new ResultWriter(outputDir);
    writer.writeAll(clicksMap);

    File outputFile = new File(outputDir, "CCC_2013J.csv");
    try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
      reader.readLine(); // skip header
      assertEquals("-5,50", reader.readLine());
      assertEquals("0,75", reader.readLine());
      assertEquals("10,100", reader.readLine());
    }
  }

  @Test
  public void testWriteAllCreatesOutputDirectory() throws IOException {
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();
    ConcurrentMap<Integer, Long> dateMap = new ConcurrentHashMap<>();
    dateMap.put(0, 5L);
    clicksMap.put("AAA_2013J", dateMap);

    String outputDir = tempDir.getAbsolutePath() + File.separator + "new_folder";
    ResultWriter writer = new ResultWriter(outputDir);
    writer.writeAll(clicksMap);

    assertTrue(new File(outputDir).exists());
  }

  @Test
  public void testWriteAllEmptyMap() throws IOException {
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    String outputDir = tempDir.getAbsolutePath() + File.separator + "output";
    ResultWriter writer = new ResultWriter(outputDir);
    writer.writeAll(clicksMap);

    File dir = new File(outputDir);
    assertTrue(dir.exists());
    assertEquals(0, dir.listFiles().length);
  }

  @Test
  public void testEquals() {
    ResultWriter w1 = new ResultWriter("/path/a");
    ResultWriter w2 = new ResultWriter("/path/a");
    ResultWriter w3 = new ResultWriter("/path/b");

    assertEquals(w1, w1);
    assertEquals(w1, w2);
    assertNotEquals(w1, w3);
    assertNotEquals(w1, null);
    assertNotEquals(w1, "string");
  }

  @Test
  public void testHashCode() {
    ResultWriter w1 = new ResultWriter("/path/a");
    ResultWriter w2 = new ResultWriter("/path/a");
    assertEquals(w1.hashCode(), w2.hashCode());
  }

  @Test
  public void testToString() {
    ResultWriter w = new ResultWriter("/output");
    assertTrue(w.toString().contains("/output"));
  }
}