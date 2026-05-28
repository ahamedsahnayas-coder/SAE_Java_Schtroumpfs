package fr.uge.but.schtroumpf.model;

import java.util.List;

public class Schtroumpfette implements Personnage {
    private final List<CouncilAction> actions = List.of(
            ActionFactory.action(
                    "Negotiate with neighbors",
                    "Ask friendly villages for help.",
                    ActionFactory.changes(Ressource.MORAL, 1),
                    ActionFactory.changes(Ressource.OR, 2, Ressource.SALSEPAREILLE, 1)
            ),
            ActionFactory.action(
                    "Calm conflict",
                    "Restore peace after village arguments.",
                    ActionFactory.changes(Ressource.SAVOIR, 1),
                    ActionFactory.changes(Ressource.MORAL, 3)
            ),
            ActionFactory.action(
                    "Organize party",
                    "Spend berries to raise morale and knowledge sharing.",
                    ActionFactory.changes(Ressource.BAIES, 2),
                    ActionFactory.changes(Ressource.MORAL, 2, Ressource.SAVOIR, 1)
            )
    );

    @Override
    public String getName() {
        return "Schtroumpfette";
    }

    @Override
    public String getDescription() {
        return "Diplomat and mediator.";
    }

    @Override
    public List<CouncilAction> getActions() {
        return actions;
    }
}
