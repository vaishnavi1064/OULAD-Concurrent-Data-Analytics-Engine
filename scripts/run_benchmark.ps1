$ErrorActionPreference = "Stop"

# Create benchmarks directory if it doesn't exist
if (-not (Test-Path -Path "benchmarks")) {
    New-Item -ItemType Directory -Path "benchmarks" | Out-Null
}

$Timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$ResultsFile = "benchmarks/results-$Timestamp.txt"

Write-Host "Building project..."
.\gradlew.bat clean build "-x" test

Write-Host "Running Sequential Benchmark..."
for ($i = 1; $i -le 4; $i++) {
    Write-Host "Sequential Run $i..."
    $output = java -cp build/classes/java/main oulad.sequentialSolution.SequentialMain data
    $benchmarkLine = $output | Select-String "BENCHMARK"
    if ($benchmarkLine) {
        $benchmarkLine.Line | Out-File -FilePath $ResultsFile -Append -Encoding UTF8
    }
}

Write-Host "Running Concurrent Benchmark (2 consumers)..."
for ($i = 1; $i -le 4; $i++) {
    Write-Host "Concurrent Run $i..."
    $output = java -cp build/classes/java/main oulad.concurrentSolution.ConcurrentMain data 10000 2
    $benchmarkLine = $output | Select-String "BENCHMARK"
    if ($benchmarkLine) {
        $benchmarkLine.Line | Out-File -FilePath $ResultsFile -Append -Encoding UTF8
    }
}

Write-Host ""
Write-Host "=== Benchmark Summary ==="
Get-Content -Path $ResultsFile
Write-Host "Results saved to $ResultsFile"
