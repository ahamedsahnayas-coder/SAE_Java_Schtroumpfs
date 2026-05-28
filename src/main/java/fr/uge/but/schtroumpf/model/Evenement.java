package fr.uge.but.schtroumpf.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Evenement {
    private final String name;
    private final String description;
    private final int weight;
    private final EnumMap<Ressource, Integer> effects;

    public Evenement(String name, String description, int weight, Map<Ressource, Integer> effects) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.effects = new EnumMap<>(Ressource.class);
        this.effects.putAll(effects);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public Map<Ressource, Integer> getEffects() {
        return Collections.unmodifiableMap(effects);
    }

    public void apply(SmurfVillage village) {
        village.applyChanges(effects);
    }
}
