package eclipse.emf.addressbook.model.test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

import eclipse.emf.addressbook.model.addressbook.Address;
import eclipse.emf.addressbook.model.addressbook.AddressBook;
import eclipse.emf.addressbook.model.addressbook.AddressbookFactory;
import eclipse.emf.addressbook.model.addressbook.Person;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AddressBookTest {

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
	    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("addressbook", new XMIResourceFactoryImpl());
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
	
	@Test
	public void notifyChangedTest() throws InterruptedException {
		final CountDownLatch messageLatch = new CountDownLatch(1);
		
		// Given		
		AddressBook createAddressBook = AddressbookFactory.eINSTANCE.createAddressBook();
		createAddressBook.eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				Assert.assertNull(notification.getOldValue());
				Assert.assertEquals("Mon Carnet d'Adresses", notification.getNewValue());
				messageLatch.countDown();
			}			
		});
		
		// When
		createAddressBook.setName("Mon Carnet d'Adresses");
		// Then
		Assert.assertTrue(messageLatch.await(2, TimeUnit.SECONDS));
	}
}
