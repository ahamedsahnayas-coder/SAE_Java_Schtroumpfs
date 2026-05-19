package fr.uge.but.schtroumpf;

import fr.uge.but.schtroumpf.controller.ControleurJeu;
import fr.uge.but.schtroumpf.model.Jeu;
import fr.uge.but.schtroumpf.model.SchtroumpfGourmand;
import fr.uge.but.schtroumpf.model.Schtroumpfette;
import fr.uge.but.schtroumpf.model.SmurfVillage;
import fr.uge.but.schtroumpf.view.AffichageJeu;
import fr.uge.but.schtroumpf.view.LecteurSaisie;

/* Point d'entrée du jeu "Conseil des Schtroumpfs".
 * Crée les objets Modèle, Vue et Contrôleur,
 * puis démarre la boucle principale.
 */
public class Main {
    public static void main(String[] args) {
        // Modèle
        SmurfVillage village = new SmurfVillage();
        SchtroumpfGourmand gourmand = new SchtroumpfGourmand();
        Schtroumpfette schtroumpfette = new Schtroumpfette();
        Jeu jeu = new Jeu(village, gourmand, schtroumpfette);

        // Vue
        AffichageJeu vue = new AffichageJeu();
        LecteurSaisie lecteur = new LecteurSaisie();

        // Contrôleur
        ControleurJeu controleur = new ControleurJeu(jeu, vue, lecteur, gourmand, schtroumpfette);

        // Lancement
        controleur.lancerJeu();
    }
}
