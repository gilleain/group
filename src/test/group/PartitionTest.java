package test.group;

import group.Partition;

import org.junit.Test;

public class PartitionTest {
    
    @Test
    public void parseTest() {
        Partition p = Partition.fromString("[0,2|1,3]");
        System.out.println(p);
    }

}
