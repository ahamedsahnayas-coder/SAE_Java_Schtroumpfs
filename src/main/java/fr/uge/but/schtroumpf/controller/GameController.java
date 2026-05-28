package fr.uge.but.schtroumpf.controller;

import fr.uge.but.schtroumpf.model.ActionResult;
import fr.uge.but.schtroumpf.model.CouncilAction;
import fr.uge.but.schtroumpf.model.CouncilGame;
import fr.uge.but.schtroumpf.model.Crisis;
import fr.uge.but.schtroumpf.model.Personnage;
import fr.uge.but.schtroumpf.model.Ressource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class GameController {
    @FXML private Label monthLabel;
    @FXML private Label actionCountLabel;
    @FXML private Label eventNameLabel;
    @FXML private Label eventDescriptionLabel;
    @FXML private Label actionMessageLabel;
    @FXML private Label crisisLabel;
    @FXML private GridPane resourceGrid;
    @FXML private VBox actionContainer;
    @FXML private TextArea historyArea;

    private ScreenNavigator navigator;
    private CouncilGame game;

    public void setNavigator(ScreenNavigator navigator) {
        this.navigator = navigator;
    }

    public void setGame(CouncilGame game) {
        this.game = game;
        refreshView();
    }

    @FXML
    private void handleNextMonth(ActionEvent event) {
        game.finishMonth();
        if (game.isGameOver()) {
            navigator.showEndGame(game);
            return;
        }
        actionMessageLabel.setText("A new month begins. Production and event have been applied.");
        refreshView();
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        navigator.showGame(new CouncilGame());
    }

    private void refreshView() {
        refreshHeader();
        refreshResources();
        refreshActions();
        refreshCrises();
        refreshHistory();
    }

    private void refreshHeader() {
        monthLabel.setText("Month " + game.getCurrentMonth() + " / " + game.getMaxMonths());
        actionCountLabel.setText("Council actions left: " + game.getRemainingActions());
        eventNameLabel.setText(game.getCurrentEvent().getName());
        eventDescriptionLabel.setText(game.getCurrentEvent().getDescription());
    }

    private void refreshResources() {
        resourceGrid.getChildren().clear();
        resourceGrid.getColumnConstraints().clear();
        int row = 0;
        for (Ressource resource : Ressource.values()) {
            int value = game.getVillage().getResource(resource);
            Label name = new Label(resource.getDisplayName());
            Label number = new Label(value + " / 10");
            ProgressBar bar = new ProgressBar(value / 10.0);

            name.getStyleClass().add("resource-name");
            number.getStyleClass().add("resource-value");
            bar.getStyleClass().add("resource-bar");
            bar.setMaxWidth(Double.MAX_VALUE);

            resourceGrid.add(name, 0, row);
            resourceGrid.add(bar, 1, row);
            resourceGrid.add(number, 2, row);
            GridPane.setMargin(name, new Insets(4, 8, 4, 0));
            GridPane.setMargin(number, new Insets(4, 0, 4, 8));
            row++;
        }
    }

    private void refreshActions() {
        actionContainer.getChildren().clear();
        for (Personnage character : game.getCharacters()) {
            Label title = new Label(character.getName());
            Label description = new Label(character.getDescription());
            VBox block = new VBox(6);

            title.getStyleClass().add("character-title");
            description.getStyleClass().add("character-description");
            block.getStyleClass().add("action-block");
            block.getChildren().addAll(title, description);

            for (CouncilAction action : character.getActions()) {
                block.getChildren().add(createActionRow(character, action));
            }
            actionContainer.getChildren().add(block);
        }
    }

    private HBox createActionRow(Personnage character, CouncilAction action) {
        Button button = new Button(action.getName());
        Label details = new Label(action.getDescription() + " " + formatActionResources(action));
        HBox row = new HBox(8, button, details);

        button.getStyleClass().add("action-button");
        details.getStyleClass().add("action-details");
        details.setWrapText(true);
        row.getStyleClass().add("action-row");

        button.setDisable(game.getRemainingActions() == 0 || !action.canExecute(game.getVillage()));
        button.setOnAction(event -> {
            ActionResult result = game.executeAction(character, action);
            actionMessageLabel.setText(result.message());
            refreshView();
        });

        return row;
    }

    private String formatActionResources(CouncilAction action) {
        String costText = formatMap("Cost", action.getCosts(), -1);
        String effectText = formatMap("Effect", action.getEffects(), 1);
        if (costText.isBlank()) {
            return effectText;
        }
        return costText + " | " + effectText;
    }

    private String formatMap(String label, Map<Ressource, Integer> changes, int sign) {
        if (changes.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(label).append(": ");
        int count = 0;
        for (var entry : changes.entrySet()) {
            if (count > 0) {
                builder.append(", ");
            }
            int value = entry.getValue() * sign;
            if (value > 0) {
                builder.append("+");
            }
            builder.append(value).append(" ").append(entry.getKey().getDisplayName());
            count++;
        }
        return builder.toString();
    }

    private void refreshCrises() {
        if (game.getActiveCrises().isEmpty()) {
            crisisLabel.setText("No active crisis.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (Crisis crisis : game.getActiveCrises()) {
            if (!builder.isEmpty()) {
                builder.append("  ");
            }
            builder.append(crisis.name()).append(": ").append(crisis.description());
        }
        crisisLabel.setText(builder.toString());
    }

    private void refreshHistory() {
        historyArea.setText(String.join("\n", game.getHistory()));
        historyArea.positionCaret(historyArea.getText().length());
    }
}
