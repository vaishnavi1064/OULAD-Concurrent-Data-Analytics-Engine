package oulad.sequentialSolution;

import java.util.Objects;

/**
 * This class represents a course offered in the OULAD dataset.
 */
public class Course {
  private String codeModule;
  private String codePresentation;
  private int presentationLength;

  /**
   * Constructor for Course
   * @param codeModule the course or module code for identification
   * @param codePresentation the presentation code for identification
   * @param presentationLength the duration of the course in number of days
   */
  public Course(String codeModule, String codePresentation, int presentationLength) {
    this.codeModule = codeModule;
    this.codePresentation = codePresentation;
    this.presentationLength = presentationLength;
  }

  /**
   * Getter for code module
   * @return Module Code
   */
  public String getCodeModule() {

    return codeModule;
  }

  /**
   * Getter for presentation code
   * @return presentation code
   */
  public String getCodePresentation() {

    return codePresentation;
  }

  /**
   * Getter for course duration.
   * @return course duration.
   */
  public int getPresentationLength() {

    return presentationLength;
  }

  /**
   * Getter for unique key combining module and presentation
   * @return Formatted key as a module presentation
   */
  public String getKey() {
    return (codeModule.replace("\"","").trim() + "_" + codePresentation.replace
        ("\"","").trim());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Course)) return false;
    Course course = (Course) o;
    return Objects.equals(codeModule, course.codeModule) &&
        Objects.equals(codePresentation, course.codePresentation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeModule, codePresentation);
  }

  @Override
  public String toString() {
    return getKey();
  }
}