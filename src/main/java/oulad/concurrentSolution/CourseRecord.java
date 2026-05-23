package oulad.concurrentSolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this class Repersents a single course offering from courses.csv file.
 * Each record contains a module code and a presentation code
 */
public class CourseRecord {

  private final String codeModule;
  private final String codePresentation;

  /**
   *
   * @param codeModule       the module code
   * @param codePresentation the presentation code
   */
  public CourseRecord(String codeModule, String codePresentation) {
    this.codeModule = codeModule;
    this.codePresentation = codePresentation;
  }

  /**
   *
   * @return module code
   */
  public String getCodeModule() {
    return codeModule;
  }

  /**
   *
   * @return presentation code
   */
  public String getCodePresentation() {
    return codePresentation;
  }

  /**
   *
   * @return the module and presentation joined by an underscore
   */
  public String getKey() {
    return codeModule + "_" + codePresentation;
  }

  /**
   * @param filePath path to courses.csv file
   * @return a list of CourseRecord objects
   * @throws IOException if the file cannot be read
   */

  public static List<CourseRecord> loadCourses(String filePath) throws IOException {
    List<CourseRecord> courses = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isHeader = true;
      while ((line = reader.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }
        String[] parts = line.split(",");
        String module = parts[0].trim().replace("\"", "");
        String presentation = parts[1].trim().replace("\"", "");
        courses.add(new CourseRecord(module, presentation));
      }
    }
    return courses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CourseRecord that = (CourseRecord) o;
    return Objects.equals(codeModule, that.codeModule)
        && Objects.equals(codePresentation, that.codePresentation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeModule, codePresentation);
  }

  @Override
  public String toString() {
    return "CourseRecord{"
        + "codeModule='" + codeModule + '\''
        + ", codePresentation='" + codePresentation + '\''
        + '}';
  }
}