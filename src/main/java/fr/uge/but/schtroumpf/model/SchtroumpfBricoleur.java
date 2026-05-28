package fr.uge.but.schtroumpf.model;

import java.util.List;
import java.util.Map;

public class SchtroumpfBricoleur implements Personnage {
    private final List<CouncilAction> actions = List.of(
            ActionFactory.action(
                    "Repair houses",
                    "Use tools to make the village safer.",
                    ActionFactory.changes(Ressource.OUTILS, 1),
                    ActionFactory.changes(Ressource.MORAL, 1, Ressource.DEFENSE, 1)
            ),
            ActionFactory.action(
                    "Build traps",
                    "Prepare forest traps against Gargamel.",
                    ActionFactory.changes(Ressource.OUTILS, 2),
                    ActionFactory.changes(Ressource.DEFENSE, 3)
            ),
            ActionFactory.action(
                    "Invent gadget",
                    "Transform knowledge and gold into useful tools.",
                    ActionFactory.changes(Ressource.OR, 1, Ressource.SAVOIR, 1),
                    ActionFactory.changes(Ressource.OUTILS, 3)
            )
    );

    @Override
    public String getName() {
        return "Schtroumpf Bricoleur";
    }

    @Override
    public String getDescription() {
        return "Inventor and repair expert.";
    }

    @Override
    public List<CouncilAction> getActions() {
        return actions;
    }
}
