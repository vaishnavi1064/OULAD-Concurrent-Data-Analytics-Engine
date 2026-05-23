package oulad.concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.Test;


public class ClickConsumerTest {

  @Test
  public void testConsumerAggregatesClicks() throws InterruptedException {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    List<String> batch = new ArrayList<>(Arrays.asList(
        "\"AAA\",\"2013J\",\"111\",\"222\",\"-10\",\"4\"",
        "\"AAA\",\"2013J\",\"222\",\"333\",\"-10\",\"3\"",
        "\"AAA\",\"2013J\",\"333\",\"444\",\"5\",\"7\""
    ));
    queue.put(batch);
    queue.put(CsvProducer.POISON_PILL);

    ClickConsumer consumer = new ClickConsumer(queue, clicksMap);
    Thread t = new Thread(consumer);
    t.start();
    t.join();

    assertEquals(Long.valueOf(7), clicksMap.get("AAA_2013J").get(-10));
    assertEquals(Long.valueOf(7), clicksMap.get("AAA_2013J").get(5));
  }

  @Test
  public void testConsumerHandlesMultipleCourses() throws InterruptedException {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    List<String> batch = new ArrayList<>(Arrays.asList(
        "\"AAA\",\"2013J\",\"111\",\"222\",\"-10\",\"4\"",
        "\"BBB\",\"2014J\",\"111\",\"222\",\"0\",\"8\""
    ));
    queue.put(batch);
    queue.put(CsvProducer.POISON_PILL);

    ClickConsumer consumer = new ClickConsumer(queue, clicksMap);
    Thread t = new Thread(consumer);
    t.start();
    t.join();

    assertTrue(clicksMap.containsKey("AAA_2013J"));
    assertTrue(clicksMap.containsKey("BBB_2014J"));
    assertEquals(Long.valueOf(4), clicksMap.get("AAA_2013J").get(-10));
    assertEquals(Long.valueOf(8), clicksMap.get("BBB_2014J").get(0));
  }

  @Test
  public void testConsumerStopsOnPoisonPill() throws InterruptedException {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    queue.put(CsvProducer.POISON_PILL);

    ClickConsumer consumer = new ClickConsumer(queue, clicksMap);
    Thread t = new Thread(consumer);
    t.start();
    t.join(5000);

    assertFalse(t.isAlive());
    assertTrue(clicksMap.isEmpty());
  }

  @Test
  public void testMultipleConsumers() throws InterruptedException {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> clicksMap = new ConcurrentHashMap<>();

    // Wrap 100 rows into batches of BATCH_SIZE (all fit in one batch since 100 < 1000)
    List<String> batch = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      batch.add("\"AAA\",\"2013J\",\"111\",\"222\",\"0\",\"1\"");
    }
    queue.put(batch);
    queue.put(CsvProducer.POISON_PILL);
    queue.put(CsvProducer.POISON_PILL);

    ClickConsumer consumer1 = new ClickConsumer(queue, clicksMap);
    ClickConsumer consumer2 = new ClickConsumer(queue, clicksMap);
    Thread t1 = new Thread(consumer1);
    Thread t2 = new Thread(consumer2);
    t1.start();
    t2.start();
    t1.join();
    t2.join();

    assertEquals(Long.valueOf(100), clicksMap.get("AAA_2013J").get(0));
  }

  @Test
  public void testEquals() {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> map = new ConcurrentHashMap<>();
    ClickConsumer c1 = new ClickConsumer(queue, map);
    ClickConsumer c2 = new ClickConsumer(queue, map);

    assertEquals(c1, c1);
    assertEquals(c1, c2);
    assertNotEquals(c1, null);
    assertNotEquals(c1, "string");
  }

  @Test
  public void testHashCode() {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> map = new ConcurrentHashMap<>();
    ClickConsumer c1 = new ClickConsumer(queue, map);
    ClickConsumer c2 = new ClickConsumer(queue, map);
    assertEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  public void testToString() {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    ConcurrentMap<String, ConcurrentMap<Integer, Long>> map = new ConcurrentHashMap<>();
    ClickConsumer c = new ClickConsumer(queue, map);
    assertNotNull(c.toString());
  }
}