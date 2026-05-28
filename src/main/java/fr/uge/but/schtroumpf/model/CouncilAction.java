package fr.uge.but.schtroumpf.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class CouncilAction {
    private final String name;
    private final String description;
    private final EnumMap<Ressource, Integer> costs;
    private final EnumMap<Ressource, Integer> effects;

    public CouncilAction(String name, String description,
                         Map<Ressource, Integer> costs,
                         Map<Ressource, Integer> effects) {
        this.name = name;
        this.description = description;
        this.costs = new EnumMap<>(Ressource.class);
        this.effects = new EnumMap<>(Ressource.class);
        this.costs.putAll(costs);
        this.effects.putAll(effects);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<Ressource, Integer> getCosts() {
        return Collections.unmodifiableMap(costs);
    }

    public Map<Ressource, Integer> getEffects() {
        return Collections.unmodifiableMap(effects);
    }

    public boolean canExecute(SmurfVillage village) {
        return village.hasEnough(costs);
    }

    public void execute(SmurfVillage village) {
        for (var cost : costs.entrySet()) {
            village.changeResource(cost.getKey(), -cost.getValue());
        }
        village.applyChanges(effects);
    }
}
