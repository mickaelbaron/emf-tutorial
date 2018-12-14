# EMF Tutoriel

Le but de cette série d'exercices est d’apprendre à manipuler le framework de modélisation d’Eclipse appelé EMF. Nous couvrirons tous les aspects liés aux développements dirigés par les modèles avec les briques logiciels fournis par la plateforme Eclipse. La version Eclipse avec le package _modeling_ sera utilisée à cet effet.

Cette série d'exercices se présente sous la forme d'un atelier composé de cinq exercices guidés :

* un premier exercice s'intéresse à la construction d'un modèle EMF ;
* un deuxième exercice montre comment générer du code Java et des outils d'assistance à partir du modèle EMF ;
* le troisième exercice explique comment instancier le modèle EMF à partir d'un éditeur graphique généré ;
* le quatrième exercice présente l'instanciation du modèle EMF en utilisant directement les codes Java générés (technique appelée EarlyBinding) ;
* le dernier exercice présente l'instanciation du modèle EMF en utilisant le métamodèle Ecore (technique appelée LateBinding).

**Buts pédagogiques** : conception de modèles EMF, génération de code Java, manipulation d’API EMF pour instancier le modèle, manipulation du méta-modèle Ecore, sauvegarde et chargement de fichiers XMI, transformation de modèles vers textes.

> Ce dépôt est utilisé dans le cadre d'un cours sur l'ingénierie dirigée par les modèles auquel je participe à l'[ISAE-ENSMA](https://www.ensma.fr) en français. Tous les supports de cours et tutoriaux sont disponibles sur ma page Developpez.com : [https://mbaron.developpez.com](https://mbaron.developpez.com/#page_soa).

## Prérequis logiciels

Avant de démarrer cette série d'exercices sur la manipulation du framework de modélisation EMF, veuillez préparer votre environnement de tests en installant :

* [Java via OpenJDK](http://jdk.java.net/)
* [Eclipse avec outils de modélisation](https://www.eclipse.org/ "Eclipse")

## Ressources

Pour aller plus loin, vous pouvez consulter les ressources suivantes :

* [Support de cours sur une introduction à la modélisation avec Eclipse EMF](https://mbaron.developpez.com/cours/eclipse/introemf/ "Support de cours sur une introduction à la modélisation avec Eclipse EMF") ;
* [Tutoriel sur Acceleo](https://younessbazhar.developpez.com/eclipse/introacceleo/ "Générer des classes Java avec Acceleo en 5 minutes") ;
* [Tutoriel sur Eclipse Papyrus](https://yassineouhammou.developpez.com/tutoriels/eclipse/uml-profil-papyrus/ "Apprendre à créer un profil UML avec Eclipse Papyrus") ;
* [Initiation à l'ingénierie dirigée par les modèles](https://yassineouhammou.developpez.com/tutoriels/conception/initiation-ingenierie-dirigee-modeles/ "Initiation à l'ingénierie dirigée par les modèles").