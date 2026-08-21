package com.campus.optimizer.db;

import com.campus.optimizer.sort.BenchmarkHarness;
import com.campus.optimizer.sort.BenchmarkRecord;

public class AlgorithmRunRecorderDemo {
    public static void main(String[] args) {
        AlgorithmRunDAO dao = new AlgorithmRunDAO();
        AlgorithmRunRecorder recorder = new AlgorithmRunRecorder(dao);

        int[] sample = {5, 3, 8, 1, 9, 2};
        BenchmarkRecord record = BenchmarkHarness.benchmark(
            "Bubble Sort Demo", sample, arr -> {
                java.util.Arrays.sort(arr);
                return arr;
            }
        );

        recorder.record(record);
        System.out.println("Persisted: " + dao.findLatest(1));
    }
}
