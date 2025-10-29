package algorithms.search;
import java.util.ArrayList;
import java.util.LinkedList;

public interface ISearchable {
    ArrayList<AState> getAllPossibleStates(AState state);
    AState getStartState();
    AState getGoalState();

}
