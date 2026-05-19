package fr.uge.but.schtroumpf.model;

/* ScoreCalculator : calcule le score final et détermine la victoire ou la défaite.
 *
 * Le score est calculé à partir des ressources restantes après 12 tours.
 * Condition de victoire : avoir passé les 12 tours sans défaite immédiate,
 * et avoir au moins 3 dans chaque ressource.
 */
public class ScoreCalculator {

    /* Calcule le score final (de 0 à 100) à partir des ressources du village.
     * Chaque ressource vaut proportionnellement sur 100. */
    public static int calculateScore(SmurfVillage village) {
        int total = village.getBerries()
                  + village.getSmurfberry()
                  + village.getGold()
                  + village.getTools()
                  + village.getMorale()
                  + village.getDefense()
                  + village.getKnowledge();
        // Max possible : 7 ressources × 10 = 70
        // On ramène sur 100
        return (int) Math.round((total / 70.0) * 100);
    }

    /* Vérifie si les conditions de victoire sont remplies (toutes les ressources >= 3). */
    public static boolean isVictory(SmurfVillage village) {
        return village.getBerries()    >= 3
            && village.getSmurfberry() >= 3
            && village.getGold()       >= 3
            && village.getTools()      >= 3
            && village.getMorale()     >= 3
            && village.getDefense()    >= 3
            && village.getKnowledge()  >= 3;
    }

    /* Retourne un commentaire associé au score. */
    public static String getScoreComment(int score) {
        if (score >= 85) return "Légendaire ! Les Schtroumpfs prospèrent !";
        if (score >= 70) return "Excellent ! Le village est en pleine forme.";
        if (score >= 55) return "Bien joué ! Le village s'en sort honorablement.";
        if (score >= 40) return "Moyen... Le village a survécu, mais de justesse.";
        return "Difficile. Le village est à bout de souffle.";
    }
}
