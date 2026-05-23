package oulad.sequentialSolution;

import java.util.Objects;

/**
 * This class represents a single parsed row from studentVle.csv
 * Contains click count for a student on a specific date
 */
public class StudentVleRecord {
  private String codeModule;
  private String codePresentation;
  private int date;
  private int sumClicks;

  /**
   * Constructor for StudentVleRecord
   * @param codeModule the course or module code for identification
   * @param codePresentation the presentation code for identification
   * @param date date relative to Course Start
   * @param sumClicks it is the number of clicks recorded
   */
  public StudentVleRecord(String codeModule, String codePresentation, int date, int sumClicks) {
    this.codeModule = codeModule;
    this.codePresentation = codePresentation;
    this.date = date;
    this.sumClicks = sumClicks;
  }

  /**
   * Getter for unique key combining module and presentation
   * @return Formatted key as a module presentation
   */
  public String getKey() {
    return (codeModule.replace("\"","").trim() + "_" + codePresentation.replace
        ("\"","").trim());
  }

  /**
   * Getter for date
   * @return date value
   */
  public int getDate() {
    return date;
  }

  /**
   * Getter for sum of the number of clicks
   * @return click count
   */
  public int getSumClicks() {
    return sumClicks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StudentVleRecord)) return false;
    StudentVleRecord that = (StudentVleRecord) o;
    return date == that.date &&
        Objects.equals(codeModule, that.codeModule) &&
        Objects.equals(codePresentation, that.codePresentation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeModule, codePresentation, date);
  }

  @Override
  public String toString() {
    return getKey() + " date=" + date + " clicks=" + sumClicks;
  }

}
