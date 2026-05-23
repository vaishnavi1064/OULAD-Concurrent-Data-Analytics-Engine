package oulad.concurrentSolution;

import java.io.File;
import java.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import oulad.analytics.ActivityAnalyzer;



/**
 * this class is teh Entry point for the concurrent solution..
 */

public class ConcurrentMain {

  private static final String COURSES_FILE = "courses.csv";
  private static final String STUDENT_VLE_FILE = "studentVle.csv";
  private static final int QUEUE_CAPACITY = 1000;

  /**
   *
   * @param args path to the data directory
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: java ConcurrentMain <data-directory> <threshold> [numConsumers]");
      return;
    }
    String dataDir = args[0];
    int threshold = Integer.parseInt(args[1]);
    int numConsumers = (args.length >= 3) ? Integer.parseInt(args[2]) : 2;
    String coursesPath = dataDir + File.separator + COURSES_FILE;
    String studentVlePath = dataDir + File.separator + STUDENT_VLE_FILE;

    try {

      List<CourseRecord> courses = CourseRecord.loadCourses(coursesPath);
      System.out.println("Loaded " + courses.size() + " course offerings.");


      BlockingQueue<List<String>> sharedQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
      ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap =
          new ConcurrentHashMap<>();


      CsvProducer producer = new CsvProducer(studentVlePath, sharedQueue, numConsumers);
      Thread producerThread = new Thread(producer);


      List<Thread> consumerThreads = new ArrayList<>();
      for (int i = 0; i < numConsumers; i++) {
        ClickConsumer consumer = new ClickConsumer(sharedQueue, clicksMap);
        Thread consumerThread = new Thread(consumer);
        consumerThreads.add(consumerThread);
      }


      ClickConsumer.rowsProcessed.set(0);
      long startNs = System.nanoTime();

      producerThread.start();
      for (Thread t : consumerThreads) {
        t.start();
      }


      producerThread.join();
      for (Thread t : consumerThreads) {
        t.join();
      }

      long endNs = System.nanoTime();
      long durationMs = (endNs - startNs) / 1_000_000;
      long rows = ClickConsumer.rowsProcessed.get();
      long rowsPerSec = (durationMs > 0) ? (rows * 1000) / durationMs : 0;
      System.out.printf("BENCHMARK,concurrent,%d,%d,%d%n", rows, durationMs, rowsPerSec);
      System.out.printf("Processed %d rows in %d ms (%,d rows/sec)%n", rows, durationMs, rowsPerSec);

      System.out.println("All threads finished. Writing output files...");


      String outputDir = dataDir + File.separator + "output";
      ResultWriter writer = new ResultWriter(outputDir);
      writer.writeAll(clicksMap);

      System.out.println("Done. Output written to: " + outputDir);
      System.out.println("Running activity analysis with threshold: " + threshold);

      ActivityAnalyzer analyzer = new ActivityAnalyzer();
      analyzer.analyze(dataDir, threshold);

    } catch (IOException e) {
      System.err.println("File error: " + e.getMessage());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Main thread was interrupted.");
    }
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    return o != null && getClass() == o.getClass();
  }

  @Override
  public int hashCode() {
    return Objects.hash(COURSES_FILE, STUDENT_VLE_FILE);
  }

  @Override
  public String toString() {
    return "ConcurrentMain{}";
  }
}