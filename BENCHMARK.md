# OULAD Pipeline Benchmark Results

## Environment
- Date: 
- Machine / CPU: 
- RAM: 
- OS: 
- JVM version: 
- JVM flags (if any non-default): 

## Dataset
- File: studentVle.csv
- Row count (from `wc -l`): 
- File size on disk: 

## Methodology
- 4 runs per configuration. Run #1 discarded as JVM warmup. Reported values are mean of runs 2-4.
- Timing covers only the producer-consumer pipeline (read + parse + aggregate). Output writing and post-processing are excluded.
- Machine was on AC power (not battery). No other heavy applications running.

## Sequential Results
| Run | Rows processed | Duration (ms) | Rows/sec |
|-----|---------------|---------------|----------|
| 1 (warmup) |  |  |  |
| 2 |  |  |  |
| 3 |  |  |  |
| 4 |  |  |  |
| **Mean (2-4)** |  |  |  |

## Concurrent Results (2 consumers)
| Run | Rows processed | Duration (ms) | Rows/sec |
|-----|---------------|---------------|----------|
| 1 (warmup) |  |  |  |
| 2 |  |  |  |
| 3 |  |  |  |
| 4 |  |  |  |
| **Mean (2-4)** |  |  |  |

## Headline Numbers
- Speedup (sequential mean / concurrent mean): 
- Concurrent throughput: 
- Sequential throughput: 
