#!/bin/bash

# Create benchmarks directory if it doesn't exist
mkdir -p benchmarks

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
RESULTS_FILE="benchmarks/results-${TIMESTAMP}.txt"

echo "Building project..."
./gradlew clean build -x test

echo "Running Sequential Benchmark..."
for i in {1..4}
do
    echo "Sequential Run $i..."
    java -cp build/classes/java/main oulad.sequentialSolution.SequentialMain data | grep "BENCHMARK" | tee -a "$RESULTS_FILE"
done

echo "Running Concurrent Benchmark (2 consumers)..."
for i in {1..4}
do
    echo "Concurrent Run $i..."
    java -cp build/classes/java/main oulad.concurrentSolution.ConcurrentMain data 10000 2 | grep "BENCHMARK" | tee -a "$RESULTS_FILE"
done

echo ""
echo "=== Benchmark Summary ==="
cat "$RESULTS_FILE"
echo "Results saved to $RESULTS_FILE"
