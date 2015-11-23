package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.util.HealthMeasureHistoryList;
import introsde.rest.model.HealthMeasureHistory;
import introsde.rest.model.HealthProfile;
import introsde.rest.model.MeasureDefinition;
import introsde.rest.model.Person;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;



@Stateless
// will work only inside a Java EE application
@LocalBean
// will work only inside a Java EE application
@Path("/person")
public class PersonCollectionResource {

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
    
 // Defines that the next path parameter after the base url is
    // treated as a parameter and passed to the PersonResources
    // Allows to type http://localhost:599/base_url/1
    // 1 will be treaded as parameter todo and passed to PersonResource
    @Path("{PersonId}")
    public PersonResource getPerson(@PathParam("PersonId") int id) {
	return new PersonResource(uriInfo, request, id);
    }

 // Return the list of people to the user in the browser
 	@GET
 	@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
 	public List<Person> getPersonsBrowser() {
 		System.out.println("Getting list of people...");
 	    List<Person> people = Person.getAll();
 		return people;
 	}
   
    // returns the number of people  to get the total number of records
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
	System.out.println("Getting count...");
	List<Person> people = Person.getAll();
	int count = people.size();
	return String.valueOf(count);
    }

    // Defines that the next path parameter after the base url is
    // treated as a parameter and passed to the PersonResources
    // Allows to type http://localhost:599/base_url/1
    // 1 will be treaded as parameter to do and passed to PersonResource

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Person newPerson(Person Person) throws IOException {
	System.out.println("Creating new Person..." + Person.getBirthdate());
	return Person.savePerson(Person);
    }

  //*******************************************************************************************************//
  	//Request #6: GET /person/{id}/{measureType} should return the list of values (the history) of {measureType} 
  	//(e.g. weight) for person identified by {id}
  	//*******************************************************************************************************//
  	
    @GET
    @Path("{PersonId}/{measureType}")
    @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public HealthMeasureHistoryList getMeasureTypeByPerson(@PathParam("PersonId") int id, @PathParam("measureType") String measureType) {
	try {

		HealthMeasureHistoryList chmhObj = new HealthMeasureHistoryList();
	    List<HealthMeasureHistory> healthMeasureHistoryLst = new ArrayList<HealthMeasureHistory>();
	    
		healthMeasureHistoryLst = HealthMeasureHistory
			.getHealthMeasureHistoryByPersonIdandMeasureType(id,
				measureType);

	    System.out.println("returned Health measure history for person with id::: " + id
		    + "=" + healthMeasureHistoryLst);
	    chmhObj.setHealthMeasureHistoryLst(healthMeasureHistoryLst);
	    return chmhObj;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new HealthMeasureHistoryList();
	}
    }

  //**********************************************************************************//
  	//Request #7: *GET /person/{id}/{measureType}/{mid} should return the value of {measureType} 
  	//(e.g. weight) identified by {mid} for person identified by {id}
  	//***************************************************************************************//
  	  	
    @GET
    @Path("{PersonId}/{measureType}/{mid}")
    @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public HealthMeasureHistoryList getMeasureHistoryByPersonAndMeasureHistoryid(
	    @PathParam("PersonId") int id, @PathParam("measureType") String measureType, @PathParam("mid") int mid) {
	try {
		HealthMeasureHistoryList hmhlstObj = new HealthMeasureHistoryList();
	    List<HealthMeasureHistory> healthMeasure = new ArrayList<HealthMeasureHistory>();
	    healthMeasure = HealthMeasureHistory
		    .getMeasureHistoryByPersonidAndMeasureHistoryidAndMeasureType(id, measureType, mid);
	    System.out.println("returned History for person with id::: " + id
		    + "and mid:::::" + mid + " =" + healthMeasure);
	    hmhlstObj.setHealthMeasureHistoryLst(healthMeasure);
	    return hmhlstObj;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return new HealthMeasureHistoryList();
	}
    }
    //****************************************************************************************
    //Request #8: POST /person/{id}/{measureType} should save a new value for the {measureType} 
    //(e.g. weight) of person identified by {id} and archive the old value in the history
    //*****************************************************************************************
    @POST
    @Path("{PersonId}/{measureType}")
    @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes(MediaType.APPLICATION_XML)
    public HealthMeasureHistoryList newMeasure(HealthMeasureHistory healthMeasureHistory,@PathParam("PersonId") int personid, @PathParam("measureType") String measureType) throws IOException {

    HealthMeasureHistoryList HealthMeasureHistoryList = new HealthMeasureHistoryList();
	List<HealthMeasureHistory> healthMeasureLst = new ArrayList<HealthMeasureHistory>();

	Person person = Person.getPersonById(personid);
	System.out.println("Returned person:::: " + person);

	MeasureDefinition measureDefinition = MeasureDefinition
		.getMeasureDefinitionByMeasureType(measureType);
	System.out.println("Returned measureDefinition::::: "
		+ measureDefinition);

	healthMeasureHistory.setMeasureDefinition(measureDefinition);
	healthMeasureHistory.setPerson(person);
	HealthMeasureHistory savedHealthMeasureHistory = HealthMeasureHistory
		.saveHealthMeasureHistory(healthMeasureHistory);
	healthMeasureLst.add(savedHealthMeasureHistory);
	HealthMeasureHistoryList
		.setHealthMeasureHistoryLst(healthMeasureLst);

	return HealthMeasureHistoryList;
    }
    //*********************************************************************************************
    //(Request #10): PUT /person/{id}/{measureType}/{mid} should update the value for 
    //the {measureType} (e.g., weight) identified by {mid}, related to the person identified by {id}
   //*************************************************************************************************
    @PUT
    @Path("{id}/{measureType}/{mid}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response putHealthMeasureHistory(
	    HealthMeasureHistory healthMeasureHistory,
	    @PathParam("id") int personid,
	    @PathParam("measureType") String measureType,
	    @PathParam("mid") int mid) {

	Response res;
	Person existingPerson = Person.getPersonById(personid);
	HealthMeasureHistory existingHealthMeasureHistory = HealthMeasureHistory
		.getHealthMeasureHistoryById(mid);
	MeasureDefinition existingmeasureDefunition = MeasureDefinition
		.getMeasureDefinitionByMeasureType(measureType);

	if (existingHealthMeasureHistory == null) {
	    res = Response.noContent().build();
	} else {
	    res = Response.created(uriInfo.getAbsolutePath()).build();
	    healthMeasureHistory.setIdMeasureHistory(mid);
	    healthMeasureHistory
		    .setMeasureDefinition(existingmeasureDefunition);
	    healthMeasureHistory.setPerson(existingPerson);
	    HealthMeasureHistory
		    .updateHealthMeasureHistory(healthMeasureHistory);
	}

	return res;
    }
    
   
    
    
}
