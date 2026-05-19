package fr.uge.but.schtroumpf.view;

import java.util.Scanner;

/* LecteurSaisie : lit les entrées de l'utilisateur depuis la console.
 *
 * IMPORTANT : Tous les System.out liés à la saisie sont ici.
 * La logique du jeu reste dans le Modèle et le Contrôleur. */
public class LecteurSaisie {

    private final Scanner scanner;

    public LecteurSaisie() {
        this.scanner = new Scanner(System.in);
    }

    /* Demande à l'utilisateur de choisir une option numérique.
     * Répète la demande tant que la saisie est invalide.
     *
     * @param prompt  le message à afficher
     * @param min     valeur minimale acceptée
     * @param max     valeur maximale acceptée
     * @return le choix valide de l'utilisateur */
    public int lireChoix(String prompt, int min, int max) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next(); // vider la saisie invalide
            System.out.print("Choix invalide. " + prompt);
        }
        int choice = scanner.nextInt();
        while (choice < min || choice > max) {
            System.out.print("Entrez un nombre entre " + min + " et " + max + " : ");
            while (!scanner.hasNextInt()) {
                scanner.next();
            }
            choice = scanner.nextInt();
        }
        return choice;
    }

    /* Attend que l'utilisateur appuie sur Entrée pour continuer. */
    public void attendreEntree() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        // consume leftover newline if any
        if (scanner.hasNextLine()) scanner.nextLine();
        scanner.nextLine();
    }

    /* Ferme le scanner (à appeler en fin de partie) */
    public void fermer() {
        scanner.close();
    }
}
