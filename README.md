# Groupe Z

Idris Mohamed BENARBIA  
Yendoumban NANMEDIGOU

---

## Règles de jeu (Fonctionnalités supplémentaires)

### Types de quiz

En plus des types questions à choix multiples, questions de précision et dynamic sort proposés, nous avons ajouté un le matching quiz. Il consiste à faire correspondre les éléments deux à deux dans une liste d'informations donnée.  
Par exemple, dans une liste de faits historiques et de dates, il faudra matcher chaque fait avec sa date.

### Learning

Nous avons rajouter la possibilité, pour les utilisateurs premium uniquement, de s'entraîner (apprendre) avant un quiz. En choisissant les domaines souhaités, l'utilisateur a accès à des flip cards d'infos (l'élément concerné en front et les infos associées en back).  
Une carte sur la planète Jupiter révèlera donc derrière son diamètre, sa position dans le système solaire et sa température.

---

## Données

### Nature

Nous avons choisi 5 domaines: Geopgraphy, History, Sports, Astronomy et Chemistry. Ils ont nécessités donc respectivement la création des tables Country, HistoricEvent, FootballTeam, Planet et ChemicalElement.  
La création des tables Game et GameQuestion a elle été nécessaire pour implémenter la fonctionnalité d'historique.

### Acquisition

Les tables, comportant une cinquantaine de ligne chacune (sauf Planet qui en comporte 8), contiennent des informations dont ChatGPT est la source. Nous les avons récupérer sous forme de json (on peut les retrouver dans le dossier assets) puis insérer dans la BD à la création (voir la classe data/seed/DatabaseSeeder)

---

## Autres informations

### Gestion de difficulté

Nous avons opté pour une gestion de la difficulté basé sur le choix de la question. Nos tables contiennent en effet une colonne notoriety qui rend compte de la popularité de l'information.  
Dans la table Country par exemple, la ligne sur la France sera de notoriety 1 alors que celle sur l'Albanie sera de notoriety 3. La difficulté d'une question sera donc fonction de la popularité des informations qu'elle demande à l'utilisateur.

### Plus réalisés

En plus d'une interface ergonomique et agréable, le rappel par notification pour jouer (avec choix de la fréquence possible) et le paramètrage du thème de l'application ont été traités.
