package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution solution = null;
    private int row_player =0;
    private int col_player =0;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNamepath = new SimpleStringProperty();
    private Timeline animation;
    private Image[] playerImages;
    private Image[] goalImages;
    private Image[] pathImages;
    private int currentPlayerImageIndex;
    private int currentGoalImageIndex;
    private int currentPathImageIndex;

    public MazeDisplayer() {
        playerImages = new Image[7];
        goalImages = new Image[4];
        pathImages = new Image[4];
        try {
            playerImages[0] = new Image(new FileInputStream("src/main/resources/Images/cat1.gif"));
            playerImages[1] = new Image(new FileInputStream("src/main/resources/Images/cat2.gif"));
            playerImages[2] = new Image(new FileInputStream("src/main/resources/Images/cat3.gif"));
            playerImages[3] = new Image(new FileInputStream("src/main/resources/Images/cat4.gif"));
            playerImages[4] = new Image(new FileInputStream("src/main/resources/Images/cat5.gif"));
            playerImages[5] = new Image(new FileInputStream("src/main/resources/Images/cat6.gif"));
            playerImages[6] = new Image(new FileInputStream("src/main/resources/Images/cat7.gif"));

            goalImages[0] = new Image(new FileInputStream("src/main/resources/Images/annable1.gif"));
            goalImages[1] = new Image(new FileInputStream("src/main/resources/Images/annable2.gif"));
            goalImages[2] = new Image(new FileInputStream("src/main/resources/Images/annable3.gif"));
            goalImages[3] = new Image(new FileInputStream("src/main/resources/Images/annable4.gif"));

            pathImages[0] = new Image(new FileInputStream("src/main/resources/Images/meso1.gif"));
            pathImages[1] = new Image(new FileInputStream("src/main/resources/Images/meso2.gif"));
            pathImages[2] = new Image(new FileInputStream("src/main/resources/Images/meso3.gif"));
            pathImages[3] = new Image(new FileInputStream("src/main/resources/Images/meso4.gif"));
        } catch (FileNotFoundException e) {
            System.out.println("Error loading images");
        }

        currentPlayerImageIndex = 0;
        currentGoalImageIndex = 0;
        currentPathImageIndex = 0;

        animation = new Timeline(
                new KeyFrame(Duration.seconds(0.25), event -> {
                    currentPlayerImageIndex = (currentPlayerImageIndex + 1) % playerImages.length;
                    currentGoalImageIndex = (currentGoalImageIndex + 1) % goalImages.length;
                    currentPathImageIndex = (currentPathImageIndex + 1) % pathImages.length;
                    draw();
                })
        );
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    public void startAnimation() {
        animation.play();
    }

    public void stopAnimation() {
        animation.stop();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String getImageFileNamepath() {
        return imageFileNamepath.get();
    }

    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
        draw();

    }

    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        draw();
    }

    public void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.getMaze().length;
            int col = maze.getMaze()[0].length;
            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w, h;

            // Draw Maze
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream("src/main/resources/Images/wall.jpg"));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }
            Image goalImage = goalImages[currentGoalImageIndex];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (maze.getMaze()[i][j] == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null) {
                            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        } else {
                            graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                        }
                    }
                    if (maze.getMaze()[i][j] == 3) {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        graphicsContext.drawImage(goalImage, w, h, cellWidth, cellHeight);
                    }
                }
            }

            if (solution != null) {
                int rowSol, colSol;
                for (int i = 0; i < solution.getSolutionPath().size(); i++) {
                    rowSol = solution.getSolutionPath().get(i).getPosition().getRowIndex();
                    colSol = solution.getSolutionPath().get(i).getPosition().getColumnIndex();
                    if (maze.getMaze()[rowSol][colSol] == 3) {
                        continue;
                    }

                    h = rowSol * cellHeight;
                    w = colSol * cellWidth;
                    Image pathSolutionImage = pathImages[currentPathImageIndex];
                    graphicsContext.drawImage(pathSolutionImage, w + (cellWidth / 4), h + (cellHeight / 4), (cellWidth / 2), (cellHeight / 2));
                }
            }

            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = playerImages[currentPlayerImageIndex];
            graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight);
        }
    }

    public void restart() {
        solution = null;
        getGraphicsContext2D().clearRect(0, 0, getHeight(), getWidth());
    }
}
