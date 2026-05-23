package oulad.sequentialSolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * This class writes aggregated results into CSV output files
 */
public class CSVWriter {

  /**
   * Writes agregated click data into separate CSV files for each course presentation
   * @param data the aggregated result map
   * @param outputDir it is the directory whsre the output file will be written
   * @throws IOException if the files writing fails
   */
  public void writeOutput(Map<String, Map<Integer, Integer>> data, String outputDir) throws IOException {

    for (String key : data.keySet()) {
      String safeKey = key.replace("\"", "").trim();
      File file = new File(outputDir + "/" + safeKey + ".csv");

      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      writer.write("date,total_clicks\n");

      for (Map.Entry<Integer, Integer> entry : data.get(key).entrySet()) {
        writer.write(entry.getKey() + "," + entry.getValue() + "\n");
      }

      writer.close();
    }
  }
}
