package oulad.concurrentSolution;

import java.util.Objects;

/**
 * Represents a single parsed row from studentVle.csv.
 * Contains the course module, presentation, date, and click count
 * for one student interaction with one resource on one day.
 */
public class ClickRecord {

  private final String codeModule;
  private final String codePresentation;
  private final int date;
  private final int sumClick;

  /**
   * Constructs a new ClickRecord.
   *
   * @param codeModule       module code
   * @param codePresentation presentation code
   * @param date             date relative to coursestart
   * @param sumClick         number of clicks
   */
  public ClickRecord(String codeModule, String codePresentation,
      int date, int sumClick) {
    this.codeModule = codeModule;
    this.codePresentation = codePresentation;
    this.date = date;
    this.sumClick = sumClick;
  }

  /**
   *
   * @return module and presentatin that is joined by undeerscore.
   */
  public String getCourseKey() {
    return codeModule + "_" + codePresentation;
  }

  /**
   *
   * @return date value
   */
  public int getDate() {
    return date;
  }

  /**
   *
   * @return click count
   */
  public int getSumClick() {
    return sumClick;
  }

  /**
   * @param csvLine singlline from studentVle.csv
   * @return the parsed ClickRecord
   */
  public static ClickRecord fromCsvLine(String csvLine) {
    String[] parts = csvLine.split(",");
    String module = parts[0].trim().replace("\"", "");
    String presentation = parts[1].trim().replace("\"", "");
    int date = Integer.parseInt(parts[4].trim().replace("\"", ""));
    int clicks = Integer.parseInt(parts[5].trim().replace("\"", ""));
    return new ClickRecord(module, presentation, date, clicks);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClickRecord that = (ClickRecord) o;
    return date == that.date
        && sumClick == that.sumClick
        && Objects.equals(codeModule, that.codeModule)
        && Objects.equals(codePresentation, that.codePresentation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codeModule, codePresentation, date, sumClick);
  }

  @Override
  public String toString() {
    return "ClickRecord{"
        + "codeModule='" + codeModule + '\''
        + ", codePresentation='" + codePresentation + '\''
        + ", date=" + date
        + ", sumClick=" + sumClick
        + '}';
  }
}