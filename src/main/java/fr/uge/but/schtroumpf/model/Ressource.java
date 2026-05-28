package fr.uge.but.schtroumpf.model;

public enum Ressource {
    BAIES("Berries"),
    SALSEPAREILLE("Sarsaparilla"),
    OR("Gold"),
    OUTILS("Tools"),
    MORAL("Morale"),
    DEFENSE("Defense"),
    SAVOIR("Knowledge");

    private final String displayName;

    Ressource(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
