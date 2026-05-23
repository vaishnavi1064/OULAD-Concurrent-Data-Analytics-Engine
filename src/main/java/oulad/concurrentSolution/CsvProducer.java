package oulad.concurrentSolution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * this class has a producer thread that reads studentVle.csv line by line
 */
public class CsvProducer implements Runnable {

  /** Number of rows per batch to reduce queue contention overhead. */
  public static final int BATCH_SIZE = 1000;

  /** Sentinel value that signals consumer threads to stop (reference-equality check). */
  public static final List<String> POISON_PILL = Collections.emptyList();

  private final String filePath;
  private final BlockingQueue<List<String>> sharedQueue;
  private final int numConsumers;

  /**
   *
   * @param filePath     the path to studentVle.csv
   * @param sharedQueue  the shared queue to place batches into
   * @param numConsumers number of consumers (one poison pill sent per consumer)
   */
  public CsvProducer(String filePath, BlockingQueue<List<String>> sharedQueue,
      int numConsumers) {
    this.filePath = filePath;
    this.sharedQueue = sharedQueue;
    this.numConsumers = numConsumers;
  }

  /**
   * this Reads the CSV file line by line, also skiping the header,
   * and places each data line into the shared queue.
   * After all the lines are read, one poison pill is snet per consumer.
   */
  @Override
  public void run() {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isHeader = true;
      List<String> batch = new ArrayList<>(BATCH_SIZE);

      while ((line = reader.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }
        batch.add(line);
        if (batch.size() >= BATCH_SIZE) {
          sharedQueue.put(batch);
          batch = new ArrayList<>(BATCH_SIZE);
        }
      }

      // Put any remaining partial batch
      if (!batch.isEmpty()) {
        sharedQueue.put(batch);
      }

      for (int i = 0; i < numConsumers; i++) {
        sharedQueue.put(POISON_PILL);
      }

    } catch (IOException e) {
      System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Producer was interrupted.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CsvProducer that = (CsvProducer) o;
    return numConsumers == that.numConsumers
        && Objects.equals(filePath, that.filePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filePath, numConsumers);
  }

  @Override
  public String toString() {
    return "CsvProducer{"
        + "filePath='" + filePath + '\''
        + ", numConsumers=" + numConsumers
        + '}';
  }
}