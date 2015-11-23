package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.util.MeasureDefinitionTypeList;
import introsde.rest.model.MeasureDefinition;
import introsde.rest.model.Person;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;



@Stateless
// will work only inside a Java EE application
@LocalBean
// will work only inside a Java EE application
@Path("/measureTypes")
public class MeasureDefinitionCollectionResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	// will work only inside a Java EE application
	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	// Return the list of people to the user in the browser
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public MeasureDefinitionTypeList getPersonsBrowser() {
		MeasureDefinitionTypeList customMeasureDefinition = new MeasureDefinitionTypeList();
		List<String> measureTypes = new ArrayList<String>();
		List<MeasureDefinition> measuredefinitionlist = MeasureDefinition.getAll();
		for (MeasureDefinition mdobj : measuredefinitionlist) {
			measureTypes.add(mdobj.getMeasureName());
		}
		customMeasureDefinition.setMeasureType(measureTypes);
		return customMeasureDefinition;
	}
	// Return the list of measure to the user in the browser
		/*	@GET
			@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
			public List<MeasureDefinition> getMeasureDefinitionBrowser() {
				System.out.println("Getting Measure types ...");
			    List<MeasureDefinition> people = MeasureDefinition.getAll();
				return people;
			}*/
}


