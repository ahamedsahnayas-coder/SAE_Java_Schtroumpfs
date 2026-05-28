package fr.uge.but.schtroumpf.model;

import java.util.List;

public interface Personnage {
    String getName();

    String getDescription();

    List<CouncilAction> getActions();
}
