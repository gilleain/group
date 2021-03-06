package group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import group.invariant.Invariant;


/**
 * Refines a 'coarse' partition (with more blocks) to a 'finer' partition that
 * is equitable.
 * 
 * Closely follows algorithm 7.5 in CAGES. The basic idea is that the refiner
 * maintains a queue of blocks to refine, starting with all the initial blocks
 * in the partition to refine. These blocks are popped off the queue, and
 * 
 * @author maclean
 * @cdk.module group
 */
public abstract class AbstractEquitablePartitionRefiner {
    
    /**
     * A forward split order tends to favor partitions where the cells are
     * refined from lowest to highest. A reverse split order is, of course, the
     * opposite.
     * 
     */
    public enum SplitOrder { FORWARD, REVERSE };
    
    /**
     * The bias in splitting cells when refining
     */
    private SplitOrder splitOrder = SplitOrder.FORWARD;
    
    /**
     * The block of the partition that is being refined
     */
    private int currentBlockIndex;
    
    /**
     * The blocks to be refined, or at least considered for refinement
     */
    private Queue<Set<Integer>> blocksToRefine;
    
    /**
     * Gets from the graph the number of vertices. Abstract to allow different 
     * graph classes to be used (eg: IMolecule or IAtomContainer, etc).
     * 
     * @return the number of vertices
     */
    public abstract int getNumberOfVertices();
    
    /**
     * Find |a &cap; b| - that is, the size of the intersection between a and b.
     * 
     * @param block a set of numbers
     * @param vertexIndex the element to compare
     * @return the size of the intersection
     */
    public abstract Invariant neighboursInBlock(Set<Integer> block, int vertexIndex);

    /**
     * Set the preference for splitting cells.
     * 
     * @param splitOrder either FORWARD or REVERSE
     */
    public void setSplitOrder(SplitOrder splitOrder) {
        this.splitOrder = splitOrder;
    }
    
    /**
     * Refines the coarse partition <code>a</code> into a finer one.
     *  
     * @param a the partition to refine
     * @return a finer partition
     */
    public Partition refine(Partition a) {
        Partition b = new Partition(a);
//        System.out.println("Refining " + a);
        
        // start the queue with the blocks of a in reverse order
        blocksToRefine = new LinkedList<Set<Integer>>();
        for (int i = 0; i < b.size(); i++) {
            blocksToRefine.add(b.copyBlock(i));
        }
        
        int numberOfVertices = getNumberOfVertices();
        while (!blocksToRefine.isEmpty()) {
            Set<Integer> t = blocksToRefine.remove();
            currentBlockIndex = 0;
            while (currentBlockIndex < b.size() && b.size() < numberOfVertices) {
                if (!b.isDiscreteCell(currentBlockIndex)) {

                    // get the neighbor invariants for this block
                    Map<Invariant, SortedSet<Integer>> invariants = getInvariants(b, t);

                    // split the block on the basis of these invariants
                    split(invariants, b);
//                    System.out.println(blocksToRefine);
                }
                currentBlockIndex++;
            }
//            System.out.println(b);

            // the partition is discrete
            if (b.size() == numberOfVertices) {
                return b;
            }
        }
//        System.out.println("To " + b);
        return b;
    }
    
    /**
     * Gets the neighbor invariants for the block j as a map of 
     * |N<sub>g</sub>(v) &cap; T| to elements of the block j. That is, the
     * size of the intersection between the set of neighbors of element v in
     * the graph and the target block T. 
     *  
     * @param partition the current partition
     * @param targetBlock the current target block of the partition
     * @return a map of set intersection sizes to elements
     */
    private Map<Invariant, SortedSet<Integer>> getInvariants(
            Partition partition, Set<Integer> targetBlock) {
        Map<Invariant, SortedSet<Integer>> setList = new HashMap<Invariant, SortedSet<Integer>>();
        for (int u : partition.getCell(currentBlockIndex)) {
            Invariant h = neighboursInBlock(targetBlock, u);
            if (setList.containsKey(h)) {
                setList.get(h).add(u);
            } else {
                SortedSet<Integer> set = new TreeSet<Integer>();
                set.add(u);
                setList.put(h, set);
            }
        }
//        System.out.println("current block " + partition.getCell(currentBlockIndex) + " target block " + targetBlock + " inv " + setList);
        return setList;
    }
    
    /**
     * Split the current block using the invariants calculated in getInvariants.
     * 
     * @param invariants a map of neighbor counts to elements
     * @param partition the partition that is being refined
     */
    private void split(Map<Invariant, SortedSet<Integer>> invariants, Partition partition) {
        int nonEmptyInvariants = invariants.keySet().size();
        if (nonEmptyInvariants > 1) {
            List<Invariant> invariantKeys =  new ArrayList<Invariant>();
            invariantKeys.addAll(invariants.keySet());
            partition.removeCell(currentBlockIndex);
            int k = currentBlockIndex;
            if (splitOrder == SplitOrder.REVERSE) {
                Collections.sort(invariantKeys);
            } else {
                Collections.sort(invariantKeys, Collections.reverseOrder());
            }
            for (Invariant h : invariantKeys) {
                SortedSet<Integer> setH = invariants.get(h);
//                System.out.println("adding block " + setH + " at " + k + " h=" + h);
                partition.insertCell(k, setH);
                blocksToRefine.add(setH);
                k++;
                
            }
            // skip over the newly added blocks
            currentBlockIndex += nonEmptyInvariants - 1;
        }
    }

}
