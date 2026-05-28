package fr.uge.but.schtroumpf.model;

import java.util.List;
import java.util.Random;

public class EventGenerator {
    private final Random random = new Random();
    private final List<Evenement> events = List.of(
            event("Gargamel attack",
                    "Gargamel almost found the village. The guards are exhausted.",
                    4,
                    ActionFactory.changes(Ressource.DEFENSE, -3, Ressource.MORAL, -1)),
            event("Berry discovery",
                    "A hidden bush gives the village fresh berries.",
                    5,
                    ActionFactory.changes(Ressource.BAIES, 3)),
            event("Storm",
                    "A storm damages workshops and scares everyone.",
                    4,
                    ActionFactory.changes(Ressource.OUTILS, -2, Ressource.MORAL, -1)),
            event("Festival",
                    "A cheerful festival brings the village together.",
                    3,
                    ActionFactory.changes(Ressource.MORAL, 3, Ressource.BAIES, -1)),
            event("Curse",
                    "A strange fog makes old recipes difficult to remember.",
                    3,
                    ActionFactory.changes(Ressource.SAVOIR, -2, Ressource.SALSEPAREILLE, -1)),
            event("Friendly village visit",
                    "A nearby village offers a small trade gift.",
                    4,
                    ActionFactory.changes(Ressource.OR, 2, Ressource.SALSEPAREILLE, 1))
    );

    public Evenement drawEvent() {
        int totalWeight = 0;
        for (Evenement event : events) {
            totalWeight += event.getWeight();
        }
        int draw = random.nextInt(totalWeight);
        for (Evenement event : events) {
            draw -= event.getWeight();
            if (draw < 0) {
                return event;
            }
        }
        return events.getLast();
    }

    private Evenement event(String name, String description, int weight, java.util.Map<Ressource, Integer> effects) {
        return new Evenement(name, description, weight, effects);
    }
}
