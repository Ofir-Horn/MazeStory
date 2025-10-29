package algorithms.search;
import org.junit.jupiter.api.Test;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
class BestFirstSearchTest {

    @Test
    public void BFSNullInputTesting() {
        ISearchingAlgorithm BFS = new BestFirstSearch();
        assertNull((BFS.solve(null)));
    }

    @Test
    public void BFSCheckGetName() {
        ISearchingAlgorithm BFS = new BestFirstSearch();
        assertEquals("BestFirstSearch", BFS.getName());
    }

    @Test
    public void BFSCheckDefaultNumNodes() {
        ISearchingAlgorithm BFS = new BestFirstSearch();
        assertEquals(0, BFS.getNumberOfNodesEvaluates());
    }

    @Test
    public void BFSCheckMinimumMazeOne(){
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(1,1);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        solveProblem(searchableMaze, new BestFirstSearch());
    }

    private static void solveProblem(ISearchable domain, ISearchingAlgorithm searcher){
        //Solve a searching problem with a searcher
        Solution solution = searcher.solve(domain);
        System.out.println(String.format("'%s algorithm - nodes evaluated: %s", searcher.getName(),
                searcher.getNumberOfNodesEvaluates()));
        //Printing Solution Path
        System.out.println("Solution path:");
        ArrayList<AState> solutionPath = solution.getSolutionPath();
        for (int i = 0; i < solutionPath.size(); i++){
            System.out.println(String.format("%s.%s", i, solutionPath.get(i)));
        }
        assertEquals(1, solution.getSolutionPath().size());
    }
}

