package test.combinatorics;

import group.Partition;

import org.junit.Test;

import combinatorics.PartitionCalculator;

public class PartitionCalculatorTest {
    
    @Test
    public void test_17_5() {
        test(17, 5);
    }
    
    @Test
    public void test_18_8() {
        test(18, 8);
    }
    
    public void test(int m, int n) {
        Partition[] partitions = PartitionCalculator.partition(m, n);
        for (int rank = 0; rank < partitions.length; rank++) {
            System.out.println(rank + " " + partitions[rank]);
        }
    }
    
}
