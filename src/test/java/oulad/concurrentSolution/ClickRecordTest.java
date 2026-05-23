package oulad.concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClickRecordTest {

  private ClickRecord record1;
  private ClickRecord record1Copy;
  private ClickRecord record2;

  @BeforeEach
  public void setUp() {
    record1 = new ClickRecord("AAA", "2013J", -10, 4);
    record1Copy = new ClickRecord("AAA", "2013J", -10, 4);
    record2 = new ClickRecord("BBB", "2014J", 5, 10);
  }

  @Test
  public void testGetCourseKey() {
    assertEquals("AAA_2013J", record1.getCourseKey());
  }

  @Test
  public void testGetDate() {
    assertEquals(-10, record1.getDate());
  }

  @Test
  public void testGetSumClick() {
    assertEquals(4, record1.getSumClick());
  }

  @Test
  public void testFromCsvLine() {
    String line = "\"AAA\",\"2013J\",\"11391\",\"546652\",\"-10\",\"4\"";
    ClickRecord parsed = ClickRecord.fromCsvLine(line);
    assertEquals("AAA_2013J", parsed.getCourseKey());
    assertEquals(-10, parsed.getDate());
    assertEquals(4, parsed.getSumClick());
  }

  @Test
  public void testFromCsvLineNoQuotes() {
    String line = "AAA,2013J,11391,546652,-10,4";
    ClickRecord parsed = ClickRecord.fromCsvLine(line);
    assertEquals("AAA_2013J", parsed.getCourseKey());
    assertEquals(-10, parsed.getDate());
    assertEquals(4, parsed.getSumClick());
  }

  @Test
  public void testFromCsvLinePositiveDate() {
    String line = "\"BBB\",\"2014J\",\"22345\",\"789012\",\"5\",\"10\"";
    ClickRecord parsed = ClickRecord.fromCsvLine(line);
    assertEquals("BBB_2014J", parsed.getCourseKey());
    assertEquals(5, parsed.getDate());
    assertEquals(10, parsed.getSumClick());
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
    assertNotEquals(record1, "not a ClickRecord");
  }

  @Test
  public void testEqualsDifferentDate() {
    ClickRecord different = new ClickRecord("AAA", "2013J", 5, 4);
    assertNotEquals(record1, different);
  }

  @Test
  public void testEqualsDifferentClicks() {
    ClickRecord different = new ClickRecord("AAA", "2013J", -10, 99);
    assertNotEquals(record1, different);
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
    assertTrue(str.contains("-10"));
    assertTrue(str.contains("4"));
  }
}