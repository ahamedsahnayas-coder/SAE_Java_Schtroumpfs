package fr.uge.but.schtroumpf.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class SmurfVillage {
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 10;

    private final EnumMap<Ressource, Integer> resources = new EnumMap<>(Ressource.class);

    public SmurfVillage() {
        resources.put(Ressource.BAIES, 6);
        resources.put(Ressource.SALSEPAREILLE, 5);
        resources.put(Ressource.OR, 4);
        resources.put(Ressource.OUTILS, 5);
        resources.put(Ressource.MORAL, 6);
        resources.put(Ressource.DEFENSE, 5);
        resources.put(Ressource.SAVOIR, 5);
    }

    public int getResource(Ressource resource) {
        return resources.get(resource);
    }

    public Map<Ressource, Integer> getResources() {
        return Collections.unmodifiableMap(resources);
    }

    public void changeResource(Ressource resource, int amount) {
        setResource(resource, getResource(resource) + amount);
    }

    public boolean hasEnough(Map<Ressource, Integer> costs) {
        for (var entry : costs.entrySet()) {
            if (getResource(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void applyChanges(Map<Ressource, Integer> changes) {
        for (var entry : changes.entrySet()) {
            changeResource(entry.getKey(), entry.getValue());
        }
    }

    public void produceResources() {
        changeResource(Ressource.BAIES, 2);
        changeResource(Ressource.SALSEPAREILLE, 1);
        changeResource(Ressource.SAVOIR, 1);
    }

    public void consumeResources() {
        changeResource(Ressource.BAIES, -2);
        changeResource(Ressource.SALSEPAREILLE, -1);
        changeResource(Ressource.MORAL, -1);
    }

    public int getScore() {
        int score = 0;
        for (int value : resources.values()) {
            score += value;
        }
        return score;
    }

    private void setResource(Ressource resource, int value) {
        resources.put(resource, clamp(value));
    }

    private int clamp(int value) {
        return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
    }
}
