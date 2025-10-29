package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
    private final ArrayList<AState> path;
    private int cost;

    public Solution(ArrayList<AState> path) {
        this.path = path;
    }

    public ArrayList<AState> getSolutionPath() {
        return this.path;
    }

}
