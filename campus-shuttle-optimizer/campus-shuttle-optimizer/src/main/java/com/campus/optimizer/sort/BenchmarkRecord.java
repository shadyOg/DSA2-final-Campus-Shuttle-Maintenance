package com.campus.optimizer.sort;

public class BenchmarkRecord {
    private final String algorithmName;
    private final int inputSize;
    private final int sortedSize;
    private final long runtimeNanos;

    public BenchmarkRecord(String algorithmName, int inputSize, int sortedSize, long runtimeNanos) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.sortedSize = sortedSize;
        this.runtimeNanos = runtimeNanos;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getSortedSize() {
        return sortedSize;
    }

    public long getRuntimeNanos() {
        return runtimeNanos;
    }
}
