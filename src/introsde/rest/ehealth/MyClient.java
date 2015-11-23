package introsde.rest.ehealth;

import introsde.rest.ehealth.util.ServiceFactory;
import introsde.rest.ehealth.util.HealthMeasureHistoryList;
import introsde.rest.ehealth.util.MeasureDefinitionTypeList;
import introsde.rest.model.HealthMeasureHistory;
import introsde.rest.model.MeasureDefinition;
import introsde.rest.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import javax.swing.text.DefaultEditorKit.CutAction;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.inject.Custom;




import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

public class MyClient {

    private static String baseUrl;
    static String path = "person";
    static String measurePath = "measureTypes";
    static int firstPersonId;
    static int lastPersonId;
    static int createdperonid;
    static WebTarget service;
    static Response response;
    static List<String> measure_types;
    static int mid;
    static int createdmid;
    static String storedmeasureType;
    static int measureCount;
   

    static String personxmlpayload = "<person>"
	    + "<firstname>Chuck</firstname>" + "<lastname>Norris</lastname>"
	    + "<birthdate>1945-01-01</birthdate>" + "<healthProfile>"
	    + "<height>172</height>" + "<weight>78.9</weight>"
	    + "</healthProfile>" + "</person>";

    static String measurexmlpayload = "<measure>" + "<value>72</value>"
	    + "<timestamp>2011-12-09</timestamp>" + "</measure>";

    static String measurexmlpayloadtoupdate = "<measure>" + "<value>90</value>"
	    + "<timestamp>2011-12-09</timestamp>" + "</measure>";

    // static String personjsonpayload = "\r\n{\r\n\""
    // + " \"firstname\"     : \"Chuck1234\",\r\n\"" +
    // "\"lastname\"      : \"Norris\",\r\n\""
    // + " \"birthdate\"     : \"1945-01-01\",\r\n\"" +
    // "\"healthProfile\" : \r\n{\r\n"
    // + "\"weight\"  : 78.9,\r\n\"" + "\"height\"  : 172" + "\r\n}\r\n" +
    // "\r\n}\r\n";

    public static void main(String[] args) throws InterruptedException {
	captureInput();
	
	ServiceFactory serviceFactory = new ServiceFactory();
	URI uri = getBaseURI();
	service = serviceFactory.getService(uri);
	// GET /person getting all people
	response = getserviceResponse("person");
	List<Person> pList = response
		.readEntity(new GenericType<List<Person>>() {
		});
	print("GET /"  + path, "allpeople", pList, null, response);

	// getting person by first personid on the list GET /person/{id}
	response = getserviceResponse(path + "/" + firstPersonId);
	Person firstperson = response.readEntity(Person.class);
	print("GET /" + path +"/"+ firstPersonId, "first_person_id", null, firstperson, response);
	
	// getting person by last personid on the list GET /person/{id}
	response = getserviceResponse(path + "/" + lastPersonId);
	Person lastperson = response.readEntity(Person.class);
	print("GET /" + path +"/"+ lastPersonId, "last_person_id", null, lastperson, response);

		
	// making a post with person xml POST /person
	postserviceResponse("person", personxmlpayload, "xmlpayLoad");

	// getting person by returned person id GET /person/{id}
	response = getserviceResponse(path + "/" + createdperonid);
	Person returnedperson = response.readEntity(Person.class);
	print("GET /" + path + createdperonid, "createdPerson", null, returnedperson, response);

	// GET BASE_URL/measureTypes
	response = getserviceResponse("measureTypes");
	MeasureDefinitionTypeList measureDefinition = response
		.readEntity(MeasureDefinitionTypeList.class);
	print("GET /" + measurePath, "measureTypes", null, measureDefinition,
		response);

	// GET BASE_URL/person/{id}/{measureType}
	HealthMeasureHistoryList measureHistory = null;
	System.out
		.println("Request #6: GET"+ "/" + measurePath +"/" + firstPersonId);

	for (String measureType : measure_types) {
	    response = getserviceResponse(path + "/" + firstPersonId+ "/"
		    + measureType);
	    measureHistory = response
		    .readEntity(HealthMeasureHistoryList.class);
	    print( path + "/" + firstPersonId + "/" + measureType,
			    "measureforperson", null, measureHistory, response);
	}
	

	// GET BASE_URL/person/{id}/{measureType}
	System.out
		.println("Request #6: GET"+ "/" + measurePath +"/" + lastPersonId);

	for (String measureType : measure_types) {

	    response = getserviceResponse(path + "/" + lastPersonId + "/"
			    + measureType);
	    measureHistory = response
		    .readEntity(HealthMeasureHistoryList.class);
	   // print( measureType,"measureforperson", null, measureHistory, response);
	    print( path + "/" + lastPersonId + "/" + measureType,
			    "measureforperson", null, measureHistory, response);
	    //print("GET"+ "/" + path + "/" + lastPersonId + "/" + measureType,
		   // "measureforperson", null, measureHistory, response);
	}

	// GET BASE_URL/person/{id}/{measureType}/{mid}
	response = getserviceResponse(path + "/" + lastPersonId + "/"
		+ storedmeasureType + "/" + mid);
	measureHistory = response.readEntity(HealthMeasureHistoryList.class);
	print("GET /"+ path + "/" + firstPersonId + "/" + storedmeasureType
		+ "/" + mid, "measureforpersonandmid", null, measureHistory,response);
	

	// GET BASE_URL/person/{first_person_id}/{measureType}
	response = getserviceResponse(path + "/" + firstPersonId + "/"
		+ storedmeasureType);
	measureHistory = response.readEntity(HealthMeasureHistoryList.class);
	measureCount = measureHistory.getHealthMeasureHistoryLst().size();
	System.out.println(" measure count before update: " + measureCount);

	// POST BASE_URL/person/{first_person_id}/{measureType}
	postserviceResponse(path + "/" + firstPersonId + "/"
		+ storedmeasureType, measurexmlpayload, "measurexmlpayLoad");

	// GET BASE_URL/person/{first_person_id}/{measureType}
	response = getserviceResponse(path + "/" + firstPersonId + "/"
		+ storedmeasureType);
	measureHistory = response.readEntity(HealthMeasureHistoryList.class);
	measureCount = measureHistory.getHealthMeasureHistoryLst().size();
	System.out.println("measure count after update using POST Request#8   " + measureCount);

	// update measuretype
	updateServiceResponse(path + "/" + firstPersonId + "/"
		+ storedmeasureType + "/" + createdmid,
		measurexmlpayloadtoupdate, "");

	// getting updated measure
	response = getserviceResponse(path + "/" + firstPersonId + "/"
		+ storedmeasureType + "/" + createdmid);
	measureHistory = response.readEntity(HealthMeasureHistoryList.class);
	print("GET /" + path + "/" + firstPersonId + "/" + storedmeasureType
		+ "/" + createdmid, "measureforpersonandmidfaterupdate", null,
		measureHistory, response);

	
    }

    private static URI getBaseURI() {
	return UriBuilder.fromUri(baseUrl).build();
    }

    public static void captureInput() {
	try {
	    BufferedReader bufferRead = new BufferedReader(
		    new InputStreamReader(System.in));
	    System.out
		    .println("Enter Base Url: http://localhost:4843/sdelab/");
	    baseUrl = bufferRead.readLine();

	   /* System.out.println("Enter before date");
	    beforeDate = bufferRead.readLine();

	    System.out.println("Enter after date");
	    afterDate = bufferRead.readLine();*/

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void print(String path, String description, List<?> pList,
	    Object object, Response response) {
	if (description.equalsIgnoreCase("allpeople")) {
	    @SuppressWarnings("unchecked")
	    List<Person> personList = (List<Person>) pList;
	    firstPersonId = personList.get(0).getIdPerson();
	    lastPersonId = personList.get(pList.size() - 1).getIdPerson();

	    System.out.println("Request #1:"+ path+" Accept:"+response.getMediaType() +" Content-Type:"+response.getMediaType()
		    + "\nResult:"+response.getStatusInfo()
		    + "\nHTTP Status:" + response.getStatus()+"\n"+" Number of people =" + pList.size()+"\n");
	}

	if (description.equalsIgnoreCase("first_person_id")) {
	    Person person = (Person) object;
	    System.out.println("Request #2:" +path +"Accept:"+response.getMediaType() +"Content-Type:"+response.getMediaType()+
	    		"\nResult:"+response.getStatusInfo()+ "\nHTTP Status:" + response.getStatus()+"\n"
		    + " Person First Name on list= " + person.getFirstname()+"\n");
	}

	if (description.equalsIgnoreCase("last_person_id")) {
	    Person person = (Person) object;
	    System.out.println("Request #3:" + path +"Accept:"+response.getMediaType() +"Content-Type:"+response.getMediaType()
		    + "\nResult:"+response.getStatusInfo()
		    + "\nHTTP Status:" + response.getStatus()+"\n"+" Person First Name on list= " + person.getFirstname()+"\n");
	}

	if (description.equalsIgnoreCase("createdPerson")) {
	    Person person = (Person) object;
	    System.out.println("Request #5:" + path+"Accept:"+response.getMediaType() +"Content-Type:"+response.getMediaType()
	    		+"\nResult:"+response.getStatusInfo()
			    + "\nHTTP Status:" + response.getStatus()+"\n"
		    + " Person First Name on list= " + person.getFirstname()+"\n");
	}

	if (description.equalsIgnoreCase("measureTypes")) {
	    @SuppressWarnings("unchecked")
	    MeasureDefinitionTypeList mdtObj = (MeasureDefinitionTypeList) object;
	    measure_types = mdtObj.getMeasureType();

	    System.out.println("Request #9:" + path +"Accept:"+response.getMediaType() +"Content-Type:"+response.getMediaType()
	    		+"\nResult:"+response.getStatusInfo()
			    + "\nHTTP Status:" + response.getStatus()+"\n"
	    		+ "     Number of MeasurTypes ="
		    + mdtObj.getMeasureType().size()+"\n" );
	}

	if (description.equalsIgnoreCase("measureforperson")) {

	    @SuppressWarnings("unchecked")
	    HealthMeasureHistoryList measureHistory = (HealthMeasureHistoryList) object;

	    // setting measureType and mid
	    storedmeasureType = measure_types.get(0);
	    if (!measureHistory.getHealthMeasureHistoryLst().isEmpty()) {
		mid = measureHistory.getHealthMeasureHistoryLst().get(0)
			.getIdMeasureHistory();
	    }

	    System.out.println(" Accept:"+response.getMediaType() +" Content-Type:"+response.getMediaType()+
	    		" Result=" + response.getStatusInfo()+
			    "\nHTTP Status:" + response.getStatus()+"\n"+
	    		path + "  Number of measures= "+ measureHistory.getHealthMeasureHistoryLst().size());
	    
	}

	if (description.equalsIgnoreCase("measureforpersonandmid")) {
	    @SuppressWarnings("unchecked")
	    HealthMeasureHistoryList measureHistory = (HealthMeasureHistoryList) object;
	    System.out.println("Request #7:" + path +"Accept:"+response.getMediaType() +"Content-Type:"+response.getMediaType()
	    		+"\nResult:"+response.getStatusInfo()
			    + "\nHTTP Status:" + response.getStatus()+"\n");
	}

	if (description.equalsIgnoreCase("measureforpersonandmidfaterupdate")) {
	    @SuppressWarnings("unchecked")
	    HealthMeasureHistoryList measureHistory = (HealthMeasureHistoryList) object;
	    System.out.println( path +" Accept:"+response.getMediaType() +" Content-Type:"+response.getMediaType()
	    		+"\nResult:"+response.getStatusInfo()
			    + "\nHTTP Status:" + response.getStatus()+"\n"+"\nValue after update: "
		    + measureHistory.getHealthMeasureHistoryLst().get(0)
			    .getValue() +"\n");
	}
	
	/*if (description.equalsIgnoreCase("measureforpersonusingdateparameters")) {
	    @SuppressWarnings("unchecked")
	    HealthMeasureHistoryList measureHistory = (HealthMeasureHistoryList) object;
	    System.out.println("Request#11 baseUrl=" + path +"  Number of measures= "+measureHistory.getHealthMeasureHistoryLst().size() +"   Response="
		    + response.getStatus());
	}*/

    }

    public static Response getserviceResponse(String path1) {
	// path = path1;
	Response r = service.path(path1).request()
		.accept(MediaType.APPLICATION_XML).method("GET");
	return r;
	
    }

    public static void postserviceResponse(String path1, String payload,
	    String description) {

	// accept(MediaType.APPLICATION_JSON).post(Entity.entity(payload,
	// MediaType.APPLICATION_JSON), Response.class);
	if (description.equalsIgnoreCase("xmlpayLoad")) {
	    Response r = service
		    .path(path1)
		    .request()
		    .accept(MediaType.APPLICATION_XML)
		    .post(Entity.entity(payload, MediaType.APPLICATION_XML),
			    Response.class);
	    Person person = r.readEntity(Person.class);
	    createdperonid = person.getIdPerson();
	    System.out.println("Request #4: POST /"+path1 +"Accept:"+r.getMediaType() +"Content-Type:"+r.getMediaType()
	    		+"\nResult:"+r.getStatusInfo()
			    + "\nHTTP Status:"+r.getStatus()+"\n  id="+ person.getIdPerson() + personxmlpayload+"\n");
	}

	if (description.equalsIgnoreCase("measurexmlpayLoad")) {
	    Response r = service
		    .path(path1)
		    .request()
		    .accept(MediaType.APPLICATION_XML)
		    .post(Entity.entity(payload, MediaType.APPLICATION_XML),
			    Response.class);
	    HealthMeasureHistoryList measureHistory = r
		    .readEntity(HealthMeasureHistoryList.class);
	    measureCount = measureHistory.getHealthMeasureHistoryLst().size();
	    createdmid = measureHistory.getHealthMeasureHistoryLst()
		    .get(measureCount - 1).getIdMeasureHistory();
	    System.out.println("Request #8: POST /"+path1 +" Accept:"+r.getMediaType() +" Content-Type:"+r.getMediaType()
	    	+"\nResult:"+r.getStatusInfo()
		    + "\nHTTP Status:" + r.getStatus()+"\n"+ measurexmlpayload+"\n");
	}

	if (description.equalsIgnoreCase("jsonlpayLoad")) {
	    System.out.println("json payload");
	    Response r = service
		    .path(path1)
		    .request()
		    .accept(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(payload, MediaType.APPLICATION_JSON),
			    Response.class);
	    System.out.println("reaching here");
	    // Person person = r.readEntity(Person.class);
	    System.out.println(r.readEntity(String.class));
	}
    }

    public static void updateServiceResponse(String path1, String payload,
	    String description) {
	Response r = service
		.path(path1)
		.request()
		.accept(MediaType.APPLICATION_XML)
		.put(Entity.entity(payload, MediaType.APPLICATION_XML),
			Response.class);
	System.out.println("Request #10: PUT /"+path1 +"Accept:"+r.getMediaType() +"Content-Type:"+r.getMediaType()
			+"\nResult:"+r.getStatusInfo()
		    + "\nHTTP Status:" + r.getStatus()+"\n"+ measurexmlpayloadtoupdate);

    }
}
