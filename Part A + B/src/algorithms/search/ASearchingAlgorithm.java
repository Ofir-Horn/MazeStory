package algorithms.search;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    public int NumberOfVisitedNodes;

    public ASearchingAlgorithm() {
        this.NumberOfVisitedNodes = 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }


}
