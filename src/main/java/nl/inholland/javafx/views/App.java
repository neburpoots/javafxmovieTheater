package nl.inholland.javafx.views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nl.inholland.javafx.views.windows.MainWindow;
import nl.inholland.javafx.views.windows.login.LoginWindow;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        System.setProperty("prism.lcdtext", "false");

        LoginWindow lw = new LoginWindow();
        lw.getStage().show();
    }
}
