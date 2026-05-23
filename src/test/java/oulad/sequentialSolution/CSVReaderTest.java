package oulad.sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVReaderTest {
  private CSVReader reader;
  private String coursesFile;
  private String studentFile;

  @BeforeEach
  void setUp() {
    try {
      reader = new CSVReader();

      coursesFile = "test_courses.csv";
      studentFile = "test_student.csv";

      FileWriter writer = new FileWriter(coursesFile);
      writer.write("code_module,code_presentation,length\n");
      writer.write("AAA,2013J,100\n");
      writer.close();

      writer = new FileWriter(studentFile);
      writer.write("code_module,code_presentation,id_student,id_site,date,sum_click\n");
      writer.write("AAA,2013J,1,1,-10,20\n");
      writer.close();

    } catch (Exception e) {
      fail("Setup failed: " + e.getMessage());
    }
  }

  @Test
  void readCourses() {
    try {
      List<Course> courses = reader.readCourses(coursesFile);

      assertNotNull(courses);
      assertEquals(1, courses.size());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void streamStudentVle() {
    try {
      SequentialMain processor = new SequentialMain();
      Map<String, Map<Integer, Integer>> result = new HashMap<>();

      reader.streamStudentVle(studentFile, processor, result);

      assertTrue(result.containsKey("AAA_2013J"));
      assertEquals(20, result.get("AAA_2013J").get(-10));

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void readCoursesEmptyFile() {
    try {
      FileWriter writer = new FileWriter("empty_courses.csv");
      writer.write("code_module,code_presentation,length\n"); // only header
      writer.close();

      List<Course> courses = reader.readCourses("empty_courses.csv");

      assertTrue(courses.isEmpty());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void streamStudentVleEmptyFile() {
    try {
      FileWriter writer = new FileWriter("empty_student.csv");
      writer.write("code_module,code_presentation,id_student,id_site,date,sum_click\n");
      writer.close();

      SequentialMain processor = new SequentialMain();
      Map<String, Map<Integer, Integer>> result = new HashMap<>();

      reader.streamStudentVle("empty_student.csv", processor, result);

      assertTrue(result.isEmpty());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void streamStudentVleMultipleRows() {
    try {
      FileWriter writer = new FileWriter(studentFile);
      writer.write("code_module,code_presentation,id_student,id_site,date,sum_click\n");
      writer.write("AAA,2013J,1,1,-10,20\n");
      writer.write("AAA,2013J,2,2,-10,30\n");
      writer.close();

      SequentialMain processor = new SequentialMain();
      Map<String, Map<Integer, Integer>> result = new HashMap<>();

      reader.streamStudentVle(studentFile, processor, result);

      assertEquals(50, result.get("AAA_2013J").get(-10));

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void streamStudentVleDifferentDates() {
    try {
      FileWriter writer = new FileWriter(studentFile);
      writer.write("code_module,code_presentation,id_student,id_site,date,sum_click\n");
      writer.write("AAA,2013J,1,1,-10,20\n");
      writer.write("AAA,2013J,1,1,-5,30\n");
      writer.close();

      SequentialMain processor = new SequentialMain();
      Map<String, Map<Integer, Integer>> result = new HashMap<>();

      reader.streamStudentVle(studentFile, processor, result);

      assertEquals(20, result.get("AAA_2013J").get(-10));
      assertEquals(30, result.get("AAA_2013J").get(-5));

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  @Test
  void streamStudentVleLargeValues() {
    try {
      FileWriter writer = new FileWriter(studentFile);
      writer.write("code_module,code_presentation,id_student,id_site,date,sum_click\n");
      writer.write("AAA,2013J,1,1,-10,100000\n");
      writer.close();

      SequentialMain processor = new SequentialMain();
      Map<String, Map<Integer, Integer>> result = new HashMap<>();

      reader.streamStudentVle(studentFile, processor, result);

      assertEquals(100000, result.get("AAA_2013J").get(-10));

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}