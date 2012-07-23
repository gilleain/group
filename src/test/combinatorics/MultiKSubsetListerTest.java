package test.combinatorics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import combinatorics.KSubsetLister;
import combinatorics.MultiKSubsetLister;

public class MultiKSubsetListerTest {
    
    public List<Integer> getNumbers(int n) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= n; i++) {
            numbers.add(i);
        }
        return numbers;
    }
    
    @Test
    public void unrankTest() {
        int n = 10;
        int k = 3;
        MultiKSubsetLister<Integer> lister = new MultiKSubsetLister<Integer>(k, getNumbers(n));
        List<Integer> subSet = lister.lexUnrank(10);
        System.out.println(subSet);
    }
    
    @Test
    public void unrankAllTest() {
        int n = 8;
        int k = 3;
        MultiKSubsetLister<Integer> lister = new MultiKSubsetLister<Integer>(k, getNumbers(n));
        for (int rank = 0; rank < KSubsetLister.choose(n + k - 1, k); rank++) {
        	List<Integer> subSet = lister.lexUnrank(rank);
        	System.out.println(rank + "\t" + subSet);
        }
    }
    
}
