package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;

public class MyViewController implements Initializable,Observer {
    private MyViewModel viewModel;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label lbl_player_row;
    @FXML
    public Label lbl_player_column;
    @FXML
    public StackPane gameBoard;
    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();
    private Maze maze;
    public Alerts alerts = new Alerts();
    private final FileChooser fileChooser = new FileChooser();
    private MediaPlayer mediaPlayer;
    @FXML
    private ProgressBar progressBar;
    private double mouseX;
    private double mouseY;
    private double translateX;
    private double translateY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
        gameBoard.setOnScroll(this::handleScroll);
        gameBoard.setOnMousePressed(this::handleMousePressed);
        gameBoard.setOnMouseDragged(this::handleMouseDragged);
        gameBoard.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMousePressed(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    private void handleMouseDragged(MouseEvent event) {
        double deltaX = event.getX() - mouseX;
        double deltaY = event.getY() - mouseY;
        translateX += deltaX;
        translateY += deltaY;
        gameBoard.setTranslateX(gameBoard.getTranslateX() + deltaX);
        gameBoard.setTranslateY(gameBoard.getTranslateY() + deltaY);
        mouseX = event.getX();
        mouseY = event.getY();
    }

    private void handleMouseReleased(MouseEvent event) {
        mouseX = 0;
        mouseY = 0;
        translateX = 0;
        translateY = 0;
    }

    private void restartMaze() {
        if (maze == null) {
            alerts.showError("There is no maze to restart.");
            return;
        }
        viewModel.restartPosition();
        mazeDisplayer.set_player_position(viewModel.getRowChar(), viewModel.getColChar());
        set_update_player_position_row(viewModel.getRowChar() + "");
        set_update_player_position_col(viewModel.getColChar() + "");
        progressBar.setProgress(0.0);
        mazeDisplayer.requestFocus();
    }


    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            double zoomFactor = 1.2;
            Point2D mousePosition = gameBoard.sceneToLocal(event.getSceneX(), event.getSceneY());

            double newScaleX;
            double newScaleY;
            if (deltaY > 0) {
                newScaleX = gameBoard.getScaleX() * zoomFactor;
                newScaleY = gameBoard.getScaleY() * zoomFactor;
            } else {
                newScaleX = gameBoard.getScaleX() / zoomFactor;
                newScaleY = gameBoard.getScaleY() / zoomFactor;
            }

            double pivotX = mousePosition.getX() / gameBoard.getWidth();
            double pivotY = mousePosition.getY() / gameBoard.getHeight();
            gameBoard.setScaleX(newScaleX);
            gameBoard.setScaleY(newScaleY);
            gameBoard.setTranslateX(gameBoard.getTranslateX() - (pivotX * (newScaleX - gameBoard.getScaleX()) * gameBoard.getWidth()));
            gameBoard.setTranslateY(gameBoard.getTranslateY() - (pivotY * (newScaleY - gameBoard.getScaleY()) * gameBoard.getHeight()));

            event.consume();
        }
    }


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }


    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }


    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }


    public void generateMazeButton(){
        if (maze != null){
            alerts.showError("You cannot generate a maze while there is already one.\n" +
                    "Please start a new game instead.");
            return;
        }
        generateMaze();
    }

    public void generateMaze()
    {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        if (rows < 2 || cols < 2){
            showError("A maze cannot be smaller than 2x2." +
                    "\nPlease try again.");
            return;
        }
        viewModel.generateMaze(rows,cols);
        mazeDisplayer.startAnimation();

    }

    public void solveMaze()
    {
        viewModel.solveMaze(this.maze);
    }


    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }

    public void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.R) {
            restartMaze();
        } else {
            viewModel.moveCharacter(keyEvent);
        }
        keyEvent.consume();
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null)
            {
                this.maze = viewModel.getMaze();
                drawMaze();
            }
            else {
                Maze maze = viewModel.getMaze();

                Solution solution = viewModel.getSolution();
                mazeDisplayer.setSolution(solution);
                drawMaze();

                if (maze == this.maze)
                {
                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = viewModel.getRowChar();
                    int colFromViewModel = viewModel.getColChar();

                    if(rowFromViewModel == rowChar && colFromViewModel == colChar)//Solve Maze
                    {
                        playMeow();
                    }
                    else
                    {
                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                        progressBar.setProgress(viewModel.calculateProgress());
                        if (maze.getMaze()[rowFromViewModel][colFromViewModel] == 3){
                            stopMusic();
                            try {
                                solved();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }


                }
                else
                {
                    this.maze = maze;
                    drawMaze();
                }
            }
        }
    }

    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }

    public void About() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("About the Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/About.fxml").openStream());
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void Help() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Game Instructions");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/Help.fxml").openStream());
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void Properties() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Properties");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/Properties.fxml").openStream());
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        PropertiesController propertiesController = fxmlLoader.getController();
        propertiesController.setMediaPlayer(this.mediaPlayer);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void solved() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Congratulations!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/Solved.fxml").openStream());
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        SolvedController solved = fxmlLoader.getController();
        solved.setMyViewController(this);
        solved.setSolvedStage(stage);
        solved.playMusic();

    }

    public void saveMaze() {
        if (maze == null) {
            showAlert("You must first generate a maze to be able to save it.");
            return;
        }

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) mazeDisplayer.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String filePath = file.getPath();
            viewModel.saveMazeToFile(maze, filePath);
            showAlert("Maze saved successfully.");
        }
    }

    public void loadMaze() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) mazeDisplayer.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String filePath = file.getPath();
            Maze loadedMaze = viewModel.loadMazeFromFile(filePath);
            if (loadedMaze != null) {
                this.maze = loadedMaze;
                drawMaze();
                showAlert("Maze loaded successfully.");
            } else {
                showAlert("Failed to load the maze from the file.");
            }
        }
    }


    public void newGame() {
        mazeDisplayer.restart();
        viewModel.restartPosition();
        generateMaze();
    }

    public void playMeow() {
        String musicFilePath = getClass().getResource("/Clips/meow.mp3").toString();
        Media media = new Media(musicFilePath);
        final MediaPlayer[] meowPlayer = {new MediaPlayer(media)};

        meowPlayer[0].setOnEndOfMedia(() -> {
            meowPlayer[0].dispose();
            meowPlayer[0] = null;
        });

        double backgroundMusicVolume = mediaPlayer.getVolume();

        final double reducedVolume = backgroundMusicVolume * 0.8;
        mediaPlayer.setVolume(reducedVolume);

        meowPlayer[0].play();

        meowPlayer[0].setOnStopped(() -> mediaPlayer.setVolume(backgroundMusicVolume));
    }

    public void playMusic() {
        stopMusic();
        String musicFilePath = getClass().getResource("/Clips/background.mp3").toString();
        Media media = new Media(musicFilePath);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    public void exitGame() {
        viewModel.exitGame();
        System.exit(0);
    }
}


