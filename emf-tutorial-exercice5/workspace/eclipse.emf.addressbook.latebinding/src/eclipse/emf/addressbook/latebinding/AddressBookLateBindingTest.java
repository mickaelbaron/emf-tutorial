package eclipse.emf.addressbook.latebinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AddressBookLateBindingTest {

	@Test
	public void getAddressBookStructureWithoutGeneratedCodeTest() {
		// Given
		StringBuffer content = new StringBuffer();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("ecore", new XMIResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		URI fileURI = URI.createFileURI("model/addressbook.ecore");
		Resource resource = resourceSet.getResource(fileURI, true);

		EPackage ePackage = (EPackage) resource.getContents().get(0);

		EList<EClassifier> eClassifiers = ePackage.getEClassifiers();

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
		Assert.assertEquals("AddressBook\n" + 
				"  Attributs : name(EString) \n" + 
				"  Références : contains(Person[0..-1])\n" + 
				"Person\n" + 
				"  Attributs : firstName(EString) familyName(EString) age(EInt) identifier(EString) \n" + 
				"  Références : location(Address[1..1])\n" + 
				"  Opérations : EString display \n" + 
				"Address\n" + 
				"  Attributs : number(EInt) street(EString) \n", string);
	}
	
	@Test
	public void saveAndLoadAddressBookWithoutGeneratedCodeTest() throws IOException {
		// Given
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("ecore", new XMIResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		URI fileURI = URI.createFileURI("model/addressbook.ecore");
		Resource resource = resourceSet.createResource(fileURI);
		resource.load(null);
		
		EPackage ePackage = (EPackage) resource.getContents().get(0);

		EClass eAddressBook = (EClass) ePackage.getEClassifier("AddressBook");
		EReference eContains = (EReference) eAddressBook
				.getEStructuralFeature("contains");
		EAttribute eName = (EAttribute) eAddressBook
				.getEStructuralFeature("name");
		EObject addressBookInstance = ePackage.getEFactoryInstance().create(
				eAddressBook);
		addressBookInstance.eSet(eName, "Mon Carnet d'Adresses");

		List<EObject> containsList = new ArrayList<EObject>();
		
		EClass ePerson = (EClass) ePackage.getEClassifier("Person");
		EAttribute eFirstName = (EAttribute) ePerson
				.getEStructuralFeature("firstName");
		EAttribute eFamilyName = (EAttribute) ePerson
				.getEStructuralFeature("familyName");
		EObject personInstance = ePackage.getEFactoryInstance().create(ePerson);
		personInstance.eSet(eFirstName, "Mickael");
		personInstance.eSet(eFamilyName, "BARON");
		containsList.add(personInstance);

		personInstance = ePackage.getEFactoryInstance().create(ePerson);
		personInstance.eSet(eFirstName, "Raoul");
		personInstance.eSet(eFamilyName, "DUPONT");
		containsList.add(personInstance);
		
		personInstance = ePackage.getEFactoryInstance().create(ePerson);
		personInstance.eSet(eFirstName, "John");
		personInstance.eSet(eFamilyName, "SARPOL");
		containsList.add(personInstance);
		
		addressBookInstance.eSet(eContains, containsList);

		// When
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		String pathname = "addressbookinstancesonlymodel.xmi";
		URI uri = URI.createURI("file:" + pathname);
		resource = resourceSet.createResource(uri);
		resource.getContents().add(addressBookInstance);
		HashMap<String, Object> opts = new HashMap<String, Object>();
		opts.put(XMIResource.OPTION_SCHEMA_LOCATION, true);
		resource.save(opts);
		// Then
		Assert.assertTrue(new File(pathname).exists());
		
		// When
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		Registry packageRegistry = resourceSet.getPackageRegistry();
		packageRegistry.put("http://addressbook/1.0", ePackage);

		uri = URI.createURI("file:" + pathname);
		resource = resourceSet.getResource(uri, true);
		resource.load(null);
		// Then		
		EObject addressBookImpl = resource.getContents().get(0);
		EClass addressBook = addressBookImpl.eClass();
		EAttribute nameAttribute = (EAttribute)(addressBook.getEStructuralFeature("name"));
		EReference containsAttribute = (EReference)(addressBook.getEStructuralFeature("contains"));
		Assert.assertEquals("Mon Carnet d'Adresses", addressBookImpl.eGet(nameAttribute));

		EcoreEList eGets = (EcoreEList)addressBookImpl.eGet(containsAttribute);
		EObject personImpl = (EObject)eGets.get(0);
		EClass person = personImpl.eClass();
		EAttribute familyName = (EAttribute)person.getEStructuralFeature("familyName");
		EAttribute firstName = (EAttribute)person.getEStructuralFeature("firstName");
		
		Assert.assertEquals("BARON", personImpl.eGet(familyName));
		Assert.assertEquals("Mickael", personImpl.eGet(firstName));
	}
}
