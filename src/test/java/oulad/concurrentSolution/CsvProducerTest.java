package oulad.concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CsvProducerTest {

  @TempDir
  File tempDir;

  @Test
  public void testProducerPutsLinesInQueue() throws IOException, InterruptedException {
    File csvFile = new File(tempDir, "test.csv");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
      writer.write("header1,header2,header3,header4,header5,header6\n");
      writer.write("\"AAA\",\"2013J\",\"111\",\"222\",\"-10\",\"4\"\n");
      writer.write("\"AAA\",\"2013J\",\"111\",\"222\",\"5\",\"7\"\n");
    }

    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer producer = new CsvProducer(csvFile.getAbsolutePath(), queue, 1);
    Thread t = new Thread(producer);
    t.start();
    t.join();

    // 2 data rows in one batch + 1 poison pill = 2 items on queue
    assertEquals(2, queue.size());

    // First item is a batch containing the 2 data rows
    List<String> batch = queue.take();
    assertEquals(2, batch.size());

    // Second item is the poison pill
    assertSame(CsvProducer.POISON_PILL, queue.take());
  }

  @Test
  public void testProducerSkipsHeader() throws IOException, InterruptedException {
    File csvFile = new File(tempDir, "test.csv");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
      writer.write("this,is,the,header,row,line\n");
      writer.write("\"AAA\",\"2013J\",\"111\",\"222\",\"-10\",\"4\"\n");
    }

    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer producer = new CsvProducer(csvFile.getAbsolutePath(), queue, 1);
    Thread t = new Thread(producer);
    t.start();
    t.join();

    List<String> batch = queue.take();
    assertFalse(batch.get(0).contains("header"));
  }

  @Test
  public void testProducerSendsPoisonPills() throws IOException, InterruptedException {
    File csvFile = new File(tempDir, "test.csv");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
      writer.write("header\n");
      writer.write("data_line\n");
    }

    int numConsumers = 3;
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer producer = new CsvProducer(csvFile.getAbsolutePath(), queue, numConsumers);
    Thread t = new Thread(producer);
    t.start();
    t.join();

    // 1 data batch + 3 poison pills = 4 items
    assertEquals(4, queue.size());

    queue.take(); // skip data batch
    assertSame(CsvProducer.POISON_PILL, queue.take());
    assertSame(CsvProducer.POISON_PILL, queue.take());
    assertSame(CsvProducer.POISON_PILL, queue.take());
  }

  @Test
  public void testProducerHandlesMissingFile() throws InterruptedException {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer producer = new CsvProducer("nonexistent.csv", queue, 1);
    Thread t = new Thread(producer);
    t.start();
    t.join();

    assertTrue(queue.isEmpty());
  }

  @Test
  public void testEquals() {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer p1 = new CsvProducer("file.csv", queue, 2);
    CsvProducer p2 = new CsvProducer("file.csv", queue, 2);
    CsvProducer p3 = new CsvProducer("other.csv", queue, 2);

    assertEquals(p1, p1);
    assertEquals(p1, p2);
    assertNotEquals(p1, p3);
    assertNotEquals(p1, null);
    assertNotEquals(p1, "string");
  }

  @Test
  public void testHashCode() {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer p1 = new CsvProducer("file.csv", queue, 2);
    CsvProducer p2 = new CsvProducer("file.csv", queue, 2);
    assertEquals(p1.hashCode(), p2.hashCode());
  }

  @Test
  public void testToString() {
    BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();
    CsvProducer p = new CsvProducer("file.csv", queue, 2);
    assertTrue(p.toString().contains("file.csv"));
  }
}