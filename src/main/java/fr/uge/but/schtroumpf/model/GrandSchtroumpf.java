package fr.uge.but.schtroumpf.model;

import java.util.List;
import java.util.Map;

public class GrandSchtroumpf implements Personnage {
    private final List<CouncilAction> actions = List.of(
            ActionFactory.action(
                    "Consult spellbook",
                    "Use old recipes to improve knowledge and morale.",
                    ActionFactory.changes(Ressource.SALSEPAREILLE, 1),
                    ActionFactory.changes(Ressource.SAVOIR, 2, Ressource.MORAL, 1)
            ),
            ActionFactory.action(
                    "Organize council meeting",
                    "Spend time debating and strengthen the village defense.",
                    Map.of(),
                    ActionFactory.changes(Ressource.DEFENSE, 1, Ressource.MORAL, 1)
            ),
            ActionFactory.action(
                    "Negotiate with animals",
                    "Trade berries for forest protection.",
                    ActionFactory.changes(Ressource.BAIES, 2),
                    ActionFactory.changes(Ressource.DEFENSE, 2, Ressource.SALSEPAREILLE, 1)
            )
    );

    @Override
    public String getName() {
        return "Grand Schtroumpf";
    }

    @Override
    public String getDescription() {
        return "Wise leader of the council.";
    }

    @Override
    public List<CouncilAction> getActions() {
        return actions;
    }
}
