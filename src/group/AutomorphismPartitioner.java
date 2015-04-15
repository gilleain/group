package group;

import java.util.List;

import combinatorics.DisjointSetForest;

/**
 * Get the automorphism partition of a group.
 * 
 * @author maclean
 *
 */
public class AutomorphismPartitioner {
    
    /**
     * The automorphism partition is a partition of the elements of the group.
     * 
     * @return a partition of the elements of group 
     */
    public static Partition getAutomorphismPartition(PermutationGroup group) {
        int n = group.getSize();
        boolean[] inOrbit = new boolean[n];
        List<Permutation> permutations = group.all();
        DisjointSetForest forest = new DisjointSetForest(n);
        int inOrbitCount = 0;
        for (Permutation p : permutations) {
            for (int i = 0; i < n; i++) {
                if (inOrbit[i]) {
                    continue;
                } else {
                    int x = p.get(i);
                    while (x != i) {
                        if (!inOrbit[x]) {
                            inOrbitCount++;
                            inOrbit[x] = true;
                            forest.makeUnion(i, x);
                        }
                        x = p.get(x);
                    }
                }
            }
            if (inOrbitCount == n) {
                break;
            }
        }
        
        // convert to a partition
        Partition partition = new Partition();
        for (int[] set : forest.getSets()) {
            partition.addCell(set);
        }
        
        // necessary for comparison by string
        partition.order();  
        return partition;
    }

}
