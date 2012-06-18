package test.combinatorics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import combinatorics.SubsetLister;

public class SubsetListerTest {
    
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
        SubsetLister<Integer> lister = new SubsetLister<Integer>(getNumbers(n));
        List<Integer> subSet = lister.lexUnrank(10);
        System.out.println(subSet);
    }
    
    @Test
    public void naturalNumberSet() {
        int n = 8;
        SubsetLister<Integer> lister = new SubsetLister<Integer>(getNumbers(n));
        int r = 0;
        for (List<Integer> subSet : lister) {
            System.out.println(r + "\t" + subSet);
            r++;
        }
    }
    
}
