package View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class SolvedController {
    private MediaPlayer mediaPlayer;
    private MyViewController myViewController;
    private Stage solvedStage;

    public void setMyViewController(MyViewController myViewController) {
        this.myViewController = myViewController;
    }

    public void setSolvedStage(Stage solvedStage) {
        this.solvedStage = solvedStage;
    }

    public void playMusic() {
        stopMusic();
        String musicFilePath = getClass().getResource("/Clips/solved.mp3").toString();
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

    public void newGame() {
        stopMusic();
        solvedStage.close();
        myViewController.playMusic();
        myViewController.newGame();
    }

    public void exitGame() {
        solvedStage.close();
        myViewController.exitGame();
    }

}
