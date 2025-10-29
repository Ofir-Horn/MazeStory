package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze implements Serializable {
    private final int[][] maze;
    private final int rows;
    private final int columns;
    private final Position startPosition;
    private final Position goalPosition;

    public Maze(int rows, int columns) {
        this.maze = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.startPosition = new Position(0,0);
        this.goalPosition = new Position(rows - 1,columns - 1);
        this.maze[0][0] = 2;
        this.maze[rows - 1][columns - 1] = 3;
    }

    //Constructor from byteArray
    //This constructor recieves an input of a byteArray, and builds the maze accordingly.
    public Maze(byte[] byteArray) {
        int startPositionRow = convertToInt(byteArray[0], byteArray[1]);
        int startPositionColumn = convertToInt(byteArray[2], byteArray[3]);
        int goalPositionRow = convertToInt(byteArray[4], byteArray[5]);
        int goalPositionColumn = convertToInt(byteArray[6], byteArray[7]);
        int rows = convertToInt(byteArray[8], byteArray[9]);
        int columns = convertToInt(byteArray[10], byteArray[11]);
        this.maze = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
        int count = 12;
        // Insert maze walls / open-spaces into the maze 2d array
        for (int i = 0; i < this.maze.length; i++){
            for (int j = 0; j < this.maze[0].length; j++){
                this.maze[i][j] = byteArray[count++];
            }
        }
        // Set values (2 / 3) for the start and goal positions
        this.startPosition = new Position(startPositionRow, startPositionColumn);
        this.goalPosition = new Position(goalPositionRow, goalPositionColumn);
        this.maze[startPositionRow][startPositionColumn] = 2;
        this.maze[goalPositionRow][goalPositionColumn] = 3;

    }


    public int getValueIndex(int row, int column) {
        return maze[row][column];
    }

    public void setValueIndex(int row, int column, int val) {
        if (rows <= row || columns <= column)
            return;
        maze[row][column] = val;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getGoalPosition() {
        return goalPosition;
    }

    public int[][] getMaze() {
        return maze;
    }

    //Method: getNeighbors
    //This method returns an array holding the Positions's of the neighbors for the current position
    public ArrayList<Position> getNeighbors(Position position) {
        ArrayList<Position> neighborsPositions = new ArrayList<>();
        if (isValid(position)){
            //TODO if valid - IMPORTENT!
            Position neighborUp = position.getUp().getUp();
            if (isValid(neighborUp) && this.maze[neighborUp.getRowIndex()][neighborUp.getColumnIndex()] == 1){
                neighborsPositions.add(neighborUp);
            }
            Position neighborRight = position.getRight().getRight();
            if (isValid(neighborRight) && this.maze[neighborRight.getRowIndex()][neighborRight.getColumnIndex()] == 1){
                neighborsPositions.add(neighborRight);
            }
            Position neighborDown = position.getDown().getDown();
            if (isValid(neighborDown) && this.maze[neighborDown.getRowIndex()][neighborDown.getColumnIndex()] == 1){
                neighborsPositions.add(neighborDown);
            }
            Position neighborLeft = position.getLeft().getLeft();
            if (isValid(neighborLeft) && this.maze[neighborLeft.getRowIndex()][neighborLeft.getColumnIndex()] == 1){
                neighborsPositions.add(neighborLeft);
            }
        }
        return neighborsPositions;
    }

    public boolean isValid(Position position){
        boolean valid = false;
        if (position != null && position.getColumnIndex() >= 0 && position.getColumnIndex() < this.columns && 0 <= position.getRowIndex() && position.getRowIndex() < this.rows ){
            valid = true;
        }
        return valid;
    }

    public boolean isWall(Position position){
        boolean wall = false;
        if (position != null && this.maze[position.getRowIndex()][position.getColumnIndex()] == 1)
            wall = true;
        return wall;
    }

    public byte[] toByteArray() {
        byte[] byteArray = new byte[(this.rows * this.columns) + 12];
        //byteArray[0] + byteArray[1]: startPosition Row
        byte[] startPositionRow = convertToBase(this.startPosition.getRowIndex());
        byteArray[0] = startPositionRow[0];
        byteArray[1] = startPositionRow[1];
        //byteArray[2] + byteArray[3]: startPosition Column
        byte[] startPositionColumn = convertToBase(this.startPosition.getColumnIndex());
        byteArray[2] = startPositionColumn[0];
        byteArray[3] = startPositionColumn[1];
        //byteArray[4] + byteArray[5]: goalPosition Row
        byte[] goalPositionRow = convertToBase(this.goalPosition.getRowIndex());
        byteArray[4] = goalPositionRow[0];
        byteArray[5] = goalPositionRow[1];
        //byteArray[6] + byteArray[7]: goalPosition Column
        byte[] goalPositionColumn = convertToBase(this.goalPosition.getColumnIndex());
        byteArray[6] = goalPositionColumn[0];
        byteArray[7] = goalPositionColumn[1];
        //byteArray[8] + byteArray[9]: Total number of Rows
        byte[] numberOfRows = convertToBase(this.rows);
        byteArray[8] = numberOfRows[0];
        byteArray[9] = numberOfRows[1];
        //byteArray[10] + byteArray[11]: Total number of Columns
        byte[] numberOfColumns = convertToBase(this.columns);
        byteArray[10] = numberOfColumns[0];
        byteArray[11] = numberOfColumns[1];
        //byteArray[12...+]: Maze data

        int count = 12;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                byteArray[count++] = (byte) this.maze[i][j];
            }
        }


        return byteArray;
    }

    public byte[] convertToBase(int number) {
        byte[] base = new byte[2];
        int leastSignificantBitInt = number % 127;
        int mostSignificantBitInt = (number / 127) % 127;
        byte leastSignificantBitByte = (byte) leastSignificantBitInt;
        byte mostSignificantBitByte = (byte) mostSignificantBitInt;
        base[0] = leastSignificantBitByte;
        base[1] = mostSignificantBitByte;
        return base;
    }

    public int convertToInt(byte leastSignificantBitByte, byte mostSignificantBitByte){
        int answerInt = (mostSignificantBitByte * 127) + leastSignificantBitByte;
        return answerInt;
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            System.out.print("[");
            for (int j = 0; j < columns; j++) {
                if (maze[i][j] == 1) {
                    System.out.print("1,"); // wall
                } else if (maze[i][j] == 0) {
                    System.out.print("0,"); // empty space
                } else if (maze[i][j] == 2) {
                    System.out.print("S,"); // start
                } else if (maze[i][j] == 3) {
                    System.out.print("E,"); // end
                } else if (maze[i][j] == 5) {
                    System.out.print("T,"); // TEST
                }
            }
            System.out.println("]"); // move to next row
        }
    }
}
