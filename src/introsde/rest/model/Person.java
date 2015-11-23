package introsde.rest.model;

import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.ehealth.util.DateAdapter;
import introsde.rest.ehealth.util.DatePersistenceConverter;
//import introsde.rest.ehealth.model.Person;
import introsde.rest.model.HealthProfile;
import introsde.rest.model.LifeStatus;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;





import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the "Person" database table.
 * 
 */
@Entity
@Table(name = "Person")
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@XmlRootElement
@XmlType(propOrder = { "firstname", "lastname", "birthdate", "healthProfile",
	"idPerson" })
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    // For sqlite in particular, you need to use the following @GeneratedValue
    // annotation
    // This holds also for the other tables
    // SQLITE implements auto increment ids through named sequences that are
    // stored in a
    // special table named "sqlite_sequence"
    @GeneratedValue(generator = "sqlite_person")
    @TableGenerator(name = "sqlite_person", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
    @Column(name = "idPerson")
    private int idPerson;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "name")
    private String firstname;

    // @Column(name = "username")
    // private String username;

    // @XmlJavaTypeAdapter(DateAdapter.class)
    @Convert(converter = DatePersistenceConverter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "birthdate")
    private Date birthdate;

    // @Column(name = "email")
    // private String email;

    // mappedBy must be equal to the name of the attribute in LifeStatus that
    // maps this relation
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LifeStatus> lifeStatus;

    // @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch =
    // FetchType.EAGER)
    // private List<HealthMeasureHistory> measureHistory;

    private HealthProfile healthProfile;

    public Person() {
    }

    // public String getEmail() {
    // return this.email;
    // }
    //
    // public void setEmail(String email) {
    // this.email = email;
    // }

    public int getIdPerson() {
	return this.idPerson;
    }

    public void setIdPerson(int idPerson) {
	this.idPerson = idPerson;
    }

    public String getFirstname() {
	return this.firstname;
    }

    public void setFirstname(String name) {
	this.firstname = name;
    }

    public String getLastname() {
	return this.lastname;
    }

    public void setLastname(String lastname) {
	this.lastname = lastname;
    }

    public Date getBirthdate() {
	return this.birthdate;
    }

    public void setBirthdate(Date birthdate) {
	this.birthdate = birthdate;
    }

    // public String getUsername() {
    // return this.username;
    // }
    //
    // public void setUsername(String username) {
    // this.username = username;
    // }

    // the XmlElementWrapper defines the name of node in which the list of
    // LifeStatus elements
    // will be inserted
    // @XmlElementWrapper(name = "Measurements")
    // @XmlElement(name = "Measure")
    @XmlTransient
    public List<LifeStatus> getLifeStatus() {
	return lifeStatus;
    }

    public void setLifeStatus(List<LifeStatus> param) {
	this.lifeStatus = param;
    }

    @XmlElement(name = "healthProfile")
    public HealthProfile getHealthProfile() {
	healthProfile = new HealthProfile();
	List<LifeStatus> lifestatuslst = new ArrayList<LifeStatus>();
	String weight = "";
	String height = "";

	lifestatuslst = getLifeStatus();

	if (lifestatuslst != null) {
	    // System.out.println("life status list::::not null"+lifestatuslst.size());
	    for (LifeStatus lifeStatus : lifestatuslst) {
		if (((LifeStatus) lifeStatus).getMeasureDefinition1()
			.getMeasureName().equalsIgnoreCase("weight")) {
		    weight = lifeStatus.getValue();
		    healthProfile.setWeight(weight);
		}

		if (((LifeStatus) lifeStatus).getMeasureDefinition1()
			.getMeasureName().equalsIgnoreCase("height")) {
		    height = lifeStatus.getValue();
		    healthProfile.setHeight(height);
		}
	    }

	}

	return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
	this.healthProfile = healthProfile;
    }

    public HealthProfile getHealthProfileSetterValue() {
	return healthProfile;
    }

    // Database operations
    // Notice that, for this example, we create and destroy and entityManager on
    // each operation.
    // How would you change the DAO to not having to create the entity manager
    // every time?
    public static Person getPersonById(int personId) {
	EntityManager em = LifeCoachDao.instance.createEntityManager();
	Person p = em.find(Person.class, personId);
	LifeCoachDao.instance.closeConnections(em);
	return p;
    }

    public static List<Person> getAll() {

    	System.out.println("--> Initializing Entity manager...");
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
    	System.out.println("--> Querying the database for all the people...");
    	List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
    		.getResultList();
    	System.out.println("--> Closing connections of entity manager...");
    	LifeCoachDao.instance.closeConnections(em);
    	return list;
        }

    public static Person savePerson(Person p) {

	EntityManager em = LifeCoachDao.instance.createEntityManager();
	EntityTransaction tx = em.getTransaction();
	tx.begin();
	em.persist(p);
	tx.commit();
	LifeCoachDao.instance.closeConnections(em);
	System.out.println("After Saving person::::::" + p.getIdPerson());
	savelifeStatus(p);
	return p;
    }

    public static Person updatePerson(Person p) {
	EntityManager em = LifeCoachDao.instance.createEntityManager();
	EntityTransaction tx = em.getTransaction();
	tx.begin();
	p = em.merge(p);
	tx.commit();
	LifeCoachDao.instance.closeConnections(em);
	return p;
    }

    public static void removePerson(Person p) {

	EntityManager em = LifeCoachDao.instance.createEntityManager();
	EntityTransaction tx = em.getTransaction();
	tx.begin();
	p = em.merge(p);
	em.remove(p);
	tx.commit();
	LifeCoachDao.instance.closeConnections(em);
	System.out.println("After Remove:::::: " + p.getIdPerson());

    }

    // save life status for person incase values given
    public static void savelifeStatus(Person p) {
	List<MeasureDefinition> measureDefLst = new ArrayList<MeasureDefinition>();

	measureDefLst = MeasureDefinition.getAll();
	HealthProfile healthProfileObj = p.getHealthProfileSetterValue();

	if (healthProfileObj != null) {
	    System.out.println("lifestatus saved as well!!!!");

	    for (MeasureDefinition obj : measureDefLst) {

		LifeStatus lifeStatusObj = new LifeStatus();

		if (obj.getMeasureName().equalsIgnoreCase("weight")) {
		    System.out.println("executing weight:::: "
			    + healthProfileObj.getWeight());
		    if (healthProfileObj.getWeight() != null) {
			lifeStatusObj.setValue(healthProfileObj.getWeight());
			lifeStatusObj.setMeasureDefinition1(obj);
		    }
		    lifeStatusObj.setPerson(p);
		    LifeStatus.saveLifeStatus(lifeStatusObj);
		}
		if (obj.getMeasureName().equalsIgnoreCase("height")) {
		    System.out.println("executing height:::: "
			    + healthProfileObj.getHeight());
		    if (healthProfileObj.getHeight() != null) {
			lifeStatusObj.setValue(healthProfileObj.getHeight());
			lifeStatusObj.setMeasureDefinition1(obj);
		    }
		    lifeStatusObj.setPerson(p);
		    LifeStatus.saveLifeStatus(lifeStatusObj);
		}

	    }
	}
    }

    public static void removePersonDependencies(Person p) {
	List<LifeStatus> lifeStatuslst = new ArrayList<LifeStatus>();
	lifeStatuslst = LifeStatus.getAll();
	for (LifeStatus obj : lifeStatuslst) {
	    if (obj.getPerson().getIdPerson() == p.getIdPerson()) {
		LifeStatus.removeLifeStatus(obj);
	    }
	}
    }
}
