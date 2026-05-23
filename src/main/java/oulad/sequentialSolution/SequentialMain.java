package oulad.sequentialSolution;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is entry point for the sequential solution
 *
 */
public class SequentialMain {
  public long rowsProcessed = 0;

  /**
   *
   * @param args args[0] is the command line directory path contains input CSV files
   */
  public static void main(String[] args) {
      if (args.length < 1) {
        System.out.println("Usage: java Main <directory>");
        return;
      }

      String dir = args[0];

      String coursesFile = dir + "/courses.csv";
      String studentFile = dir + "/studentVle.csv";

      try {
        CSVReader reader = new CSVReader();
        reader.readCourses(coursesFile);
        SequentialMain processor = new SequentialMain();
        Map<String, Map<Integer, Integer>> result = new HashMap<>();


        long startNs = System.nanoTime();

        reader.streamStudentVle(studentFile, processor, result);

        long endNs = System.nanoTime();
        long durationMs = (endNs - startNs) / 1_000_000;
        long rows = processor.rowsProcessed;
        long rowsPerSec = (durationMs > 0) ? (rows * 1000) / durationMs : 0;
        System.out.printf("BENCHMARK,sequential,%d,%d,%d%n", rows, durationMs, rowsPerSec);
        System.out.printf("Processed %d rows in %d ms (%,d rows/sec)%n", rows, durationMs, rowsPerSec);

        CSVWriter writer = new CSVWriter();
        writer.writeOutput(result, dir);

        System.out.println("Streaming processing complete!");

      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  /**
   * This method processes a single StudentVleRecord and updates teh aggregation map
   * This method helpful to handle streaming large datasets efficiently
   * @param record the student interaction record
   * @param result teh map storing aggregated results
   */
  public void processRecord(StudentVleRecord record,
      Map<String, Map<Integer, Integer>> result) {

    String key = record.getKey();
    int date = record.getDate();
    int clicks = record.getSumClicks();

    result.putIfAbsent(key, new HashMap<>());
    Map<Integer, Integer> dateMap = result.get(key);

    dateMap.put(date, dateMap.getOrDefault(date, 0) + clicks);
    this.rowsProcessed++;
  }
}
