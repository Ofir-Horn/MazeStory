package algorithms.search;
import algorithms.mazeGenerators.Position;

import java.io.Serializable;

public class MazeState extends AState implements Serializable{
    private Position position;

    public MazeState(Position position, int cost, AState cameFrom) {
        super(position, cost, cameFrom);
        this.position = position;
    }
    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "state=" + position +
                ", cost=" + getCost() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        MazeState other = (MazeState) obj;
        return (this.position.getRowIndex() == other.position.getRowIndex() && this.position.getColumnIndex() == other.position.getColumnIndex());
    }


}
