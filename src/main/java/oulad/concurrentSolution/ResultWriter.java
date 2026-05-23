package oulad.concurrentSolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * this class Writes the aggregated click data to output CSV files.
 */
public class ResultWriter {

  private final String outputDirectory;

  /**
   * Constructs a ResultWriter.
   *
   * @param outputDirectory the directory for writin output CSV files into
   */
  public ResultWriter(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * this Writes all the aggregated click data to CSV files.
   *
   * @param clicksMap the aggregated data
   * @throws IOException if a file cannot be written
   */
  public void writeAll(ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap)
      throws IOException {

    File outputDir = new File(outputDirectory);
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    for (Map.Entry<String, ConcurrentMap<Integer, Long>> courseEntry : clicksMap.entrySet()) {
      String courseKey = courseEntry.getKey();
      ConcurrentMap<Integer, Long> dateClicks = courseEntry.getValue();

      String fileName = courseKey + ".csv";
      File outputFile = new File(outputDir, fileName);


      TreeMap<Integer, Long> sorted = new TreeMap<>(dateClicks);

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        writer.write("date,total_clicks");
        writer.newLine();

        for (Map.Entry<Integer, Long> entry : sorted.entrySet()) {
          writer.write(entry.getKey() + "," + entry.getValue());
          writer.newLine();
        }
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResultWriter that = (ResultWriter) o;
    return Objects.equals(outputDirectory, that.outputDirectory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outputDirectory);
  }

  @Override
  public String toString() {
    return "ResultWriter{" + "outputDirectory='" + outputDirectory + '\'' + '}';
  }
}