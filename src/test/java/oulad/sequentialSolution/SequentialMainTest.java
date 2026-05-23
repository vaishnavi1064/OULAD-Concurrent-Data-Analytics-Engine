package oulad.sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SequentialMainTest {
  private SequentialMain processor;
  private Map<String, Map<Integer, Integer>> result;

  @BeforeEach
  void setUp() {
    processor = new SequentialMain();
    result = new HashMap<>();
  }

  @Test
  void testProcessRecord() {
    StudentVleRecord record = new StudentVleRecord("AAA", "2013J", -10, 20);

    processor.processRecord(record, result);

    assertTrue(result.containsKey("AAA_2013J"));
    assertEquals(20, result.get("AAA_2013J").get(-10));
  }

  @Test
  void testDifferentDatesSameCourse() {
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 20), result);
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -5, 30), result);

    assertEquals(20, result.get("AAA_2013J").get(-10));
    assertEquals(30, result.get("AAA_2013J").get(-5));
  }

  @Test
  void testMultipleCourses() {
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 20), result);
    processor.processRecord(new StudentVleRecord("BBB", "2014J", -10, 40), result);

    assertTrue(result.containsKey("AAA_2013J"));
    assertTrue(result.containsKey("BBB_2014J"));

    assertEquals(20, result.get("AAA_2013J").get(-10));
    assertEquals(40, result.get("BBB_2014J").get(-10));
  }

  @Test
  void testAggregationMultipleRecords() {
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 20), result);
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 30), result);

    assertEquals(50, result.get("AAA_2013J").get(-10));
  }

  @Test
  void testZeroClicks() {
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 0), result);

    assertEquals(0, result.get("AAA_2013J").get(-10));
  }

  @Test
  void testNegativeDate() {
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -100, 25), result);

    assertEquals(25, result.get("AAA_2013J").get(-100));
  }
  @Test
  void testComplexScenario() {
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 10), result);
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -5, 20), result);
    processor.processRecord(new StudentVleRecord("BBB", "2014J", -10, 30), result);
    processor.processRecord(new StudentVleRecord("AAA", "2013J", -10, 5), result);

    assertEquals(15, result.get("AAA_2013J").get(-10));
    assertEquals(20, result.get("AAA_2013J").get(-5));
    assertEquals(30, result.get("BBB_2014J").get(-10));
  }
}