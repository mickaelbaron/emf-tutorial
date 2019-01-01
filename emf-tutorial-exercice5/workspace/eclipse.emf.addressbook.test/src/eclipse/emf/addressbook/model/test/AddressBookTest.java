package eclipse.emf.addressbook.model.test;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

import eclipse.emf.addressbook.model.addressbook.Address;
import eclipse.emf.addressbook.model.addressbook.AddressBook;
import eclipse.emf.addressbook.model.addressbook.AddressbookFactory;
import eclipse.emf.addressbook.model.addressbook.AddressbookPackage;
import eclipse.emf.addressbook.model.addressbook.Person;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AddressBookTest {

	@Test
	public void getAddressBookStructureTest() {
		// Given
		StringBuffer content = new StringBuffer();

		AddressbookPackage addressbookPackage = AddressbookPackage.eINSTANCE;
		EList<EClassifier> eClassifiers = addressbookPackage.getEClassifiers();

		for (EClassifier eClassifier : eClassifiers) {
			content.append(eClassifier.getName() + "\n");

			if (eClassifier instanceof EClass) {
				EClass eClass = (EClass) eClassifier;

				EList<EAttribute> eAttributes = eClass.getEAttributes();
				if (!eAttributes.isEmpty()) {
					content.append("  Attributs : ");
					for (EAttribute eAttribute : eAttributes) {
						content.append(eAttribute.getName() + "(" + eAttribute.getEAttributeType().getName() + ") ");
					}
				}

				EList<EReference> eReferences = eClass.getEReferences();
				if (!eReferences.isEmpty()) {
					content.append("\n");
					content.append("  Références : ");

					for (EReference eReference : eReferences) {
						content.append(eReference.getName() + "(" + eReference.getEReferenceType().getName() + "["
								+ eReference.getLowerBound() + ".." + eReference.getUpperBound() + "])");
					}
				}

				EList<EOperation> eOperations = eClass.getEOperations();
				if (!eOperations.isEmpty()) {
					content.append("\n");
					content.append("  Opérations : ");

					for (EOperation eOperation : eOperations) {
						content.append(eOperation.getEType().getName() + " " + eOperation.getName() + " ");
					}
				}
			}
			content.append("\n");
		}

		// When
		String string = content.toString();
		System.out.println(string);

		// Then
		Assert.assertEquals("AddressBook\n" + "  Attributs : name(EString) \n"
				+ "  Références : contains(Person[0..-1])\n" + "Person\n"
				+ "  Attributs : firstName(EString) familyName(EString) age(EInt) identifier(EString) \n"
				+ "  Références : location(Address[1..1])\n" + "  Opérations : EString display \n" + "Address\n"
				+ "  Attributs : number(EInt) street(EString) \n", string);
	}

	@Test
	public void saveAndLoadAddressBookTest() throws IOException {
		// Given
		Address createMBAddress = AddressbookFactory.eINSTANCE.createAddress();
		createMBAddress.setNumber(50);
		createMBAddress.setStreet("Place de Java");
		Person mickaelBaron = AddressbookFactory.eINSTANCE.createPerson();
		mickaelBaron.setAge(36);
		mickaelBaron.setFamilyName("BARON");
		mickaelBaron.setFirstName("Mickael");
		mickaelBaron.setLocation(createMBAddress);

		Address createDRAddress = AddressbookFactory.eINSTANCE.createAddress();
		createDRAddress.setNumber(1);
		createDRAddress.setStreet("Rue DotNet");
		Person raoulDupont = AddressbookFactory.eINSTANCE.createPerson();
		raoulDupont.setAge(37);
		raoulDupont.setFamilyName("DUPONT");
		raoulDupont.setFirstName("Raoul");
		raoulDupont.setLocation(createDRAddress);

		Address createSJAddress = AddressbookFactory.eINSTANCE.createAddress();
		createSJAddress.setNumber(50);
		createSJAddress.setStreet("Square Express");
		Person johnSarpol = AddressbookFactory.eINSTANCE.createPerson();
		johnSarpol.setAge(38);
		johnSarpol.setFamilyName("SARPOL");
		johnSarpol.setFirstName("John");
		johnSarpol.setLocation(createSJAddress);

		AddressBook createAddressBook = AddressbookFactory.eINSTANCE.createAddressBook();
		createAddressBook.setName("Mon Carnet d'Adresses");

		createAddressBook.getContains().add(mickaelBaron);
		createAddressBook.getContains().add(raoulDupont);
		createAddressBook.getContains().add(johnSarpol);

		Assert.assertEquals(3, createAddressBook.getContains().size());
		Assert.assertEquals("Mon Carnet d'Adresses", createAddressBook.getName());
		Assert.assertEquals("BARON", mickaelBaron.getFamilyName());
		Assert.assertEquals("Place de Java", mickaelBaron.getLocation().getStreet());
		Assert.assertEquals("Raoul", raoulDupont.getFirstName());
		Assert.assertEquals("John SARPOL 38", johnSarpol.getIdentifier());

		// When
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("addressbook",
				new XMIResourceFactoryImpl());
		String pathname = "apisample.addressbook";
		URI uri = URI.createURI("file:" + pathname);
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(createAddressBook);
		resource.save(null);
		// Then
		Assert.assertTrue(new File(pathname).exists());

		// When
		resourceSet = new ResourceSetImpl();
		uri = URI.createURI("file:../../runtime-AddressBook/AddressBookSampleInstances/sample.addressbook");
		resource = resourceSet.getResource(uri, true);
		createAddressBook = (AddressBook) resource.getContents().get(0);
		// Then
		Assert.assertEquals(3, createAddressBook.getContains().size());
	}
}
