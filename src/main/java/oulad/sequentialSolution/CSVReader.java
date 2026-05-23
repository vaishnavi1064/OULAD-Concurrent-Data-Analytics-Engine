package oulad.sequentialSolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for reading CSV input files and to convert them into objects
 */
public class CSVReader {

  /**
   * Read the courses.csv file and converts it into a list of Course object
   * @param filePath this is path to the courses.csv file
   * @return  list of Course objects
   * @throws IOException if the file cannot be read
   */
  public List<Course> readCourses(String filePath) throws IOException {
    List<Course> courses = new ArrayList<>();

    BufferedReader br = new BufferedReader(new FileReader(filePath));
    String line;
    br.readLine();

    while ((line = br.readLine()) != null) {
      String[] parts = line.split(",", -1);

      Course course = new Course(
          parts[0],
          parts[1],
          Integer.parseInt(parts[2].replace("\"", "").trim())
      );

      courses.add(course);
    }

    br.close();
    return courses;
  }

  /**
   * This method streams the studentVle.csv file line-by-line and processes each record
   * This method also avoid loading the entire file into memory
   * @param filePath path to studentVle.csv file
   * @param processor the processor who is handling aggregation logic
   * @param result it is the map storing aggregated results
   * @throws IOException if the file cannot be read
   */
  public void streamStudentVle(String filePath,
      SequentialMain processor,
      Map<String, Map<Integer, Integer>> result)
      throws IOException {

    BufferedReader br = new BufferedReader(new FileReader(filePath));
    String line;
    br.readLine();

    while ((line = br.readLine()) != null) {

      String[] parts = line.split(",", -1);
      StudentVleRecord record = new StudentVleRecord(
          parts[0],
          parts[1],
          Integer.parseInt(parts[4].replace("\"", "").trim()),
          Integer.parseInt(parts[5].replace("\"", "").trim())
      );

      processor.processRecord(record, result);
    }

    br.close();
  }
}