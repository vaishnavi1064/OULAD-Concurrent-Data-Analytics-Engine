package oulad.concurrentSolution;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentMainTest {

  @Test
  void testMain() throws Exception {

    Path dir = Files.createTempDirectory("data");

    Path courses = dir.resolve("courses.csv");
    BufferedWriter w1 = Files.newBufferedWriter(courses);
    w1.write("code_module,code_presentation,code_presentation_length\n");
    w1.write("AAA,2013J,100\n");
    w1.close();

    Path student = dir.resolve("studentVle.csv");
    BufferedWriter w2 = Files.newBufferedWriter(student);
    w2.write("code_module,code_presentation,id_student,id_site,date,sum_click\n");
    w2.write("AAA,2013J,1,1,-10,5000\n");
    w2.write("AAA,2013J,2,1,-9,12000\n");
    w2.write("AAA,2013J,3,1,-8,15000\n");
    w2.close();

    String[] args = { dir.toString(), "10000" };
    ConcurrentMain.main(args);

    Path outDir = dir.resolve("output");
    boolean folderExists = Files.exists(outDir);
    assertTrue(folderExists);

    Path summary = outDir.resolve("AAA_2013J.csv");
    boolean fileExists = Files.exists(summary);
    assertTrue(fileExists);

    List<String> lines = Files.readAllLines(summary);
    assertTrue(lines.size() >= 2);

    Path activity = dir.resolve("activity-10000.csv");
    boolean activityExists = Files.exists(activity);
    assertTrue(activityExists);

    List<String> activityData = Files.readAllLines(activity);

    int size = activityData.size();
    assertEquals(3, size);

    String header = activityData.get(0);
    assertEquals("module_presentation,date,total_clicks", header);

    boolean ok1 = activityData.contains("AAA_2013J,-9,12000");
    boolean ok2 = activityData.contains("AAA_2013J,-8,15000");

    assertTrue(ok1);
    assertTrue(ok2);
  }

  @Test
  void testEqualsHashCodeToString() {

    ConcurrentMain obj1 = new ConcurrentMain();
    ConcurrentMain obj2 = new ConcurrentMain();

    boolean same = obj1.equals(obj2);
    assertTrue(same);

    int hash1 = obj1.hashCode();
    int hash2 = obj2.hashCode();
    assertEquals(hash1, hash2);

    String str = obj1.toString();
    assertTrue(str.contains("ConcurrentMain"));

    boolean notNull = obj1.equals(null);
    assertFalse(notNull);

    boolean self = obj1.equals(obj1);
    assertTrue(self);
  }

  @Test
  void testMainWithNoArgs() {
    String[] args = {};
    ConcurrentMain.main(args);
  }

  @Test
  void testMainIOException() {
    String[] args = { "invalid_path_123", "100" };
    ConcurrentMain.main(args);
  }

  @Test
  void testMainInterrupted() throws Exception {

    Thread t = new Thread(() -> {
      String[] args = { "invalid_path_123", "100" };
      ConcurrentMain.main(args);
    });

    t.start();

    Thread.sleep(50);

    t.interrupt();

    t.join();
  }

  @Test
  void testEqualsFalse() {

    ConcurrentMain obj = new ConcurrentMain();

    Object other = new Object();

    boolean result = obj.equals(other);

    assertFalse(result);
  }



}