package algorithms.search;

import java.util.Comparator;
import java.util.*;

public class BestFirstSearch extends BreadthFirstSearch{
    public PriorityQueue<AState> AStatePriority;

    public BestFirstSearch() {
        super(new PriorityQueue<>(new AStateComparator()));
    }
}

    //this comparator is for BestFirstSearch to compere between the costs the AState
    class AStateComparator implements Comparator<AState>
    {
        @Override
        public int compare(AState o1, AState o2) {
            int comp = Integer.compare(o1.getCost(),o2.getCost());
            return comp;
        }
    }




