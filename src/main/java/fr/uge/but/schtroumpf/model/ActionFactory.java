package fr.uge.but.schtroumpf.model;

import java.util.EnumMap;
import java.util.Map;

final class ActionFactory {
    private ActionFactory() {
    }

    static CouncilAction action(String name, String description,
                                Map<Ressource, Integer> costs,
                                Map<Ressource, Integer> effects) {
        return new CouncilAction(name, description, costs, effects);
    }

    static Map<Ressource, Integer> changes(Object... values) {
        var changes = new EnumMap<Ressource, Integer>(Ressource.class);
        for (int index = 0; index < values.length; index += 2) {
            changes.put((Ressource) values[index], (Integer) values[index + 1]);
        }
        return changes;
    }
}
