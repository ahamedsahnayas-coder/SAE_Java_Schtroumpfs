package fr.uge.but.schtroumpf.launcher;

import fr.uge.but.schtroumpf.controller.ScreenNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Conseil des Schtroumpfs");
        stage.setMinWidth(1000);
        stage.setMinHeight(680);
        new ScreenNavigator(stage).showMainMenu();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
