package View;

import Server.Configurations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesController {
    Alerts alerts = new Alerts();
    Configurations config = Configurations.getInstance();
    @FXML
    private ChoiceBox<String> genAlgo;
    @FXML
    private ChoiceBox<String> solveAlgo;
    @FXML
    private Slider musicVolumeSlider;
    // Reference to your game's MediaPlayer
    private MediaPlayer mediaPlayer;

    // Set the reference to the MediaPlayer instance
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @FXML
    private void initialize() {
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 100.0);
        });
    }

    public void Update() throws IOException {
        String genAlgorithm = genAlgo.getValue();
        String solveAlgorithm = solveAlgo.getValue();

        if (genAlgorithm == null || solveAlgorithm == null){
            alerts.showError("You must choose both options to update the settings.");
            return;
        }

        Properties properties = new Properties();
        properties.setProperty("mazeGeneratingAlgorithm", genAlgorithm);
        properties.setProperty("mazeSearchingAlgorithm", solveAlgorithm);
        properties.setProperty("threadPoolSize", "4");

        FileWriter writer = new FileWriter("resources/config.properties");
        properties.store(writer, "Game Configurations");
        alerts.showAlert("Settings have been changed.");
        writer.close();

        config.loadConfigurations();

    }
}
