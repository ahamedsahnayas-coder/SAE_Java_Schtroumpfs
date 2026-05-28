package fr.uge.but.schtroumpf.controller;

import fr.uge.but.schtroumpf.model.CouncilGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {
    private ScreenNavigator navigator;

    public void setNavigator(ScreenNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        navigator.showGame(new CouncilGame());
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        navigator.quit();
    }
}
