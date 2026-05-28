package fr.uge.but.schtroumpf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CouncilGame {
    private static final int MAX_MONTHS = 12;
    private static final int MAX_ACTIONS_PER_MONTH = 2;

    private final SmurfVillage village = new SmurfVillage();
    private final EventGenerator eventGenerator = new EventGenerator();
    private final CrisisChecker crisisChecker = new CrisisChecker();
    private final List<Personnage> characters = List.of(
            new GrandSchtroumpf(),
            new SchtroumpfBricoleur(),
            new SchtroumpfGourmand(),
            new Schtroumpfette()
    );
    private final List<String> history = new ArrayList<>();
    private final List<Crisis> activeCrises = new ArrayList<>();

    private int currentMonth = 1;
    private int actionsUsedThisMonth;
    private Evenement currentEvent;
    private boolean monthStarted;
    private boolean gameOver;
    private GameResult result;

    public CouncilGame() {
        history.add("Month 1 - Review the village resources before starting the month.");
    }

    public SmurfVillage getVillage() {
        return village;
    }

    public List<Personnage> getCharacters() {
        return characters;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getMaxMonths() {
        return MAX_MONTHS;
    }

    public int getRemainingActions() {
        return MAX_ACTIONS_PER_MONTH - actionsUsedThisMonth;
    }

    public Evenement getCurrentEvent() {
        return currentEvent;
    }

    public boolean isMonthStarted() {
        return monthStarted;
    }

    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public List<Crisis> getActiveCrises() {
        return Collections.unmodifiableList(activeCrises);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameResult getResult() {
        return result;
    }

    public ActionResult executeAction(Personnage character, CouncilAction action) {
        if (gameOver) {
            return new ActionResult(false, "The game is already finished.");
        }
        if (!monthStarted) {
            return new ActionResult(false, "Start the month before choosing actions.");
        }
        if (actionsUsedThisMonth >= MAX_ACTIONS_PER_MONTH) {
            return new ActionResult(false, "No council actions remain this month.");
        }
        if (!action.canExecute(village)) {
            return new ActionResult(false, "Not enough resources for: " + action.getName());
        }
        action.execute(village);
        actionsUsedThisMonth++;
        String message = character.getName() + " used " + action.getName() + ".";
        history.add("Month " + currentMonth + " - " + message);
        return new ActionResult(true, message);
    }

    public void finishMonth() {
        if (gameOver) {
            return;
        }
        if (!monthStarted) {
            startMonth();
            return;
        }

        village.consumeResources();
        history.add("Month " + currentMonth + " - End-of-month consumption applied.");

        activeCrises.clear();
        activeCrises.addAll(crisisChecker.checkCrises(village));
        if (activeCrises.isEmpty()) {
            history.add("Month " + currentMonth + " - No crisis.");
        } else {
            history.add("Month " + currentMonth + " - " + activeCrises.size() + " crisis/crises active.");
        }

        if (activeCrises.size() >= 3) {
            gameOver = true;
            result = new GameResult(false, village.getScore(), "Defeat",
                    "Three crises happened at the same time. The council has fallen.");
            return;
        }

        if (currentMonth >= MAX_MONTHS) {
            gameOver = true;
            result = buildVictoryResult();
            return;
        }

        currentMonth++;
        monthStarted = false;
        currentEvent = null;
        history.add("Month " + currentMonth + " - Review the village resources before starting the month.");
    }

    private void startMonth() {
        monthStarted = true;
        actionsUsedThisMonth = 0;
        activeCrises.clear();
        village.produceResources();
        currentEvent = eventGenerator.drawEvent();
        currentEvent.apply(village);
        history.add("Month " + currentMonth + " - Production applied.");
        history.add("Month " + currentMonth + " - Event: " + currentEvent.getName() + ".");
    }

    private GameResult buildVictoryResult() {
        int score = village.getScore();
        if (score >= 55) {
            return new GameResult(true, score, "Great victory",
                    "The village is strong, happy, and ready for another year.");
        }
        if (score >= 40) {
            return new GameResult(true, score, "Fragile victory",
                    "The village survived, but the next council must stay careful.");
        }
        return new GameResult(true, score, "Difficult survival",
                "The village reached the end of winter with very little comfort.");
    }
}
