package fr.uge.but.schtroumpf.view;

import fr.uge.but.schtroumpf.model.CrisisChecker.Crisis;
import fr.uge.but.schtroumpf.model.Jeu.GameResult;
import fr.uge.but.schtroumpf.model.SmurfVillage;

import java.util.List;

/* AffichageJeu : affiche toutes les informations du jeu dans la console.
 *
 * IMPORTANT : Tous les System.out.println sont dans cette classe (et LecteurSaisie).
 * Aucune autre classe du modèle ou du contrôleur ne doit afficher quoi que ce soit. */
public class AffichageJeu {

    /* Affiche l'écran de bienvenue. */
    public void afficherBienvenue() {
        System.out.println("==============================================");
        System.out.println("   CONSEIL DES SCHTROUMPFS - Le jeu console  ");
        System.out.println("==============================================");
        System.out.println("Votre mission : survivre 12 mois et prospérer !");
        System.out.println();
    }

    /* Affiche le début d'un tour. */
    public void afficherDebutTour(int tour, int maxTours) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("   MOIS " + tour + " / " + maxTours);
        System.out.println("==========================================");
    }

    /* Affiche les ressources actuelles du village. */
    public void afficherRessources(SmurfVillage village) {
        System.out.println();
        System.out.println("--- Ressources du village ---");
        System.out.println("  Baies        : " + barRessource(village.getBerries()));
        System.out.println("  Salsepareille: " + barRessource(village.getSmurfberry()));
        System.out.println("  Or           : " + barRessource(village.getGold()));
        System.out.println("  Outils       : " + barRessource(village.getTools()));
        System.out.println("  Moral        : " + barRessource(village.getMorale()));
        System.out.println("  Défense      : " + barRessource(village.getDefense()));
        System.out.println("  Savoir       : " + barRessource(village.getKnowledge()));
        System.out.println();
    }

    /* Crée une petite barre visuelle pour une ressource (valeur /10) */
    private String barRessource(int value) {
        String bar = "█".repeat(value) + "░".repeat(10 - value);
        return bar + " " + value + "/10";
    }

    /* Affiche les crises déclenchées. */
    public void afficherCrises(List<Crisis> crises) {
        if (crises.isEmpty()) {
            System.out.println("✅ Aucune crise ce tour. Bien joué !");
        } else {
            System.out.println("⚠️  CRISES DÉCLENCHÉES (" + crises.size() + ") :");
            for (Crisis c : crises) {
                System.out.println("  [" + c.name() + "] " + c.description());
            }
        }
    }

    /* Affiche un avertissement de défaite immédiate */
    public void afficherDefaiteImmediate() {
        System.out.println();
        System.out.println("💀 3 CRISES SIMULTANÉES ! DÉFAITE IMMÉDIATE !");
        System.out.println("   Le village des Schtroumpfs est anéanti...");
    }

    /* Affiche l'écran de fin de partie. */
    public void afficherFinPartie(GameResult result) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println("           FIN DE PARTIE");
        System.out.println("==========================================");
        if (result.victory()) {
            System.out.println("VICTOIRE ! Les Schtroumpfs ont prospéré !");
        } else {
            System.out.println("DÉFAITE... Le village n'a pas survécu.");
        }
        System.out.println("Score final : " + result.score() + "/100");
        System.out.println(result.comment());
        System.out.println("==========================================");
    }

    /* Affiche un message générique (événement, action, info) */
    public void afficherMessage(String message) {
        System.out.println(message);
    }

    /* Affiche le menu des actions du Schtroumpf Gourmand */
    public void afficherMenuGourmand(int energy) {
        System.out.println();
        System.out.println("--- Schtroumpf Gourmand (Énergie: " + energy + ") ---");
        System.out.println("  1. Cueillir des baies (+2 Baies)");
        System.out.println("  2. Organiser un festin (-2 Baies, +3 Moral)");
        System.out.println("  3. Trouver un champignon rare (bonus aléatoire)");
        System.out.println("  0. Passer (ne rien faire)");
    }

    /* Affiche le menu des actions de Schtroumpfette */
    public void afficherMenuSchtroumpfette(int charisma, boolean feteActive) {
        System.out.println();
        System.out.println("--- Schtroumpfette (Charisme: " + charisma + ") ---");
        if (feteActive) {
            System.out.println("  ℹ️  Bonus de fête encore actif !");
        }
        System.out.println("  1. Négocier avec les villages voisins (+2 Or ou +2 Salsepareille)");
        System.out.println("  2. Apaiser un conflit interne (+2 Moral)");
        System.out.println("  3. Organiser une fête (-2 Baies, +2 Moral + bonus 2 tours)");
        System.out.println("  0. Passer (ne rien faire)");
    }
}
