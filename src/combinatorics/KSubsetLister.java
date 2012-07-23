package combinatorics;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

/**
 * List all the k-subsets of size k of a set. From CAGES pages ??.
 * 
 * @author maclean
 *
 * @param k the size of the subset
 * @param <T> the type of the elements of the set
 */
public class KSubsetLister<T> implements Iterable<List<T>> {
    
    private final List<T> elements;
    
    private final int size;
    
    private final int k;
    
    public KSubsetLister(int k, List<T> elements) {
        this.elements = elements;
        this.size = elements.size();
        this.k = k;
    }
    
    // TODO : more efficiently, with multiplicative formula
    public static int choose(int n, int k) {
    	return f(n) / (f(k) * f(n - k));
    }
    
    // TODO : remove!!
    private static int f(int n) {
    	return (n <= 1)? 1 : n * f(n - 1);
    }
    
    public int lexRank(BitSet subSetVector) {
       return -1;	// TODO!
    }
    
    public List<T> lexUnrank(int rank) {
    	int x = 1;
    	List<T> t = new ArrayList<T>();
    	for (int i = 1; i <= k; i++) {
    		
    		while (choose(size - x, k - i) <= rank) {
    			rank -= choose(size - x, k - i);
    			x += 1;
    		}
    		t.add(elements.get(x - 1));
    		x += 1;
    	}
    	return t;
    }

    public Iterator<List<T>> iterator() {
        final int maxRank = choose(size, k);
        
        return new Iterator<List<T>>() {
            int rank = 0;

            public boolean hasNext() {
                return rank < maxRank;
            }

            public List<T> next() {
                List<T> nextSubSet = lexUnrank(rank);
                rank++;
                return nextSubSet;
            }

            public void remove() {
                rank++;
            }
        };
    }
    
    public static KSubsetLister<Integer> getIndexLister(int k, int n) {
    	List<Integer> indices = new ArrayList<Integer>();
    	for (int i = 0; i < n; i++) {
    		indices.add(i);
    	}
    	return new KSubsetLister<Integer>(k, indices);
    }
}
