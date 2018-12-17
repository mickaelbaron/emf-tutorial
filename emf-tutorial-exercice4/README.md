# Exercice 4 (EMF) : création d’instances via l’API EMF => EarlyBinding

Nous allons dans cet exercice créer des instances d’un modèle de manière programmatique à partir de classes connues avant l’exécution du programme (*EarlyBinding*). Dans ce cas les plugins générés précédemment (*Edit* et *Editor*) ne seront pas utilisés. Nous utiliserons un plugin spécifique appelé *fragment* (utilisé pour enrichir un plugin existant) pour créer des classes de tests.

## But

* Créer des instances via l’API EMF.
* Créer un plugin (fragment) de test.
* Sauvegarder et charger via l’API EMF des instances via un XMI (en utilisant la première instance d’Eclipse).

## Étapes à suivre

* Créer un nouveau plugin de type *fragment* (**File -> New -> Other... -> Plug-in Development -> Fragment Project**) nommé *eclipse.emf.addressbook.test*. Choisir comme plugin hôte *eclipse.emf.addressbook* (créé dans l’exercice 1). Une fois le fragment créé, ajouter la dépendance (onglet **Dependencies** du fichier *manifest.mf*) vers le plugin *org.junit* (4.12.0).

* Créer un package `eclipse.emf.addressbook.model.test` et créer une classe appelée `AddressBookTest`.

* Depuis la classe `AddressBookTest` ajouter une méthode de tests `saveAndLoadAddressBookTest` en vous s’assurant que les assertions soient vraies. Il vous est demandé de construire des instances (via la fabrique `AddressbookFactory`) et de modifier les valeurs des attributs.

```java
public class AddressBookTest {

  @Test
  public void saveAndLoadAddressBookTest() {
    // Given
    Address createMBAddress = AddressbookFactory.eINSTANCE.createAddress();
    createMBAddress.setNumber(50);
    // À compléter...

    AddressBook createAddressBook = AddressbookFactory.eINSTANCE.createAddressBook();
    createAddressBook.setName("Mon Carnet d'Adresses");
    // A compléter...

    Assert.assertEquals(3, createAddressBook.getContains().size());
    Assert.assertEquals("Mon Carnet d'Adresses", createAddressBook.getName());
    Assert.assertEquals("BARON", mickaelBaron.getFamilyName());
    Assert.assertEquals("Place de Java", mickaelBaron.getLocation().getStreet());
    Assert.assertEquals("Raoul", raoulDupont.getFirstName());
    Assert.assertEquals("John SARPOL 38", johnSarpol.getIdentifier());
  }
}
```

* Nous nous intéressons maintenant à sauvegarder des instances depuis un fichier XMI. Compléter la méthode de tests précédente de manière à sauvegarder les instances créées précédemment (voir code ci-dessous). Le fichier d’instances sera stocké dans le répertoire utilisé par la configuration d’exécution de l’exercice 3. Ajouter dans votre plugin la dépendance vers le plugin *org.eclipse.emf.ecore.xmi*. Dû à la présence de ce plugin, l’exécution du test unitaire doit se faire obligatoirement dans un environnement de plugins (**Run As JUnit Plug-in Test**).

```java
public class AddressBookTest {

  @Test
  public void saveAndLoadAddressBookTest() throws IOException {
    ...
  
    // When
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("addressbook", new XMIResourceFactoryImpl());
    String pathname = "apisample.addressbook";
    URI uri = URI.createURI("file:" + pathname);
    Resource resource = resourceSet.createResource(uri);
    resource.getContents().add(createAddressBook);
    resource.save(null);
    // Then
    Assert.assertTrue(new File(pathname).exists());
  }
}
```

* Nous nous intéressons ensuite à charger des instances depuis un fichier XMI. Ajouter une nouvelle méthode de test dont le but est de vérifier que le chargement des instances créées lors de l’exercice 3 (fichier *sample.addressbook*) fonctionne.

```java
public class AddressBookTest {

  @Test
  public void saveAndLoadAddressBookTest() {
    ...

    // When
    resourceSet = new ResourceSetImpl();
    uri = URI.createURI("file:/" + apiSamplePath + "sample.addressbook");
    resource = resourceSet.getResource(uri, true);
    createAddressBook = (AddressBook) resource.getContents().get(0);
    Assert.assertNotNull(createAddressBook.getContains());
    // Then
    Assert.assertEquals(3, createAddressBook.getContains().size());
  }
}
```

* Finalement, ajouter une nouvelle méthode de test `notifyChangedTest` qui se chargera de tester si l'ajout d'un écouteur de notification sur une instance de type `AddressBook` fonctionne correctement. Il vous sera demandé de compléter le code du test afin de trouver la meilleure implémentation pour s'assurer que lorsqu'une modification sur le modèle est effectuée, une notification est invoquée (déclenchement de la méthode `notifyChanged`).

```java
public class AddressBookTest {

  @Test
  public void notifyChangedTest() throws InterruptedException {
    // Given
    AddressBook createAddressBook = AddressbookFactory.eINSTANCE.createAddressBook();
    createAddressBook.eAdapters().add(new EContentAdapter() {
      @Override
      public void notifyChanged(Notification notification) {
        Assert.assertNull(notification.getOldValue());
        Assert.assertEquals("Mon Carnet d'Adresses", notification.getNewValue());
      }
    });

    // When
    createAddressBook.setName("Mon Carnet d'Adresses");
    // Then
    // À compléter : s'assurer que lorsqu'une modification
    // sur le modèle est effectuée, une  notification est invoquée.
  }
}
```