package ee.gr.GUI.controllers;

import ee.gr.GUI.ClientWindow;
import ee.gr.Launch;
import ee.gr.Planet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class channelsController implements Initializable {

    @FXML
    private Label labelUniverse;
    @FXML
    private Label labelEarth;
    @FXML
    private Label labelJupiter;
    @FXML
    private Label labelMars;
    @FXML
    private Label labelMercury;
    @FXML
    private Label labelNeptune;
    @FXML
    private Label labelSaturn;
    @FXML
    private Label labelVenus;
    @FXML
    private Label labelUranus;
    @FXML
    private Label labelOther;
    private Launch launch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    launch = Launch.getInstance();
    }

    @FXML
    private void changeToUniverse() throws IOException {
        launch.setCurrentChannel(Planet.UNIVERSE);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToEarth() throws IOException {
        launch.setCurrentChannel(Planet.EARTH);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToJupiter() throws IOException {
        launch.setCurrentChannel(Planet.JUPITER);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToMars() throws IOException {
        launch.setCurrentChannel(Planet.MARS);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToMercury() throws IOException {
        launch.setCurrentChannel(Planet.MERCURY);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToNeptune() throws IOException {
        launch.setCurrentChannel(Planet.NEPTUNE);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToSaturn() throws IOException {
        launch.setCurrentChannel(Planet.SATURN);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToVenus() throws IOException {
        launch.setCurrentChannel(Planet.VENUS);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToUranus() throws IOException {
        launch.setCurrentChannel(Planet.URANUS);
        changeScene("GUI/controllers/Chat.fxml");
    }

    @FXML
    private void changeToOther() {

    }

    private void changeScene(String s) throws IOException {
        File ne = new File(Launch.PROPERTY.concat(Launch.PATH + s));
        Pane myPane = FXMLLoader.load(ne.toURL());
        ClientWindow.getMyStage().setScene(new Scene(myPane));
    }
}
