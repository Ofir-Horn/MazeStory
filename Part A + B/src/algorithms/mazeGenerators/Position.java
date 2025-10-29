package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable {
    private int columns;
    private int rows;

    public Position(int row, int colum) {
        this.columns = colum;
        this.rows = row;
    }

    public int getColumnIndex()
    {
        return this.columns;
    }

    public int getRowIndex()
    {
        return this.rows;
    }

    public void setRowIndex(int row) {
        this.rows = row;
    }

    public void setColumnIndex(int column) {
        this.columns = column;
    }

    public Position getUp(){
        Position upPosition = new Position(this.rows - 1,this.columns);
        return upPosition;
    }

    public Position getDown(){
        Position downPosition = new Position(this.rows + 1,this.columns);
        return downPosition;
    }

    public Position getRight(){
        Position rightPosition = new Position(this.rows,this.columns + 1);
        return rightPosition;
    }

    public Position getLeft(){
        Position leftPosition = new Position(this.rows,this.columns - 1);
        return leftPosition;
    }


    @Override
    public String toString() {
        return String.format("{%d,%d}", rows, columns);
    }

}
