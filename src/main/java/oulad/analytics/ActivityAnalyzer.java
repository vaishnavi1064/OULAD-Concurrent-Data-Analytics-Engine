package oulad.analytics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * this class is used for filtering activity data based on threshold.
 * it reads files and then writes the result.
 */
public class ActivityAnalyzer {

  /**
   * this method reads all csv files and checks for high activity days
   *
   * @param baseDir folder where output files are present
   * @param threshold minimum clicks value
   * @throws IOException if file reading or writing fails
   */
  public void analyze(String baseDir, int threshold) throws IOException {

    Path inputDir = Path.of(baseDir, "output");
    Path outputFile = Path.of(baseDir, "activity-" + threshold + ".csv");

    long total = 0;
    long matched = 0;

    BufferedWriter writer = Files.newBufferedWriter(outputFile);

    writer.write("module_presentation,date,total_clicks");
    writer.newLine();

    List<Path> files = Files.list(inputDir).toList();

    for (int i = 0; i < files.size(); i++) {

      Path file = files.get(i);
      String name = file.getFileName().toString();

      if (name.endsWith(".csv")) {

        String modulePresentation = name.replace(".csv", "");

        BufferedReader reader = Files.newBufferedReader(file);

        String line = reader.readLine();

        line = reader.readLine();

        while (line != null) {

          total = total + 1;

          String[] parts = line.split(",");

          if (parts.length >= 2) {

            int date = Integer.parseInt(parts[0].trim());
            int clicks = Integer.parseInt(parts[1].trim());

            if (clicks >= threshold) {
              writer.write(modulePresentation + "," + date + "," + clicks);
              writer.newLine();
              matched = matched + 1;
            }
          }

          line = reader.readLine();
        }

        reader.close();
      }
    }

    writer.close();

    System.out.println("Total rows scanned is: " + total);
    System.out.println("Rows meeting threshold is: " + matched);
    System.out.println("Output file is written at: " + outputFile);
  }
}