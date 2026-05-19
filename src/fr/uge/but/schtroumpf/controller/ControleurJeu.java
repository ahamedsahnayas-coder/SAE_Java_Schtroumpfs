package fr.uge.but.schtroumpf.controller;

import fr.uge.but.schtroumpf.model.CrisisChecker.Crisis;
import fr.uge.but.schtroumpf.model.Jeu;
import fr.uge.but.schtroumpf.model.Jeu.GameResult;
import fr.uge.but.schtroumpf.model.SchtroumpfGourmand;
import fr.uge.but.schtroumpf.model.Schtroumpfette;
import fr.uge.but.schtroumpf.view.AffichageJeu;
import fr.uge.but.schtroumpf.view.LecteurSaisie;

import java.util.List;

/**
 * ControleurJeu : fait le lien entre la Vue et le Modèle.
 *
 * Il orchestre le déroulement du jeu :
 * - Récupère les actions de l'utilisateur via LecteurSaisie
 * - Appelle les méthodes du Modèle (Jeu, personnages)
 * - Demande à AffichageJeu d'afficher les résultats
 *
 * IMPORTANT : Aucun System.out dans cette classe.
 *
 * NOTE : Ce contrôleur gère uniquement les étapes dont la Personne C est responsable
 * (vérification des crises, actions de Gourmand et Schtroumpfette, fin de partie).
 * Les étapes Production, Événements, et Actions des autres personnages sont à
 * intégrer par les Personnes A et B dans leurs méthodes respectives.
 */
public class ControleurJeu {

    private final Jeu jeu;
    private final AffichageJeu vue;
    private final LecteurSaisie lecteur;

    // Accès directs aux personnages de la Personne C
    private final SchtroumpfGourmand gourmand;
    private final Schtroumpfette schtroumpfette;

    public ControleurJeu(Jeu jeu, AffichageJeu vue, LecteurSaisie lecteur,
                         SchtroumpfGourmand gourmand, Schtroumpfette schtroumpfette) {
        this.jeu = jeu;
        this.vue = vue;
        this.lecteur = lecteur;
        this.gourmand = gourmand;
        this.schtroumpfette = schtroumpfette;
    }

    /**
     * Lance et gère la boucle principale du jeu (12 tours).
     */
    public void lancerJeu() {
        vue.afficherBienvenue();

        while (!jeu.isOver()) {

            // --- Démarrage du tour ---
            boolean ok = jeu.startTurn();
            if (!ok) break;

            vue.afficherDebutTour(jeu.getCurrentTurn(), jeu.getMaxTurns());
            vue.afficherRessources(jeu.getVillage());

            // --- Étape 1 : Production des ressources (Personne A) ---
            // village.produireRessources(); // à intégrer par Personne A

            // --- Étape 2 : Événement aléatoire (Personne A) ---
            // Evenement evt = evenementManager.tirerEvenement();
            // vue.afficherMessage("Événement : " + evt.getDescription());
            // evt.appliquerEffet(village);

            // --- Étape 3 : Actions du Conseil ---
            // Actions des autres personnages (Personne B)
            // grandSchtroumpf.choisirAction(village);
            // schtroumpfBricoleur.choisirAction(village);

            // Actions de la Personne C
            gererActionGourmand();
            gererActionSchtroumpfette();

            // --- Étape 4 : Consommation des ressources (Personne A) ---
            // village.consommerRessources();

            vue.afficherRessources(jeu.getVillage());

            // --- Étape 5 : Vérification des crises (Personne C) ---
            List<Crisis> crises = jeu.checkCrises();
            vue.afficherCrises(crises);

            if (jeu.isImmediateDefeat()) {
                vue.afficherDefaiteImmediate();
                break;
            }

            if (jeu.isGameOver()) break;

            lecteur.attendreEntree();
        }

        // --- Fin de partie ---
        GameResult result = jeu.getResult();
        vue.afficherFinPartie(result);
        lecteur.fermer();
    }

    /**
     * Gère le tour du Schtroumpf Gourmand (interaction utilisateur).
     */
    private void gererActionGourmand() {
        vue.afficherMenuGourmand(gourmand.getEnergy());
        int choix = lecteur.lireChoix("Votre choix : ", 0, 3);

        switch (choix) {
            case 1 -> {
                boolean ok = gourmand.cueillirBaies(jeu.getVillage());
                if (ok) vue.afficherMessage("✅ Schtroumpf Gourmand a cueilli des baies ! (+2 Baies)");
                else    vue.afficherMessage("❌ Pas assez d'énergie pour cueillir !");
            }
            case 2 -> {
                boolean ok = gourmand.organiserFestin(jeu.getVillage());
                if (ok) vue.afficherMessage("✅ Le festin booste le moral du village ! (-2 Baies, +3 Moral)");
                else    vue.afficherMessage("❌ Pas assez de baies pour organiser un festin !");
            }
            case 3 -> {
                boolean success = gourmand.trouverChampignonRare(jeu.getVillage());
                if (success) vue.afficherMessage("✅ Champignon rare trouvé ! Bonus aléatoire obtenu !");
                else         vue.afficherMessage("❌ Aucun champignon rare trouvé ce tour...");
            }
            default -> vue.afficherMessage("Le Schtroumpf Gourmand se repose ce tour.");
        }
    }

    /**
     * Gère le tour de Schtroumpfette (interaction utilisateur).
     */
    private void gererActionSchtroumpfette() {
        vue.afficherMenuSchtroumpfette(schtroumpfette.getCharisma(), schtroumpfette.hasFeteBonus());
        int choix = lecteur.lireChoix("Votre choix : ", 0, 3);

        switch (choix) {
            case 1 -> {
                boolean ok = schtroumpfette.negocierVillagesVoisins(jeu.getVillage());
                if (ok) vue.afficherMessage("✅ Schtroumpfette a négocié ! (+2 Or ou +2 Salsepareille)");
                else    vue.afficherMessage("❌ Le charisme de Schtroumpfette est épuisé !");
            }
            case 2 -> {
                schtroumpfette.apaiservConflitInterne(jeu.getVillage());
                vue.afficherMessage("✅ Conflit apaisé ! (+2 Moral)");
            }
            case 3 -> {
                boolean ok = schtroumpfette.organiserFete(jeu.getVillage());
                if (ok) vue.afficherMessage("✅ Fête organisée ! (-2 Baies, +2 Moral pendant 2 tours)");
                else    vue.afficherMessage("❌ Pas assez de baies pour organiser la fête !");
            }
            default -> vue.afficherMessage("Schtroumpfette se repose ce tour.");
        }
    }
}
