package oulad.sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentVleRecordTest {
  private StudentVleRecord record;
  @BeforeEach
  void setUp() {
    record = new StudentVleRecord("AAA", "2013J", -10, 19);
  }

  @Test
  void getKey() {
    assertEquals("AAA_2013J", record.getKey());
  }

  @Test
  void getDate() {
    assertEquals(-10, record.getDate());
  }

  @Test
  void getSumClicks() {
    assertEquals(19, record.getSumClicks());
  }

  @Test
  void testEquals() {
    StudentVleRecord sameRecord = new StudentVleRecord("AAA", "2013J", -10, 50);
    StudentVleRecord differentRecord = new StudentVleRecord("BBB", "2014J", -5, 10);

    assertEquals(record, sameRecord);
    assertNotEquals(record, differentRecord);
    assertNotEquals(record, null);
    assertNotEquals(record,"NotARecord");
  }

  @Test
  void testHashCode() {
    StudentVleRecord sameRecord = new StudentVleRecord("AAA", "2013J", -10, 50);
    assertEquals(record.hashCode(), sameRecord.hashCode());
  }

  @Test
  void testToString() {
    String result = record.toString();

    assertTrue(result.contains("AAA_2013J"));
    assertTrue(result.contains("date=-10"));
    assertTrue(result.contains("clicks=19"));
  }
}