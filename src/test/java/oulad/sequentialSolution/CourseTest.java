package oulad.sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {

  private Course course;

  @BeforeEach
  void setUp() {
    course = new Course("AAA", "2013J", 100);
  }

  @Test
  void getCodeModule() {
    assertEquals("AAA", course.getCodeModule());
  }

  @Test
  void getCodePresentation() {
    assertEquals("2013J", course.getCodePresentation());
  }

  @Test
  void getPresentationLength() {
    assertEquals(100, course.getPresentationLength());
  }

  @Test
  void getKey() {
    assertEquals("AAA_2013J", course.getKey());
  }

  @Test
  void testEquals() {
    Course same = new Course("AAA", "2013J", 200);
    Course different = new Course("BBB", "2014J", 100);
    assertTrue(course.equals(same));
    assertFalse(course.equals(different));
    assertFalse(course.equals(null));
    assertFalse(course.equals("NotACourse"));
  }

  @Test
  void testHashCode() {
    Course same = new Course("AAA", "2013J", 100);
    assertEquals(course.hashCode(), same.hashCode());
  }

  @Test
  void testToString() {
    assertEquals("AAA_2013J", course.toString());
  }
}