package fr.uge.but.schtroumpf.model;

import java.util.Random;

/* Le Schtroumpf Gourmand : spécialiste de la nourriture.
 * Il peut cueillir des baies, organiser des festins ou trouver des champignons rares. */
public class SchtroumpfGourmand implements Personnage {

    private static final Random random = new Random();

    private String name;
    private int energy;   // énergie (0-10)
    private int morale;   // moral personnel (0-10)

    public SchtroumpfGourmand() {
        this.name = "Schtroumpf Gourmand";
        this.energy = 7;
        this.morale = 8;
    }

    @Override
    public String getName() {
        return name;
    }

    /* Action 1 : Cueillir des baies (+Baies)
     * Toujours disponible si énergie > 0. */
    public boolean cueillirBaies(SmurfVillage village) {
        if (energy <= 0) {
            return false; // pas assez d'énergie
        }
        village.modifyResource("berries", 2);
        energy = Math.max(0, energy - 1);
        return true;
    }

    /* Action 2 : Organiser un festin (-Baies, +Moral du village)
     * Nécessite au moins 2 Baies dans le village. */
    public boolean organiserFestin(SmurfVillage village) {
        if (village.getResource("berries") < 2) {
            return false; // pas assez de baies
        }
        village.modifyResource("berries", -2);
        village.modifyResource("morale", 3);
        morale = Math.min(10, morale + 1);
        return true;
    }

    /* Action 3 : Trouver un champignon rare (bonus aléatoire)
     * 50% de chance de succès. En cas de succès, bonus aléatoire sur une ressource. */
    public boolean trouverChampignonRare(SmurfVillage village) {
        if (energy <= 0) {
            return false;
        }
        energy = Math.max(0, energy - 2);
        boolean success = random.nextBoolean();
        if (success) {
            // Bonus aléatoire : Baies, Or, ou Savoir
            int choice = random.nextInt(3);
            switch (choice) {
                case 0 -> village.modifyResource("berries", 2);
                case 1 -> village.modifyResource("gold", 1);
                case 2 -> village.modifyResource("knowledge", 1);
            }
        }
        return success;
    }

    /* Régénère l'énergie en début de tour. */
    public void regenerateEnergy() {
        energy = Math.min(10, energy + 3);
    }

    public int getEnergy() { return energy; }
    public int getMorale() { return morale; }

    @Override
    public String toString() {
        return name + " [Énergie: " + energy + ", Moral: " + morale + "]";
    }
}
