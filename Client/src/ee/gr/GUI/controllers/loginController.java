package ee.gr.GUI.controllers;

import ee.gr.GUI.ClientWindow;
import ee.gr.Launch;
import ee.gr.Planet;
import ee.gr.sendingdata.DataPackage;
import ee.gr.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    ObservableList<Planet> planetsList;
    @FXML
    private Button buttonLogIn;
    @FXML
    private ComboBox comboBoxPlanets;
    @FXML
    private Button buttonRegister;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private TextField fieldLogin;
    @FXML
    private TextField fieldIp;
    @FXML
    private Label labelMsgText;
    private Socket socket;
    private Launch launchInstance;
    private ObjectInputStream inPut;
    private ObjectOutputStream outPut;

    public void initialize(URL location, ResourceBundle resources) {
        launchInstance = Launch.getInstance();
        setPlanetsList();
        comboBoxPlanets.setItems(planetsList);
    }

    private void setPlanetsList() {
        planetsList = FXCollections.observableArrayList(
                Arrays.asList(Planet.EARTH, Planet.VENUS, Planet.MARS,
                        Planet.JUPITER, Planet.MERCURY, Planet.NEPTUNE, Planet.SATURN, Planet.URANUS));
    }

    @FXML
    private void login() throws IOException, ClassNotFoundException {
        if (checkEntries()) return;
        try {
            socket = new Socket(fieldIp.getText().equals("") ? "10.0.0.37" : fieldIp.getText(), 5000);
            launchInstance.setSocket(socket);
        } catch (IOException e) {
            labelMsgText.setText("Server doesn't respond");
            return;
        }
        if (socket.isConnected()) {
            launchInstance.setSocket(socket);
            try {
                outPut = new ObjectOutputStream(socket.getOutputStream());
                launchInstance.setOutputStream(outPut);
                inPut = new ObjectInputStream(socket.getInputStream());
                launchInstance.setInputStream(inPut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendSerializedUser(fieldLogin.getText(), fieldPassword.getText(), (Planet) comboBoxPlanets.getValue());
            initByDataPackageReceived();
            try {
                changeSceneToLoggedInCustomerPanel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            launchInstance.startListener();
        } else {
            labelMsgText.setText("Server doesn't respond");
            return;
        }
    }

    private void initByDataPackageReceived() throws IOException, ClassNotFoundException {
        DataPackage dataPackage = ((DataPackage) inPut.readObject());
        launchInstance.setChannelList(dataPackage.getChannelUpdate().getChannelList());
        launchInstance.setCurrentChannel(dataPackage.getChannelUpdate().getChannelList().get(0));
        launchInstance.setActiveUser(dataPackage.getUser());
    }

    private boolean checkEntries() {
        if (fieldLogin.getText().equals("")) {
            labelMsgText.setText("Enter Login!");
            return true;
        }
        if (fieldPassword.getText().equals("")) {
            labelMsgText.setText("Enter password!");
            return true;
        }
        if (comboBoxPlanets.getValue() == null) {
            labelMsgText.setText("Choose your planet!");
            return true;
        }
        return false;
    }

    private void sendSerializedUser(String name, String password, Planet planet) throws IOException {
        User user = new User(true, name, password, planet);
        outPut.writeObject(user);
        outPut.flush();
    }

    @FXML
    private void changeSceneToLoggedInCustomerPanel() throws IOException {
        changeScene("GUI/controllers/Chat.fxml");
    }

    private void changeScene(String s) throws IOException {
        File ne = new File(Launch.PROPERTY.concat(Launch.PATH + s));
        Pane myPane = FXMLLoader.load(ne.toURL());
        ClientWindow.getMyStage().setScene(new Scene(myPane));
    }

    @FXML
    private void register() {

    }

}
