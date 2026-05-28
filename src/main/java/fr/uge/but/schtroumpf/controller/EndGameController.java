package fr.uge.but.schtroumpf.controller;

import fr.uge.but.schtroumpf.model.CouncilGame;
import fr.uge.but.schtroumpf.model.GameResult;
import fr.uge.but.schtroumpf.model.Ressource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class EndGameController {
    @FXML private Label resultTitleLabel;
    @FXML private Label scoreLabel;
    @FXML private Label messageLabel;
    @FXML private Label resourceSummaryLabel;
    @FXML private TextArea historyArea;

    private ScreenNavigator navigator;

    public void setNavigator(ScreenNavigator navigator) {
        this.navigator = navigator;
    }

    public void setGame(CouncilGame game) {
        GameResult result = game.getResult();
        resultTitleLabel.setText(result.title());
        scoreLabel.setText("Final score: " + result.score() + " / 70");
        messageLabel.setText(result.message());
        resourceSummaryLabel.setText(buildResourceSummary(game));
        historyArea.setText(String.join("\n", game.getHistory()));
        historyArea.positionCaret(historyArea.getText().length());
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        navigator.showGame(new CouncilGame());
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        navigator.quit();
    }

    private String buildResourceSummary(CouncilGame game) {
        StringBuilder builder = new StringBuilder();
        for (Ressource resource : Ressource.values()) {
            if (!builder.isEmpty()) {
                builder.append("   ");
            }
            builder.append(resource.getDisplayName())
                    .append(": ")
                    .append(game.getVillage().getResource(resource));
        }
        return builder.toString();
    }
}
