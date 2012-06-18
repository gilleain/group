package test.group;

import group.Permutation;

import org.junit.Test;

public class PermutationTest {
    
    @Test
    public void fromCycleStringTest() {
        String cycleString = "(1,4)";
        Permutation p = Permutation.fromCycleString(cycleString, 4);
        System.out.println(p);
    }

}
