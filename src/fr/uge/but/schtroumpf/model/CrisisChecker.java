package fr.uge.but.schtroumpf.model;

import java.util.ArrayList;
import java.util.List;

/* CrisisChecker : vérifie les crises à la fin de chaque tour.
 * Une crise est déclenchée quand une ressource tombe à 0
 * Si 3 crises ou plus se déclenchent en même temps : défaite immédiate. */
public class CrisisChecker {

    /* Représente une crise déclenchée lors d'un tour. */
    public record Crisis(String name, String description) {}

    /* Vérifie les crises sur le village et applique leurs effets.
     Retourne la liste des crises déclenchées ce tour. */
    public static List<Crisis> checkCrises(SmurfVillage village) {
        List<Crisis> crises = new ArrayList<>();

        // Famine : Baies à 0
        if (village.getResource("berries") == 0) {
            crises.add(new Crisis("Famine",
                "Les Schtroumpfs manquent de nourriture ! -1 Moral ce tour."));
            village.modifyResource("morale", -1);
        }

        // Épidémie : Salsepareille à 0
        if (village.getResource("smurfberry") == 0) {
            crises.add(new Crisis("Épidémie",
                "Sans Salsepareille, les maladies se répandent ! -1 Défense."));
            village.modifyResource("defense", -1);
        }

        // Révolte : Moral à 0
        if (village.getResource("morale") == 0) {
            crises.add(new Crisis("Révolte des Schtroumpfs",
                "Les Schtroumpfs se révoltent ! -1 Or (pillage)."));
            village.modifyResource("gold", -1);
        }

        // Attaque massive : Défense à 0
        if (village.getResource("defense") == 0) {
            crises.add(new Crisis("Attaque massive",
                "Le village est sans défense ! -2 Baies et -1 Outils."));
            village.modifyResource("berries", -2);
            village.modifyResource("tools", -1);
        }

        // Oubli des recettes : Savoir à 0
        if (village.getResource("knowledge") == 0) {
            crises.add(new Crisis("Oubli des recettes",
                "Les Schtroumpfs ont oublié leurs recettes ! -1 Salsepareille."));
            village.modifyResource("smurfberry", -1);
        }

        return crises;
    }

    /* Retourne true si la condition de défaite immédiate est atteinte
     * (3 crises simultanées ou plus). */
    public static boolean isImmediateDefeat(List<Crisis> crises) {
        return crises.size() >= 3;
    }
}
