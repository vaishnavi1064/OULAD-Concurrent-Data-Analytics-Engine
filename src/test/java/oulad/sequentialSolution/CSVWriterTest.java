package oulad.sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVWriterTest {
  private CSVWriter writer;
  private Map<String, Map<Integer, Integer>> data;
  private String outputDir;

  @BeforeEach
  void setUp() {
    writer = new CSVWriter();

    data = new HashMap<>();
    Map<Integer, Integer> inner = new HashMap<>();
    inner.put(-10, 100);

    data.put("AAA_2013J", inner);

    outputDir = "test_output";
    new File(outputDir).mkdir();
  }

  @Test
  void writeOutput() throws Exception {
    writer.writeOutput(data, outputDir);

    File outputFile = new File(outputDir + "/AAA_2013J.csv");
    assertTrue(outputFile.exists());

    String content = new String(Files.readAllBytes(outputFile.toPath()));
    assertTrue(content.contains("-10,100"));
  }


}