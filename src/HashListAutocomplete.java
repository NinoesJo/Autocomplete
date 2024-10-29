import java.util.*;

public class HashListAutocomplete implements Autocompletor {
    
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}

		if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
        initialize(terms, weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        // TODO Auto-generated method stub
        if (k < 0) {
            throw new IllegalArgumentException("Illegal value of k: " + k);
        }
        prefix = prefix.substring(0, Math.min(MAX_PREFIX, prefix.length()));
        List<Term> all = myMap.getOrDefault(prefix, new ArrayList<>());
        List<Term> list = all.subList(0, Math.min(k, all.size()));
        return list;
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        // TODO Auto-generated method stub
        myMap = new HashMap<>();
        for (int i = 0; i < terms.length; i++) {
            if (weights[i] >= 0) {
                Term t = new Term(terms[i], weights[i]);
                int minimum = Math.min(MAX_PREFIX, t.getWord().length()); 
                for (int j = 0; j <= minimum; j++) {
                    List<Term> listT = myMap.get(t.getWord().substring(0, j));
                    if (listT == null) {
                        listT = new ArrayList<>();
                        myMap.put(t.getWord().substring(0, j), listT);
                    }
                    listT.add(t);
                }
            }
        }
        for (List<Term> list: myMap.values()) {
            Collections.sort(list, Comparator.comparing(Term::getWeight).reversed());
        }
    }

    @Override
    public int sizeInBytes() {
        // TODO Auto-generated method stub
        int mySize = 0;
        for (Map.Entry<String, List<Term>> entry : myMap.entrySet()) {
            mySize += entry.getKey().length() * BYTES_PER_CHAR;
            List<Term> termL = entry.getValue();
            for (Term term : termL) {
                mySize += BYTES_PER_DOUBLE + (term.getWord().length() * BYTES_PER_CHAR);
            }
        }
        return mySize;
    }  
}

