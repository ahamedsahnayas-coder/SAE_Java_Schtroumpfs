package fr.uge.but.schtroumpf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventGenerator {
    
    private final List<Event> availableEvents;
    private final List<Event> history;
    private final Random random;

    public EventGenerator() {
        this.availableEvents = new ArrayList<>();
        this.history = new ArrayList<>();
        this.random = new Random();

        // LES 6 ÉVÉNEMENTS OBLIGATOIRES
        
        // Attaque de Gargamel : Défense et Moral baissent
        availableEvents.add(new Event("Attaque de Gargamel", "Gargamel rôde près du village ! La défense prend un coup.", "defense", -3));
        
        // Découverte de baies magiques : (+Baies, +Salsepareille) 
        availableEvents.add(new Event("Découverte de baies magiques", "Un buisson miraculeux remplit nos stocks de nourriture.", "berries", 3));
        
        // Visite d'un village ami : (+Or, +Moral)
        availableEvents.add(new Event("Visite d'un village ami", "Des marchands viennent échanger des richesses.", "gold", 2));
        
        // Tempête de Salsepareille : (-Outils, +Savoir) 
        availableEvents.add(new Event("Tempête de Salsepareille", "Le vent abîme les outils restés dehors.", "tools", -2));
        
        // Fête des Schtroumpfs : (+Moral, -Baies) 
        availableEvents.add(new Event("Fête des Schtroumpfs", "Une grande danse remonte le moral de tout le monde !", "morale", 3));
        
        // Malédiction de la forêt : (-Savoir)
        availableEvents.add(new Event("Malédiction de la forêt", "Un brouillard mystique fait perdre la mémoire au village.", "knowledge", -2));

        
        // ÉVÉNEMENTS SUPPLÉMENTAIRES 
        
        
        // Événements pour la Nourriture (Berries)
        availableEvents.add(new Event("Invasion de chenilles", "Des insectes mangent une partie des réserves de baies.", "berries", -2));
        availableEvents.add(new Event("Super récolte", "Le Schtroumpf Jardinier ramène des paniers pleins.", "berries", 2));

        // Événements pour la Salsepareille
        availableEvents.add(new Event("Clairière secrète", "Une zone pleine de salsepareille fraîche est découverte.", "sarsaparilla", 3));
        availableEvents.add(new Event("Sécheresse", "La salsepareille manque d'eau et flétrit.", "sarsaparilla", -2));

        // Événement pour l'Or
        availableEvents.add(new Event("Filon de la mine", "Le Schtroumpf Mineur trouve une pépite d'or.", "gold", 2));

        // Événement pour les Outils
        availableEvents.add(new Event("Nouvelle forge", "Le Schtroumpf Bricoleur améliore l'atelier.", "tools", 2));

        // Événement pour le Moral
        availableEvents.add(new Event("Dispute au village", "Le Schtroumpf Grognon s'est disputé avec tout le monde.", "morale", -2));

        // Événement pour la Défense
        availableEvents.add(new Event("Palissade renforcée", "Les Schtroumpfs consolident les pièges autour du village.", "defense", 2));

        // Événement pour le Savoir
        availableEvents.add(new Event("Grimoire ancien", "Le Grand Schtroumpf décode une page magique oubliée.", "knowledge", 2));
    }

    // Pioche un événement au hasard, applique l'effet au village et le met dans l'historique
    public Event triggerRandomEvent(SmurfVillage village) {
        int randomIndex = random.nextInt(availableEvents.size());
        Event chosenEvent = availableEvents.get(randomIndex);

        // Modifie les ressources du village
        chosenEvent.applyEffect(village);

        // Enregistre dans l'historique 
        history.add(chosenEvent);

        return chosenEvent;
    }

    // Permet de récupérer l'historique (pour le rapport ou l'affichage de fin de partie)
    public List<Event> getHistory() {
        return this.history;
    }
}