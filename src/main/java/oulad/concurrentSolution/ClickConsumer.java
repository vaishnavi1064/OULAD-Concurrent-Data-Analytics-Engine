package oulad.concurrentSolution;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Consumer thread that aggregates click data from a shared BlockingQueue into a ConcurrentHashMap.
 */
public class ClickConsumer implements Runnable {

  public static final AtomicLong rowsProcessed = new AtomicLong(0);

  private final BlockingQueue<List<String>> sharedQueue;
  private final ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap;

  /**
   * Constructs a ClickConsumer.
   *
   * @param sharedQueue shared queue from where we take batches form
   * @param clicksMap   shared map for aggregating clicks into it.
   *                    Outer key: course key (e.g., "AAA_2013J").
   *                    Inner key: date. Inner value: total clicks.
   */
  public ClickConsumer(BlockingQueue<List<String>> sharedQueue,
      ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap) {
    this.sharedQueue = sharedQueue;
    this.clicksMap = clicksMap;
  }

  /**
   * Continuously taking batches from the queue, parsing each line into a
   * ClickRecord, and merginh the click count into shared map.
   * thisTerminates when the poison pill is got.
   */
  @Override
  public void run() {
    try {
      while (true) {
        List<String> batch = sharedQueue.take();

        if (batch == CsvProducer.POISON_PILL) {
          break;
        }

        for (String line : batch) {
          ClickRecord record = ClickRecord.fromCsvLine(line);
          String courseKey = record.getCourseKey();


          clicksMap.putIfAbsent(courseKey, new ConcurrentHashMap<>());
          ConcurrentMap<Integer, Long> dateMap = clicksMap.get(courseKey);


          dateMap.merge(record.getDate(), (long) record.getSumClick(), Long::sum);
          rowsProcessed.incrementAndGet();
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Consumer was interrupted.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClickConsumer that = (ClickConsumer) o;
    return Objects.equals(sharedQueue, that.sharedQueue)
        && Objects.equals(clicksMap, that.clicksMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sharedQueue, clicksMap);
  }

  @Override
  public String toString() {
    return "ClickConsumer{" + "clicksMap keys=" + clicksMap.keySet() + '}';
  }
}