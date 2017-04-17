package test.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import group.AbstractEquitablePartitionRefiner;
import group.Partition;

public class TestEquitablePartitionRefiner {
    
    private class Lookup {
        public Set<Integer> block;
        public int vertex;
        public int count;
        
        public Lookup(Set<Integer> block, int vertex, int count) {
            this.block = block;
            this.vertex = vertex;
            this.count = count;
        }
    }
    
    private class Impl extends AbstractEquitablePartitionRefiner {
        
        private List<Lookup> neighbourCounts;
        
        public Impl(List<Lookup> neighbourCounts) {
            this.neighbourCounts = neighbourCounts;
        }

        @Override
        public int getNumberOfVertices() {
            return 9;
        }

        @Override
        public int neighboursInBlock(Set<Integer> block, int vertexIndex) {
            for (Lookup lookup : neighbourCounts) {
                if (lookup.block.equals(block) && lookup.vertex == vertexIndex) {
                    return lookup.count;
                }
            }
            return 0;   // default - only have to provide nonzero values 
        }
        
    }
    
    private Set<Integer> make(int... elements) {
        Set<Integer> set = new HashSet<>();
        for (int element : elements) {
            set.add(element);
        }
        return set;
    }
    
    @Test
    public void testRefine() {
        List<Lookup> map = new ArrayList<>();
        
        map.add(new Lookup(make(0), 1, 1));
        map.add(new Lookup(make(0), 3, 1));
        
        map.add(new Lookup(make(1, 3), 2, 1));
        map.add(new Lookup(make(1, 3), 6, 1));
        
        map.add(new Lookup(make(2, 6, 8), 1, 1));
        map.add(new Lookup(make(2, 6, 8), 3, 1));
        map.add(new Lookup(make(2, 6, 8), 5, 2));
        map.add(new Lookup(make(2, 6, 8), 7, 2));
        
        map.add(new Lookup(make(4), 1, 1));
        map.add(new Lookup(make(4), 3, 1));
        map.add(new Lookup(make(4), 5, 1));
        map.add(new Lookup(make(4), 7, 1));
        
        map.add(new Lookup(make(5, 7), 2, 1));
        map.add(new Lookup(make(5, 7), 6, 1));
        map.add(new Lookup(make(5, 7), 8, 2));
        
        map.add(new Lookup(make(8), 5, 1));
        map.add(new Lookup(make(8), 7, 1));
        
        Impl impl = new Impl(map);
        Partition a = Partition.fromString("0|2,6,8|5,7|1,3|4");
        Partition b = impl.refine(a);
        System.out.println(b);
        assertEquals(b, Partition.fromString("0|8|2,6|5,7|1,3|4"));
    }

}
