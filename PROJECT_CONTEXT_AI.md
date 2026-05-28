# PROJECT OVERVIEW

- Name: `Conseil des Schtroumpfs`
- Type: pedagogical JavaFX 25 game
- Course context: R2.02 IHM, BUT2-level constraints
- Goal: manage a Smurf village council for 12 months
- Architecture: strict, simple MVC
- UI: FXML + JavaFX CSS
- Build: Maven
- Main class: `fr.uge.but.schtroumpf.launcher.Main`
- No forbidden tech: no Spring, Hibernate, database, REST, networking, multithreading, game engine, physics engine, ECS, complex persistence

# GAME LOOP

For each month:

1. Show current month and resources.
2. User clicks `Apply Production & Event`.
3. `CouncilGame.startMonth()` logic runs through `finishMonth()` when `monthStarted == false`.
4. `SmurfVillage.produceResources()` applies automatic production.
5. `EventGenerator.drawEvent()` selects one weighted random `Evenement`.
6. `Evenement.apply(SmurfVillage)` modifies resources.
7. Player can execute up to 2 council actions.
8. User clicks `Next Month` or `Finish Month`.
9. `SmurfVillage.consumeResources()` applies end-of-month consumption.
10. `CrisisChecker.checkCrises(village)` detects resource-zero crises.
11. If `activeCrises.size() >= 3`, immediate defeat.
12. If month 12 is completed, calculate final `GameResult`.
13. Otherwise increment month and return to resource review phase.

# MAIN SYSTEMS

## Resources

- Enum: `Ressource`
- Values:
  - `BAIES`
  - `SALSEPAREILLE`
  - `OR`
  - `OUTILS`
  - `MORAL`
  - `DEFENSE`
  - `SAVOIR`
- Stored in: `SmurfVillage.resources`
- Storage type: `EnumMap<Ressource, Integer>`
- Range: always clamped from `0` to `10`
- Clamp owner: `SmurfVillage`
- Initial values:
  - `BAIES = 6`
  - `SALSEPAREILLE = 5`
  - `OR = 4`
  - `OUTILS = 5`
  - `MORAL = 6`
  - `DEFENSE = 5`
  - `SAVOIR = 5`

## Production

- Method: `SmurfVillage.produceResources()`
- Effects:
  - `BAIES +2`
  - `SALSEPAREILLE +1`
  - `SAVOIR +1`

## Consumption

- Method: `SmurfVillage.consumeResources()`
- Effects:
  - `BAIES -2`
  - `SALSEPAREILLE -1`
  - `MORAL -1`

## Events

- Class: `Evenement`
- Generator: `EventGenerator`
- Selection: weighted random
- Applied at active month start
- Events:
  - `Gargamel attack`: `DEFENSE -3`, `MORAL -1`
  - `Berry discovery`: `BAIES +3`
  - `Storm`: `OUTILS -2`, `MORAL -1`
  - `Festival`: `MORAL +3`, `BAIES -1`
  - `Curse`: `SAVOIR -2`, `SALSEPAREILLE -1`
  - `Friendly village visit`: `OR +2`, `SALSEPAREILLE +1`

## Actions

- Class: `CouncilAction`
- Created by character classes using `ActionFactory`
- Each action has:
  - name
  - description
  - costs: `Map<Ressource, Integer>`
  - effects: `Map<Ressource, Integer>`
- Validation:
  - `CouncilAction.canExecute(village)`
  - `SmurfVillage.hasEnough(costs)`
- Execution:
  - subtract costs
  - apply effects
  - values are clamped by `SmurfVillage`
- Monthly limit:
  - `CouncilGame.MAX_ACTIONS_PER_MONTH = 2`

## Crises

- Crisis data: `record Crisis(String name, String description)`
- Checker: `CrisisChecker`
- Trigger condition: important resource reaches `0`
- Crisis mapping:
  - `BAIES == 0` -> `Famine`, then `MORAL -1`
  - `MORAL == 0` -> `Revolt`, then `OR -1`
  - `SALSEPAREILLE == 0` -> `Epidemic`, then `DEFENSE -1`
  - `SAVOIR == 0` -> `Forgotten knowledge`, then `OUTILS -1`
  - `DEFENSE == 0` -> `Massive attack`, then `BAIES -2`, `OUTILS -1`
- Defeat:
  - if `activeCrises.size() >= 3`

## Scoring

- Method: `SmurfVillage.getScore()`
- Formula: sum of all 7 resource values
- Maximum: `70`
- End result built by: `CouncilGame.buildVictoryResult()`
- Results:
  - score `>= 55`: `Great victory`
  - score `>= 40`: `Fragile victory`
  - score `< 40`: `Difficult survival`
  - 3 simultaneous crises: `Defeat`

# MVC MAP

## MODELS

`Ressource`:
- role: enum of all resources
- stores: display name
- important methods: `getDisplayName()`
- dependencies: none

`SmurfVillage`:
- role: resource state and resource modification rules
- stores: `EnumMap<Ressource, Integer> resources`
- important methods: `getResource`, `getResources`, `changeResource`, `hasEnough`, `applyChanges`, `produceResources`, `consumeResources`, `getScore`
- dependencies: `Ressource`

`CouncilAction`:
- role: executable council action with cost/effect validation
- stores: name, description, costs, effects
- important methods: `canExecute`, `execute`, `getCosts`, `getEffects`
- dependencies: `SmurfVillage`, `Ressource`

`ActionFactory`:
- role: small helper for creating `CouncilAction` and resource maps
- stores: nothing
- important methods: `action`, `changes`
- dependencies: `CouncilAction`, `Ressource`

`Personnage`:
- role: common interface for council characters
- stores: nothing
- important methods: `getName`, `getDescription`, `getActions`
- dependencies: `CouncilAction`

`GrandSchtroumpf`:
- role: wise leader character
- stores: immutable list of actions
- important methods: `getName`, `getDescription`, `getActions`
- dependencies: `Personnage`, `CouncilAction`, `ActionFactory`

`SchtroumpfBricoleur`:
- role: tools/repair/defense character
- stores: immutable list of actions
- important methods: `getName`, `getDescription`, `getActions`
- dependencies: `Personnage`, `CouncilAction`, `ActionFactory`

`SchtroumpfGourmand`:
- role: food-related character
- stores: immutable list of actions
- important methods: `getName`, `getDescription`, `getActions`
- dependencies: `Personnage`, `CouncilAction`, `ActionFactory`

`Schtroumpfette`:
- role: diplomacy/morale character
- stores: immutable list of actions
- important methods: `getName`, `getDescription`, `getActions`
- dependencies: `Personnage`, `CouncilAction`, `ActionFactory`

`Evenement`:
- role: random event with weighted probability and resource effects
- stores: name, description, weight, effects
- important methods: `getWeight`, `getEffects`, `apply`
- dependencies: `SmurfVillage`, `Ressource`

`EventGenerator`:
- role: event list and weighted random draw
- stores: `Random`, list of `Evenement`
- important methods: `drawEvent`
- dependencies: `Evenement`, `ActionFactory`, `Ressource`

`Crisis`:
- role: immutable crisis data
- stores: name, description
- important methods: generated record accessors
- dependencies: none

`CrisisChecker`:
- role: detects and applies crises
- stores: nothing
- important methods: `checkCrises`
- dependencies: `SmurfVillage`, `Crisis`, `Ressource`

`ActionResult`:
- role: result message for an attempted council action
- stores: `success`, `message`
- important methods: generated record accessors
- dependencies: none

`GameResult`:
- role: final game result
- stores: `victory`, `score`, `title`, `message`
- important methods: generated record accessors
- dependencies: none

`CouncilGame`:
- role: central game state and turn/month coordinator
- stores: village, event generator, crisis checker, characters, history, active crises, month, action count, current event, month phase, game over flag, final result
- important methods: `executeAction`, `finishMonth`, `getVillage`, `getCharacters`, `getCurrentMonth`, `getRemainingActions`, `getCurrentEvent`, `isMonthStarted`, `getHistory`, `getActiveCrises`, `isGameOver`, `getResult`
- dependencies: all main model systems

## CONTROLLERS

`ScreenNavigator`:
- linked fxml: all FXML files
- role: central screen navigation
- handles: loading FXML, applying CSS, setting scene, closing stage
- communicates with: `MainMenuController`, `GameController`, `EndGameController`, `CouncilGame`, `ViewFiles`

`MainMenuController`:
- linked fxml: `MainMenuView.fxml`
- role: main menu event handler
- handles: start game, quit
- communicates with: `ScreenNavigator`, creates `CouncilGame`

`GameController`:
- linked fxml: `GameView.fxml`
- role: main gameplay UI controller
- handles: next month button, restart button, action buttons
- communicates with: `CouncilGame`, `Personnage`, `CouncilAction`, `ActionResult`, `Crisis`, `Ressource`
- important UI updates:
  - `refreshHeader`
  - `refreshResources`
  - `refreshActions`
  - `refreshCrises`
  - `refreshHistory`

`EndGameController`:
- linked fxml: `EndGameView.fxml`
- role: final result screen controller
- handles: restart, quit
- communicates with: `CouncilGame`, `GameResult`, `Ressource`, `ScreenNavigator`

## VIEWS

`MainMenuView.fxml`:
- purpose: start screen
- important widgets: title label, explanation label, `Start Game` button, `Quit` button
- linked controller: `MainMenuController`

`GameView.fxml`:
- purpose: main gameplay screen
- important widgets:
  - `monthLabel`
  - `actionCountLabel`
  - `eventNameLabel`
  - `eventDescriptionLabel`
  - `actionMessageLabel`
  - `crisisLabel`
  - `nextMonthButton`
  - `resourceGrid`
  - `actionContainer`
  - `historyArea`
- linked controller: `GameController`

`EndGameView.fxml`:
- purpose: final result screen
- important widgets:
  - `resultTitleLabel`
  - `scoreLabel`
  - `messageLabel`
  - `resourceSummaryLabel`
  - `historyArea`
  - Restart/Quit buttons
- linked controller: `EndGameController`

`style.css`:
- purpose: external JavaFX CSS styling
- colors: blue, beige, forest green, soft red
- styles: panels, buttons, titles, event banner, resource bars, action blocks, history area

`ViewFiles`:
- role: constants for FXML and CSS paths
- constants:
  - `CSS`
  - `FXML_DIRECTORY`

# NAVIGATION FLOW

```text
Main.start(Stage)
  -> new ScreenNavigator(stage)
  -> ScreenNavigator.showMainMenu()
  -> MainMenuView.fxml + MainMenuController
  -> Start Game
  -> ScreenNavigator.showGame(new CouncilGame())
  -> GameView.fxml + GameController
  -> game completes
  -> ScreenNavigator.showEndGame(game)
  -> EndGameView.fxml + EndGameController
```

Restart flow:

```text
GameView Restart -> showGame(new CouncilGame())
EndGameView Restart -> showGame(new CouncilGame())
```

Quit flow:

```text
MainMenu Quit -> stage.close()
EndGameView Quit -> stage.close()
```

# RESOURCE FLOW

All resource modifications should go through `SmurfVillage`.

Main modification methods:

- `changeResource(resource, amount)`
- `applyChanges(changes)`
- `produceResources()`
- `consumeResources()`

Action flow:

```text
GameController button click
-> CouncilGame.executeAction(character, action)
-> CouncilAction.canExecute(village)
-> CouncilAction.execute(village)
-> SmurfVillage.changeResource(...)
-> values clamped
-> history updated
-> GameController.refreshView()
```

Event flow:

```text
CouncilGame.startMonth()
-> SmurfVillage.produceResources()
-> EventGenerator.drawEvent()
-> Evenement.apply(village)
-> SmurfVillage.applyChanges(...)
-> history updated
```

Crisis flow:

```text
CouncilGame.finishMonth() while monthStarted == true
-> SmurfVillage.consumeResources()
-> CrisisChecker.checkCrises(village)
-> activeCrises updated
-> if activeCrises >= 3: gameOver + defeat result
-> else if month 12: gameOver + victory result
-> else next month review phase
```

# EVENT FLOW

- Events are defined in `EventGenerator.events`.
- Each `Evenement` has a positive integer `weight`.
- `drawEvent()`:
  - sums all weights
  - draws integer from `0` to `totalWeight - 1`
  - subtracts each event weight until selected
- Event effects are maps of resource deltas.
- Applying event:
  - `Evenement.apply(village)`
  - calls `village.applyChanges(effects)`
  - clamp happens inside `SmurfVillage`

# FXML CONNECTIONS

FXML uses `fx:controller`:

- `MainMenuView.fxml` -> `fr.uge.but.schtroumpf.controller.MainMenuController`
- `GameView.fxml` -> `fr.uge.but.schtroumpf.controller.GameController`
- `EndGameView.fxml` -> `fr.uge.but.schtroumpf.controller.EndGameController`

Controller fields use `@FXML` and matching `fx:id`.

Event methods are connected with `onAction`:

- `#handleStartGame`
- `#handleQuit`
- `#handleNextMonth`
- `#handleRestart`

Dynamic action buttons are created in Java by `GameController.createActionRow(...)` because the number of character action rows is generated from model data.

# IMPORTANT RULES

- Resource values must remain between `0` and `10`.
- Model must not depend on JavaFX views.
- Controllers should not contain deep game rules.
- User interaction goes through JavaFX views/controllers.
- FXML is mandatory and used for all main screens.
- CSS is external: `style.css`.
- Up to 2 council actions per active month.
- Actions disabled if:
  - month has not started;
  - no action remains;
  - village lacks required resources.
- Immediate defeat if 3 or more crises are active together.
- Victory if month 12 is completed without immediate defeat.
- Final score is sum of all resources, max `70`.

# PROJECT STRUCTURE

```text
src/main/java/fr/uge/but/schtroumpf
├── controller
│   ├── EndGameController.java
│   ├── GameController.java
│   ├── MainMenuController.java
│   └── ScreenNavigator.java
├── launcher
│   └── Main.java
├── model
│   ├── ActionFactory.java
│   ├── ActionResult.java
│   ├── CouncilAction.java
│   ├── CouncilGame.java
│   ├── Crisis.java
│   ├── CrisisChecker.java
│   ├── Evenement.java
│   ├── EventGenerator.java
│   ├── GameResult.java
│   ├── GrandSchtroumpf.java
│   ├── Personnage.java
│   ├── Ressource.java
│   ├── SchtroumpfBricoleur.java
│   ├── SchtroumpfGourmand.java
│   ├── Schtroumpfette.java
│   └── SmurfVillage.java
└── view
    └── ViewFiles.java
```

```text
src/main/resources/fr/uge/but/schtroumpf/resources
├── css
│   └── style.css
├── fxml
│   ├── EndGameView.fxml
│   ├── GameView.fxml
│   └── MainMenuView.fxml
└── images
    └── smurf-house.svg
```

# KNOWN LIMITATIONS

- No save/load system.
- No database.
- No animations except possible future simple JavaFX animations.
- No character images displayed in current FXML.
- Resource and action panels are sections inside `GameView.fxml`, not separate reusable FXML components.
- Random events are hardcoded in `EventGenerator`.
- Character actions are hardcoded in character classes.
- No automated JUnit tests currently.
- Maven command requires Maven installed on local machine.
- Architecture intentionally avoids services/repositories/DI to stay pedagogical and BUT2-level.

# SAFE FUTURE CHANGES

- Add events in `EventGenerator`.
- Add actions in character classes.
- Add simple CSS improvements in `style.css`.
- Add a Help/Rules FXML screen via `ScreenNavigator`.
- Split resource/action panels into separate FXML files if the teacher asks for more view decomposition.
- Add simple JavaFX animations in controllers without changing model logic.

# DO NOT INTRODUCE

- Spring
- Hibernate
- REST controllers
- database layer
- repository/service architecture
- dependency injection framework
- networking
- advanced threading
- reactive streams
- external UI/game framework
- canvas game engine
- physics engine
- ECS architecture
