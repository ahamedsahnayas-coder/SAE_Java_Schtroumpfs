# Conseil des Schtroumpfs — documentation du projet

## Introduction

**Conseil des Schtroumpfs** est une application JavaFX réalisée dans un objectif pédagogique pour le module R2.02 IHM. Le projet met en pratique une interface graphique JavaFX construite avec FXML, stylisée avec JavaFX CSS, et organisée selon une architecture MVC simple.

Le joueur dirige le conseil des Schtroumpfs pendant 12 mois. Chaque mois, le village produit des ressources, subit un événement aléatoire, choisit des actions de conseil, consomme des ressources, puis vérifie les crises. Si le village survit jusqu'à la fin du mois 12, la partie est gagnée. Si 3 crises ou plus sont actives en même temps, la partie est immédiatement perdue.

Technologies utilisées :

- Java 25 ;
- JavaFX 25 ;
- FXML ;
- JavaFX CSS ;
- Maven ;
- architecture MVC simple ;
- collections Java standard ;
- gestion classique des événements JavaFX.

Le projet reste volontairement simple, lisible et adapté à un niveau BUT2. Il n'utilise pas Spring, Hibernate, base de données, API REST, réseau, architecture complexe, moteur de jeu ou système de sauvegarde avancé.

## Game Rules

### Ressources

Le village possède 7 ressources, définies dans l'énumération `Ressource` :

- `BAIES` : nourriture principale du village ;
- `SALSEPAREILLE` : ressource médicale et magique ;
- `OR` : richesse utilisée pour certains échanges ;
- `OUTILS` : matériel nécessaire aux réparations et à la défense ;
- `MORAL` : état moral du village ;
- `DEFENSE` : protection contre les dangers ;
- `SAVOIR` : connaissances, recettes et sagesse collective.

Chaque ressource est toujours comprise entre `0` et `10`. Les modifications passent par `SmurfVillage`, qui applique automatiquement les bornes minimales et maximales.

Valeurs initiales :

```text
Berries       = 6
Sarsaparilla  = 5
Gold          = 4
Tools         = 5
Morale        = 6
Defense       = 5
Knowledge     = 5
```

### Déroulement d'un mois

La partie dure 12 mois. Dans l'interface, chaque mois est présenté en deux temps pour respecter l'ordre demandé :

1. afficher le mois courant et les ressources ;
2. lancer la production et l'événement du mois ;
3. appliquer la production automatique ;
4. tirer et appliquer un événement aléatoire ;
5. laisser le joueur choisir jusqu'à deux actions de conseil ;
6. terminer le mois ;
7. appliquer la consommation de fin de mois ;
8. vérifier les crises ;
9. vérifier la défaite ou la victoire ;
10. passer au mois suivant si la partie continue.

Cette organisation permet au joueur de voir l'état du village avant les changements du mois.

### Production

Au début de la phase active du mois, `produceResources()` ajoute :

- `+2` baies ;
- `+1` salsepareille ;
- `+1` savoir.

Cette étape est automatique et ne demande pas d'action du joueur.

### Événements

Après la production, `EventGenerator` tire un événement aléatoire. Les événements sont représentés par la classe `Evenement`.

Événements principaux :

- `Gargamel attack` : diminue la défense et le moral ;
- `Berry discovery` : ajoute des baies ;
- `Storm` : abîme les outils et diminue le moral ;
- `Festival` : augmente le moral mais consomme des baies ;
- `Curse` : diminue le savoir et la salsepareille ;
- `Friendly village visit` : apporte de l'or et de la salsepareille.

Chaque événement possède :

- un nom ;
- une description ;
- un poids de probabilité ;
- une liste d'effets sur les ressources ;
- une méthode `apply(SmurfVillage village)`.

Les événements sont ajoutés à l'historique de la partie.

### Actions du conseil

Après l'événement, le joueur peut choisir jusqu'à deux actions de conseil par mois. Une action est représentée par `CouncilAction`.

Chaque action :

- possède un nom ;
- possède une description ;
- peut avoir un coût ;
- modifie une ou plusieurs ressources ;
- vérifie si elle est possible avant d'être appliquée.

Si le village n'a pas assez de ressources, le bouton correspondant est désactivé dans l'interface. Cette validation rend le comportement clair et évite les actions impossibles.

### Personnages

Chaque personnage implémente l'interface `Personnage`.

Un personnage possède :

- un nom ;
- une description ;
- une liste d'actions.

Personnages disponibles :

- `GrandSchtroumpf` ;
- `SchtroumpfBricoleur` ;
- `SchtroumpfGourmand` ;
- `Schtroumpfette`.

### Consommation

En fin de mois, `consumeResources()` retire :

- `-2` baies ;
- `-1` salsepareille ;
- `-1` moral.

Cette étape représente les besoins réguliers du village.

### Crises

Les crises sont vérifiées par `CrisisChecker`. Une crise se déclenche lorsqu'une ressource importante atteint `0`.

Exemples :

- `Famine` si les baies valent 0 ;
- `Revolt` si le moral vaut 0 ;
- `Epidemic` si la salsepareille vaut 0 ;
- `Forgotten knowledge` si le savoir vaut 0 ;
- `Massive attack` si la défense vaut 0.

Certaines crises ont aussi des conséquences supplémentaires. Par exemple, la famine diminue le moral et une attaque massive fait perdre des baies et des outils.

Si 3 crises ou plus sont actives en même temps, le village perd immédiatement.

### Victoire et défaite

Défaite :

- si 3 crises ou plus sont actives simultanément.

Victoire :

- si le village survit jusqu'à la fin du mois 12.

Score final :

```text
score = somme de toutes les ressources
```

Résultats possibles :

- `Great victory` ;
- `Fragile victory` ;
- `Difficult survival` ;
- `Defeat`.

## MVC Architecture

Le projet respecte une architecture MVC.

MVC signifie :

- **Model** : données et logique métier ;
- **View** : interface graphique ;
- **Controller** : lien entre l'utilisateur, la vue et le modèle.

### Pourquoi MVC

MVC est adapté à JavaFX et au cours d'IHM parce qu'il sépare clairement les responsabilités :

- le modèle ne dépend pas de JavaFX ;
- les fichiers FXML ne contiennent pas la logique du jeu ;
- les contrôleurs réagissent aux événements et appellent le modèle ;
- l'interface peut être modifiée sans réécrire toute la logique.

Cette séparation rend le projet plus lisible et plus facile à présenter.

### Model

Le modèle gère :

- les ressources ;
- les personnages ;
- les actions ;
- les événements ;
- les crises ;
- l'état de la partie ;
- le score final.

Le modèle ne contient pas d'affichage console et ne connaît pas les composants JavaFX.

### View

La vue est composée de :

- fichiers FXML ;
- fichier CSS ;
- dossier d'images ;
- classe `ViewFiles` pour centraliser les chemins des ressources.

Les écrans sont construits avec des composants JavaFX simples : `BorderPane`, `VBox`, `HBox`, `GridPane`, `Label`, `Button`, `TextArea`, `ProgressBar`, `ScrollPane`.

### Controller

Les contrôleurs :

- reçoivent les événements JavaFX ;
- appellent les méthodes du modèle ;
- mettent à jour les labels, boutons et zones de texte ;
- gèrent la navigation entre les écrans ;
- désactivent les actions impossibles.

## View Layer

L'interface est construite avec FXML. Cela permet d'ouvrir les vues avec Scene Builder et de garder la structure graphique séparée du code Java.

### Organisation des vues

FXML :

```text
src/main/resources/fr/uge/but/schtroumpf/resources/fxml
```

CSS :

```text
src/main/resources/fr/uge/but/schtroumpf/resources/css/style.css
```

Images :

```text
src/main/resources/fr/uge/but/schtroumpf/resources/images
```

### CSS

`style.css` définit :

- les couleurs principales ;
- les panneaux ;
- les boutons ;
- les barres de ressources ;
- les zones d'historique ;
- les titres et messages.

Les couleurs sont inspirées de l'univers des Schtroumpfs : bleu, beige, vert forêt et rouge doux.

### Navigation

La navigation est centralisée dans `ScreenNavigator`.

Flux principal :

```text
MainMenuView.fxml -> GameView.fxml -> EndGameView.fxml
```

Le joueur peut aussi redémarrer une partie depuis l'écran de jeu ou l'écran de fin.

### MainMenuView.fxml

Rôle :

- afficher le titre ;
- expliquer brièvement le jeu ;
- proposer `Start Game` ;
- proposer `Quit`.

Widgets importants :

- `Label` de titre ;
- `Label` de description ;
- `Button` de démarrage ;
- `Button` de fermeture.

Contrôleur lié :

```text
MainMenuController
```

### GameView.fxml

Rôle :

- écran principal de gameplay.

Contenu :

- mois courant ;
- actions restantes ;
- événement courant ;
- panneau de ressources ;
- panneau des personnages et actions ;
- statut des crises ;
- historique ;
- bouton pour avancer dans le mois ;
- bouton Restart.

Widgets importants :

- `Label` ;
- `GridPane` ;
- `ProgressBar` ;
- `VBox` ;
- `ScrollPane` ;
- `TextArea` ;
- `Button`.

Contrôleur lié :

```text
GameController
```

### EndGameView.fxml

Rôle :

- afficher le résultat final ;
- afficher le score ;
- afficher les ressources finales ;
- afficher l'historique ;
- proposer Restart ou Quit.

Widgets importants :

- `Label` de résultat ;
- `Label` de score ;
- `Label` de résumé des ressources ;
- `TextArea` historique ;
- boutons Restart et Quit.

Contrôleur lié :

```text
EndGameController
```

## Controller Layer

Les contrôleurs sont dans :

```text
fr.uge.but.schtroumpf.controller
```

Ils font le lien entre les vues FXML et le modèle.

### ScreenNavigator

Rôle :

- charger les FXML ;
- créer les scènes ;
- appliquer la feuille CSS ;
- donner le navigateur aux contrôleurs ;
- changer d'écran.

Méthodes importantes :

- `showMainMenu()` ;
- `showGame(CouncilGame game)` ;
- `showEndGame(CouncilGame game)` ;
- `quit()`.

### MainMenuController

FXML lié :

```text
MainMenuView.fxml
```

Rôle :

- gérer le clic sur Start Game ;
- gérer le clic sur Quit.

Méthodes importantes :

- `handleStartGame(ActionEvent event)` ;
- `handleQuit(ActionEvent event)`.

Modèle utilisé :

- crée une nouvelle instance de `CouncilGame`.

### GameController

FXML lié :

```text
GameView.fxml
```

Rôle :

- afficher l'état courant de la partie ;
- créer les boutons d'actions ;
- gérer les clics sur les actions ;
- faire avancer le mois ;
- mettre à jour les ressources, événements, crises et historique.

Méthodes importantes :

- `setGame(CouncilGame game)` ;
- `handleNextMonth(ActionEvent event)` ;
- `handleRestart(ActionEvent event)` ;
- `refreshView()` ;
- `refreshResources()` ;
- `refreshActions()` ;
- `createActionRow(...)` ;
- `refreshCrises()` ;
- `refreshHistory()`.

Communication avec le modèle :

- lit `CouncilGame` ;
- appelle `finishMonth()` ;
- appelle `executeAction(...)` ;
- lit `SmurfVillage`, les événements, les crises et l'historique.

### EndGameController

FXML lié :

```text
EndGameView.fxml
```

Rôle :

- afficher le résultat final ;
- afficher le score ;
- afficher les ressources finales ;
- afficher l'historique ;
- gérer Restart et Quit.

Méthodes importantes :

- `setGame(CouncilGame game)` ;
- `handleRestart(ActionEvent event)` ;
- `handleQuit(ActionEvent event)` ;
- `buildResourceSummary(CouncilGame game)`.

## Model Layer

Le modèle est dans :

```text
fr.uge.but.schtroumpf.model
```

Il contient la logique du jeu et ne dépend pas de JavaFX.

### Ressource

Type :

```text
enum
```

Rôle :

- lister les ressources ;
- fournir un nom affichable avec `getDisplayName()`.

### SmurfVillage

Rôle :

- stocker les ressources ;
- appliquer les bornes 0-10 ;
- modifier les ressources ;
- gérer production, consommation et score.

Données :

- `EnumMap<Ressource, Integer> resources`.

Méthodes importantes :

- `getResource(...)` ;
- `changeResource(...)` ;
- `hasEnough(...)` ;
- `applyChanges(...)` ;
- `produceResources()` ;
- `consumeResources()` ;
- `getScore()`.

### CouncilAction

Rôle :

- représenter une action du conseil.

Données :

- nom ;
- description ;
- coûts ;
- effets.

Méthodes importantes :

- `canExecute(SmurfVillage village)` ;
- `execute(SmurfVillage village)`.

### ActionFactory

Rôle :

- simplifier la création des actions ;
- éviter de répéter la création des `EnumMap`.

### Personnage

Type :

```text
interface
```

Rôle :

- imposer les méthodes communes aux personnages.

Méthodes :

- `getName()` ;
- `getDescription()` ;
- `getActions()`.

### GrandSchtroumpf

Rôle :

- chef sage du conseil.

Actions :

- `Consult spellbook` ;
- `Organize council meeting` ;
- `Negotiate with animals`.

### SchtroumpfBricoleur

Rôle :

- spécialiste des outils, réparations et pièges.

Actions :

- `Repair houses` ;
- `Build traps` ;
- `Invent gadget`.

### SchtroumpfGourmand

Rôle :

- spécialiste de la nourriture.

Actions :

- `Gather berries` ;
- `Organize feast` ;
- `Find rare mushroom`.

### Schtroumpfette

Rôle :

- diplomate et médiatrice.

Actions :

- `Negotiate with neighbors` ;
- `Calm conflict` ;
- `Organize party`.

### Evenement

Rôle :

- représenter un événement aléatoire.

Données :

- nom ;
- description ;
- poids ;
- effets.

Méthode importante :

- `apply(SmurfVillage village)`.

### EventGenerator

Rôle :

- stocker les événements disponibles ;
- tirer un événement selon les poids.

Méthode importante :

- `drawEvent()`.

### Crisis

Type :

```text
record
```

Rôle :

- stocker le nom et la description d'une crise.

### CrisisChecker

Rôle :

- vérifier les ressources à 0 ;
- créer les crises correspondantes ;
- appliquer les conséquences.

Méthode importante :

- `checkCrises(SmurfVillage village)`.

### CouncilGame

Rôle :

- classe centrale de l'état de partie ;
- coordonne les mois, actions, événements, crises et résultat final.

Données :

- village ;
- générateur d'événements ;
- vérificateur de crises ;
- liste des personnages ;
- historique ;
- crises actives ;
- mois courant ;
- événement courant ;
- actions utilisées ;
- état de fin de partie ;
- résultat final.

Méthodes importantes :

- `executeAction(...)` ;
- `finishMonth()` ;
- `getVillage()` ;
- `getCharacters()` ;
- `getCurrentMonth()` ;
- `getRemainingActions()` ;
- `getCurrentEvent()` ;
- `isMonthStarted()` ;
- `getHistory()` ;
- `getActiveCrises()` ;
- `isGameOver()` ;
- `getResult()`.

### ActionResult

Type :

```text
record
```

Rôle :

- indiquer si une action a réussi ;
- fournir un message à afficher.

### GameResult

Type :

```text
record
```

Rôle :

- stocker le résultat final ;
- indiquer victoire/défaite, score, titre et message.

## Project Structure

### Packages Java

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

### Ressources

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

## Technical Choices

### Pourquoi FXML

FXML est utilisé car il :

- correspond aux pratiques JavaFX vues en IHM ;
- permet l'utilisation de Scene Builder ;
- sépare la structure graphique du code Java ;
- rend les écrans plus lisibles.

### Pourquoi MVC

MVC est utilisé car il :

- sépare les responsabilités ;
- évite de mélanger logique et interface ;
- rend le code plus maintenable ;
- facilite l'explication du projet.

### Pourquoi une architecture simple

L'objectif n'est pas de créer une application d'entreprise. Le projet reste volontairement simple :

- pas de couche service ;
- pas de repository ;
- pas d'injection de dépendances ;
- pas de base de données ;
- pas de multithreading avancé ;
- pas de framework externe.

Cette simplicité rend le projet cohérent avec les contraintes pédagogiques.

## Conclusion

Le projet fournit :

- une application JavaFX fonctionnelle ;
- une interface FXML ;
- un style CSS externe ;
- une architecture MVC claire ;
- une logique de jeu complète ;
- des ressources bornées ;
- des événements aléatoires ;
- des actions de personnages ;
- un système de crises ;
- un écran de fin de partie ;
- un historique de jeu.

Améliorations possibles :

- ajouter plus d'événements ;
- ajouter des illustrations de personnages ;
- séparer la vue des ressources dans un FXML dédié ;
- séparer la vue des actions dans un FXML dédié ;
- ajouter un écran d'aide ;
- ajouter des animations simples.

Ce projet montre surtout comment construire proprement une petite application JavaFX avec FXML, CSS et MVC, en gardant un code compréhensible et adapté à un contexte BUT.
