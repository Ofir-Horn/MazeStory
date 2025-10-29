package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class AState implements Serializable{
    //private final Object state;
    private final transient Position state;
    private final int cost;
    private AState cameFrom;
    // TODO: fix for "stream classdesc serialVersionUID = 3584168432785041762, local class serialVersionUID = 3079203568953422235"
    private static final long serialVersionUID = 3584168432785041762L;

    public AState(Object state, int cost, AState cameFrom) {
        //this.state = state;
        if (!(state instanceof Position)) {
            throw new IllegalArgumentException("The state object must be an instance of Position.");
        }
        this.state = (Position) state;
        this.cost = cost;
        this.cameFrom = cameFrom;
    }


    public Object getState() {
        return state;
    }

    public int getCost() {
        return cost;
    }

    public void setCameFrom(AState cameFrom) {
        this.cameFrom = cameFrom;
    }

    public AState getCameFrom() {
        return cameFrom;
    }


    public abstract Position getPosition();

    @Override
    public String toString() {
        return "state=" + state +
                ", cost=" + cost +
                '}';
    }

    public String getString() {
        return "position=" + state;
    }

}
