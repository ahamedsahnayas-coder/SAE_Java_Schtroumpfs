package fr.uge.but.schtroumpf.model;

import java.util.List;
import java.util.Map;

public class SchtroumpfGourmand implements Personnage {
    private final List<CouncilAction> actions = List.of(
            ActionFactory.action(
                    "Gather berries",
                    "Collect food near the village.",
                    Map.of(),
                    ActionFactory.changes(Ressource.BAIES, 3)
            ),
            ActionFactory.action(
                    "Organize feast",
                    "Share a large meal and improve morale.",
                    ActionFactory.changes(Ressource.BAIES, 3),
                    ActionFactory.changes(Ressource.MORAL, 3)
            ),
            ActionFactory.action(
                    "Find rare mushroom",
                    "Bring back a rare ingredient for the stock.",
                    Map.of(),
                    ActionFactory.changes(Ressource.BAIES, 1, Ressource.SALSEPAREILLE, 2)
            )
    );

    @Override
    public String getName() {
        return "Schtroumpf Gourmand";
    }

    @Override
    public String getDescription() {
        return "Food specialist of the village.";
    }

    @Override
    public List<CouncilAction> getActions() {
        return actions;
    }
}
