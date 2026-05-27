package fr.uge.but.schtroumpf.model;

public class SmurfVillage {
    
    // Attributs privés pour stocker la valeur de chaque ressource
    private int berries;
    private int sarsaparilla;
    private int gold;
    private int tools;
    private int morale;
    private int defense;
    private int knowledge;

    // Constructeur : toutes les ressources commencent à 5 au début du jeu
    public SmurfVillage() {
        this.berries = 5;
        this.sarsaparilla = 5;
        this.gold = 5;
        this.tools = 5;
        this.morale = 5;
        this.defense = 5;
        this.knowledge = 5;
    }

    // Méthode de sécurité pour bloquer les valeurs entre 0 et 10
    private int applyBounds(int value) {
        if (value > 10) {
            return 10;
        }
        if (value < 0) {
            return 0;
        }
        return value;
    }

    // Méthodes pour modifier la valeur des ressources (+ ou -)
    public void changeBerries(int amount) {
        this.berries = applyBounds(this.berries + amount);
    }

    public void changeSarsaparilla(int amount) {
        this.sarsaparilla = applyBounds(this.sarsaparilla + amount);
    }

    public void changeGold(int amount) {
        this.gold = applyBounds(this.gold + amount);
    }

    public void changeTools(int amount) {
        this.tools = applyBounds(this.tools + amount);
    }

    public void changeMorale(int amount) {
        this.morale = applyBounds(this.morale + amount);
    }

    public void changeDefense(int amount) {
        this.defense = applyBounds(this.defense + amount);
    }

    public void changeKnowledge(int amount) {
        this.knowledge = applyBounds(this.knowledge + amount);
    }

    // Getters pour permettre au reste du programme de lire les scores
    public int getBerries() { return this.berries; }
    public int getSarsaparilla() { return this.sarsaparilla; }
    public int getGold() { return this.gold; }
    public int getTools() { return this.tools; }
    public int getMorale() { return this.morale; }
    public int getDefense() { return this.defense; }
    public int getKnowledge() { return this.knowledge; }

    // Phase 1 : Production automatique en début de tour
    public void produceResources() {
        changeBerries(2);
        changeKnowledge(1);
    }

    // Phase 4 : Consommation automatique en fin de tour
    public void consumeResources() {
        changeBerries(-2);
        changeMorale(-1);
    }

    // Phase 5 : Vérifie si le village perd (une ressource tombe à 0)
    public boolean checkCrisis() {
        return this.berries == 0 || this.sarsaparilla == 0 || this.morale == 0 || this.defense == 0 || this.knowledge == 0;
    }
}