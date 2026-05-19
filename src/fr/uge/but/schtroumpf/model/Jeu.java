package fr.uge.but.schtroumpf.model;

import java.util.List;

/* Jeu : contient la logique de la boucle principale des 12 tours.
 *
 * Cette classe appartient au Modèle. Elle ne contient aucun affichage (pas de System.out).
 * C'est le Contrôleur qui appelle ses méthodes, et la Vue qui affiche.
 *
 * Déroulement d'un tour :
 * 1. Production des ressources       (Personne A)
 * 2. Événement aléatoire             (Personne A)
 * 3. Actions du Conseil              (Personne B)
 * 4. Consommation des ressources     (Personne A)
 * 5. Vérification des crises         (Personne C - CrisisChecker)
 */
public class Jeu {

    private static final int MAX_TURNS = 12;

    private final SmurfVillage village;
    private final SchtroumpfGourmand gourmand;
    private final Schtroumpfette schtroumpfette;

    private int currentTurn;
    private boolean gameOver;
    private boolean immediateDefeat;

    // Résultat de la dernière vérification de crises
    private List<CrisisChecker.Crisis> lastCrises;

    public Jeu(SmurfVillage village, SchtroumpfGourmand gourmand, Schtroumpfette schtroumpfette) {
        this.village = village;
        this.gourmand = gourmand;
        this.schtroumpfette = schtroumpfette;
        this.currentTurn = 0;
        this.gameOver = false;
        this.immediateDefeat = false;
        this.lastCrises = List.of();
    }

    /* Démarre un nouveau tour. Incrémente le compteur et régénère l'énergie des personnages.
     * @return false si le jeu est déjà terminé */
    public boolean startTurn() {
        if (gameOver) return false;
        currentTurn++;
        gourmand.regenerateEnergy();
        // Appliquer le bonus de fête de Schtroumpfette si actif
        schtroumpfette.applyFeteBonus(village);
        return true;
    }

    /* Vérifie les crises et met à jour l'état du jeu.
     * Doit être appelée après la consommation des ressources.
     * @return la liste des crises déclenchées */
    public List<CrisisChecker.Crisis> checkCrises() {
        lastCrises = CrisisChecker.checkCrises(village);
        if (CrisisChecker.isImmediateDefeat(lastCrises)) {
            gameOver = true;
            immediateDefeat = true;
        }
        return lastCrises;
    }

    /* Vérifie si le jeu est terminé (12 tours passés ou défaite immédiate).
     * Doit être appelé après checkCrises().
     * @return true si la partie est finie */
    public boolean isGameOver() {
        if (currentTurn >= MAX_TURNS && !immediateDefeat) {
            gameOver = true;
        }
        return gameOver;
    }

    /* Calcule le score final et retourne le résultat de la partie.
     * À appeler uniquement quand isGameOver() == true. */
    public GameResult getResult() {
        if (immediateDefeat) {
            return new GameResult(false, 0, "Défaite immédiate : 3 crises simultanées !");
        }
        int score = ScoreCalculator.calculateScore(village);
        boolean victory = ScoreCalculator.isVictory(village);
        String comment = victory
            ? ScoreCalculator.getScoreComment(score)
            : "Le village n'a pas atteint les seuils minimaux de survie.";
        return new GameResult(victory, score, comment);
    }

    // Getters pour le Contrôleur
    public int getCurrentTurn()  { return currentTurn; }
    public int getMaxTurns()     { return MAX_TURNS; }
    public boolean isOver()      { return gameOver; }
    public boolean isImmediateDefeat() { return immediateDefeat; }
    public List<CrisisChecker.Crisis> getLastCrises() { return lastCrises; }
    public SmurfVillage getVillage() { return village; }

    /* Résultat de fin de partie. */
    public record GameResult(boolean victory, int score, String comment) {}
}
