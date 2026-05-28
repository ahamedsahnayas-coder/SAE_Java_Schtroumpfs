package fr.uge.but.schtroumpf.model;

import java.util.ArrayList;
import java.util.List;

public class CrisisChecker {

    public List<Crisis> checkCrises(SmurfVillage village) {
        var crises = new ArrayList<Crisis>();

        if (village.getResource(Ressource.BAIES) == 0) {
            crises.add(new Crisis("Famine", "No berries remain. Morale falls."));
            village.changeResource(Ressource.MORAL, -1);
        }
        if (village.getResource(Ressource.MORAL) == 0) {
            crises.add(new Crisis("Revolt", "The village loses trust in the council. Gold is wasted."));
            village.changeResource(Ressource.OR, -1);
        }
        if (village.getResource(Ressource.SALSEPAREILLE) == 0) {
            crises.add(new Crisis("Epidemic", "Without sarsaparilla, illness spreads. Defense falls."));
            village.changeResource(Ressource.DEFENSE, -1);
        }
        if (village.getResource(Ressource.SAVOIR) == 0) {
            crises.add(new Crisis("Forgotten knowledge", "Recipes are forgotten. Tools become harder to maintain."));
            village.changeResource(Ressource.OUTILS, -1);
        }
        if (village.getResource(Ressource.DEFENSE) == 0) {
            crises.add(new Crisis("Massive attack", "The village is exposed. Berries and tools are lost."));
            village.changeResource(Ressource.BAIES, -2);
            village.changeResource(Ressource.OUTILS, -1);
        }

        return crises;
    }
}
