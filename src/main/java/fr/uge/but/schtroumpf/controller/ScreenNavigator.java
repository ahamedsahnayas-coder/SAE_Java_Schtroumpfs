package fr.uge.but.schtroumpf.controller;

import fr.uge.but.schtroumpf.model.CouncilGame;
import fr.uge.but.schtroumpf.view.ViewFiles;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenNavigator {
    private final Stage stage;

    public ScreenNavigator(Stage stage) {
        this.stage = stage;
    }

    public void showMainMenu() {
        try {
            FXMLLoader loader = load("MainMenuView.fxml");
            Parent root = loader.load();
            MainMenuController controller = loader.getController();
            controller.setNavigator(this);
            setScene(root);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the main menu.", exception);
        }
    }

    public void showGame(CouncilGame game) {
        try {
            FXMLLoader loader = load("GameView.fxml");
            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setNavigator(this);
            controller.setGame(game);
            setScene(root);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the game view.", exception);
        }
    }

    public void showEndGame(CouncilGame game) {
        try {
            FXMLLoader loader = load("EndGameView.fxml");
            Parent root = loader.load();
            EndGameController controller = loader.getController();
            controller.setNavigator(this);
            controller.setGame(game);
            setScene(root);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the end game view.", exception);
        }
    }

    public void quit() {
        stage.close();
    }

    private FXMLLoader load(String fileName) {
        return new FXMLLoader(getClass().getResource(ViewFiles.FXML_DIRECTORY + fileName));
    }

    private void setScene(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(ViewFiles.CSS).toExternalForm());
        stage.setScene(scene);
    }
}
