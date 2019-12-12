package ee.gr.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientWindow extends Application {

    private static Pane myPane;
    private static Stage myStage;

    public void start(Stage primaryStage) throws Exception {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        myStage = primaryStage;
        myPane = FXMLLoader.load(getClass().getResource("controllers/Login.fxml"));
        myStage.setTitle("SpaceChat");
        myStage.setScene(new Scene(myPane));
        myStage.setResizable(false);
        myStage.show();
    }

    public static Pane getMyPane() {
        return myPane;
    }

    public static void setMyPane(Pane myPane) {
        ClientWindow.myPane = myPane;
    }

    public static Stage getMyStage() {
        return myStage;
    }

    public static void setMyStage(Stage myStage) {
        ClientWindow.myStage = myStage;
    }

}
