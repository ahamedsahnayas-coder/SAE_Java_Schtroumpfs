package fr.uge.but.schtroumpf.model;

public class Event {
    
    // Attributs de l'événement
    private final String name;
    private final String description;
    private final String targetResource; 
    private final int effect;            
    // Constructeur pour créer un événement
    public Event(String name, String description, String targetResource, int effect) {
        this.name = name;
        this.description = description;
        this.targetResource = targetResource;
        this.effect = effect;
    }

    // Getters pour l'affichage dans la Vue
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    // Applique les modifications sur le village selon la ressource ciblée
    public void applyEffect(SmurfVillage village) {
        if (this.targetResource.equals("berries")) {
            village.changeBerries(this.effect);
        } else if (this.targetResource.equals("sarsaparilla")) {
            village.changeSarsaparilla(this.effect);
        } else if (this.targetResource.equals("gold")) {
            village.changeGold(this.effect);
        } else if (this.targetResource.equals("tools")) {
            village.changeTools(this.effect);
        } else if (this.targetResource.equals("morale")) {
            village.changeMorale(this.effect);
        } else if (this.targetResource.equals("defense")) {
            village.changeDefense(this.effect);
        } else if (this.targetResource.equals("knowledge")) {
            village.changeKnowledge(this.effect);
        }
    }
}