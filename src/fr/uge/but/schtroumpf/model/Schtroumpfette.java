package fr.uge.but.schtroumpf.model;

import java.util.Random;

/* La Schtroumpfette : diplomate du village.
 * Elle peut négocier avec les villages voisins, apaiser les conflits ou organiser une fête. */
public class Schtroumpfette implements Personnage {

    private static final Random random = new Random();

    private String name;
    private int charisma;  // charisme (0-10)
    private int morale;    // moral personnel (0-10)

    // Suivi de l'effet bonus de fête (+Moral pendant 2 tours)
    private int feteBonus; // nombre de tours restants avec le bonus de fête

    public Schtroumpfette() {
        this.name = "Schtroumpfette";
        this.charisma = 9;
        this.morale = 8;
        this.feteBonus = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    /* Action 1 : Négocier avec les villages voisins (+Or ou +Salsepareille)
     * Le résultat dépend du charisma. */
    public boolean negocierVillagesVoisins(SmurfVillage village) {
        if (charisma <= 0) {
            return false;
        }
        // Tirage aléatoire : Or ou Salsepareille
        if (random.nextBoolean()) {
            village.modifyResource("gold", 2);
        } else {
            village.modifyResource("smurfberry", 2);
        }
        charisma = Math.max(0, charisma - 1);
        return true;
    }

    /* Action 2 : Apaiser un conflit interne (+Moral)
     * Toujours disponible. */
    public boolean apaiservConflitInterne(SmurfVillage village) {
        village.modifyResource("morale", 2);
        morale = Math.min(10, morale + 1);
        return true;
    }

    /* Action 3 : Organiser une fête (-Baies, +Moral pour 2 tours)
     * Nécessite au moins 2 Baies. */
    public boolean organiserFete(SmurfVillage village) {
        if (village.getResource("berries") < 2) {
            return false; // pas assez de baies
        }
        village.modifyResource("berries", -2);
        village.modifyResource("morale", 2);
        feteBonus = 2; // le bonus durera 2 tours
        return true;
    }

    /* À appeler en début de chaque tour pour appliquer le bonus de fête.
     * Retourne true si le bonus est encore actif. */
    public boolean applyFeteBonus(SmurfVillage village) {
        if (feteBonus > 0) {
            village.modifyResource("morale", 1);
            feteBonus--;
            return true;
        }
        return false;
    }

    public boolean hasFeteBonus() {
        return feteBonus > 0;
    }

    public int getFeteBonus() { return feteBonus; }
    public int getCharisma() { return charisma; }
    public int getMorale() { return morale; }

    @Override
    public String toString() {
        return name + " [Charisme: " + charisma + ", Moral: " + morale + "]";
    }
}
