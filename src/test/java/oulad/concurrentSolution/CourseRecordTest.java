package oulad.concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


public class CourseRecordTest {

  @TempDir
  File tempDir;

  private CourseRecord record1;
  private CourseRecord record1Copy;
  private CourseRecord record2;

  @BeforeEach
  public void setUp() {
    record1 = new CourseRecord("AAA", "2013J");
    record1Copy = new CourseRecord("AAA", "2013J");
    record2 = new CourseRecord("BBB", "2014J");
  }

  @Test
  public void testGetCodeModule() {
    assertEquals("AAA", record1.getCodeModule());
  }

  @Test
  public void testGetCodePresentation() {
    assertEquals("2013J", record1.getCodePresentation());
  }

  @Test
  public void testGetKey() {
    assertEquals("AAA_2013J", record1.getKey());
    assertEquals("BBB_2014J", record2.getKey());
  }

  @Test
  public void testLoadCourses() throws IOException {
    File csvFile = new File(tempDir, "courses.csv");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
      writer.write("code_module,code_presentation,module_presentation_length\n");
      writer.write("\"AAA\",\"2013J\",\"268\"\n");
      writer.write("\"BBB\",\"2014J\",\"240\"\n");
    }

    List<CourseRecord> courses = CourseRecord.loadCourses(csvFile.getAbsolutePath());
    assertEquals(2, courses.size());
    assertEquals("AAA", courses.get(0).getCodeModule());
    assertEquals("2013J", courses.get(0).getCodePresentation());
    assertEquals("BBB", courses.get(1).getCodeModule());
  }

  @Test
  public void testLoadCoursesFileNotFound() {
    assertThrows(IOException.class, () -> {
      CourseRecord.loadCourses("nonexistent_file.csv");
    });
  }

  @Test
  public void testEqualsSameObject() {
    assertEquals(record1, record1);
  }

  @Test
  public void testEqualsEqualObjects() {
    assertEquals(record1, record1Copy);
  }

  @Test
  public void testEqualsDifferentObjects() {
    assertNotEquals(record1, record2);
  }

  @Test
  public void testEqualsNull() {
    assertNotEquals(record1, null);
  }

  @Test
  public void testEqualsDifferentClass() {
    assertNotEquals(record1, "AAA_2013J");
  }

  @Test
  public void testHashCodeEqual() {
    assertEquals(record1.hashCode(), record1Copy.hashCode());
  }

  @Test
  public void testHashCodeDifferent() {
    assertNotEquals(record1.hashCode(), record2.hashCode());
  }

  @Test
  public void testToString() {
    String str = record1.toString();
    assertTrue(str.contains("AAA"));
    assertTrue(str.contains("2013J"));
  }
}