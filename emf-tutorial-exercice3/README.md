# Exercice 3 (EMF) : création d’instances via l’éditeur généré

Nous allons maintenant générer le code correspondant à un éditeur graphique. Cet éditeur sera utilisé pour créer graphiquement des instances de notre modèle. Nous vérifierons par ailleurs la validité de notre modèle par rapport à un jeu d’instances.

## But

* Générer un éditeur graphique.
* Exécuter une configuration d’exécution.
* Création d’instances via l’éditeur généré.
* Validation des contraintes.

## Étapes à suivre

* À partir du modèle de génération (fichier *genmodel*), générer le code de l’éditeur (**Generate Edit Code** et **Generate Editor Code**). Deux plugins doivent être créés (*eclipse.emf.addressbook.edit* et *eclipse.emf.addressbook.editor*).

* Passer en perspective Java.

> Exécution d'une application Eclipse

> Créer une configuration d’exécution (**Run -> Run Configurations...**) à partir d’un type Eclipse Application. Nommer cette configuration *AddressBookConfiguration*, modifier la valeur de son chemin avec cette valeur (*${workspace_loc}/runtime-AddressBookConfiguration*) puis ajouter vos trois plugins (*addressbook*, *edit* et *editor*). Décocher *Target Platform* puis faites **Add Required Plug-ins**. Ajouter enfin le plugin *org.eclipse.ui.ide.application* et *org.eclipse.ui.navigator.resources* et faires une nouvelle fois **Add Required Plug-ins**.

Ou

> Une solution plus simple pour la création automatique de configuration d'exécution est de passer par le fichier *MANIFEST.MF* du plugin *eclipse.emf.addressbook*. Depuis l'éditeur spécifique du fichier *MANIFEST.MF* une option **Launch an Eclipse application** est disponible pour démarrer l'exécution ou tout simplement via le menu contextuel sur le fichier *MANIFEST.MF* faire **Run As -> Eclipse Application**. Cette solution ne permet pas de configurer l'exécution. Si vous avez besoin de paramétrer la configuration d'exécution, lancer une première exécution puis revenez pour l'éditer.

* Exécuter cette configuration d’exécution. Une nouvelle instance d’Eclipse s’exécute en intégrant votre éditeur de modèle de carnet d’adresse.

* Créer un simple projet (**File -> New -> Project... -> General -> Project**) que vous appellerez *AddressBookSampleInstances*.

* À partir de cette nouvelle instance, créer une instance du modèle *AddressBook* (**File -> New -> Other... -> Example EMF Model Creation Wizards -> Addressbook Model**) que vous appellerez *sample.addressbook*. Choisir ensuite `Address Book` comme classe racine.

* Construire les instances via l’éditeur associé à votre modèle en s’appuyant sur les instances données ci-dessous.

```xml
<addressbook:AddressBook ...>
  <contains familyName="DUPONT" firstName="Raoul" age="37">
    <location number="1" street="Rue DotNet"/>
  </contains>
  <contains familyName="BARON" firstName="Mickael" age="36">
    <location number="50" street="Place de Java"/>
  </contains>
  <contains familyName="SARPOL" firstName="John" age="38">
    <location number="17" street="Square Express"/>
  </contains>
</addressbook:AddressBook>
```

* Sélectionner le nœud racine de vos instances et valider ces instances en cliquant sur Validate (via le menu contextuel).