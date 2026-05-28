# Conseil des Schtroumpfs

JavaFX 25 game project for the R2.02 IHM course.

The player manages the Smurfs council for 12 months. Each month applies production, one random event, up to two council actions, consumption, crisis detection, then victory or defeat checks.

## Technologies

- Java 25
- JavaFX 25
- FXML
- JavaFX CSS
- Maven
- Simple MVC structure

No database, network, REST API, Spring, Hibernate, or external game engine is used.

## Project Structure

```text
src/main/java/fr/uge/but/schtroumpf
├── controller
├── launcher
└── model

src/main/resources/fr/uge/but/schtroumpf
├── css
├── fxml
└── images
```

## Run

```bash
mvn javafx:run
```

Main class:

```text
fr.uge.but.schtroumpf.launcher.Main
```

## Game Rules

- The game lasts 12 months.
- All resources stay between 0 and 10.
- At the start of each month, resources are produced and one random event is applied.
- The player may use up to two council actions per month.
- At the end of each month, resources are consumed and crises are checked.
- If three crises are active at the same time, the village immediately loses.
- If the village survives month 12, the final score is the sum of all resources.

## Main Resources

- Berries
- Sarsaparilla
- Gold
- Tools
- Morale
- Defense
- Knowledge

## Main Screens

- Main menu
- Game view with resources, actions, current event, crises, and history
- End-game screen with final score and resources
