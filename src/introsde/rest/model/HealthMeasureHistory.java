package introsde.rest.model;

import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.model.Person;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//import utility.DatePersistenceConverter;

/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name = "HealthMeasureHistory")
@NamedQuery(name = "HealthMeasureHistory.findAll", query = "SELECT h FROM HealthMeasureHistory h")
@XmlRootElement(name = "measure")
public class HealthMeasureHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sqlite_mhistory")
    @TableGenerator(name = "sqlite_mhistory", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "HealthMeasureHistory")
    @Column(name = "idMeasureHistory")
    private int idMeasureHistory;

    // @Convert(converter = DatePersistenceConverter.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef")
    private MeasureDefinition measureDefinition;

    // notice that we haven't included a reference to the history in Person
    // this means that we don't have to make this attribute XmlTransient
    @ManyToOne
    @JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
    private Person person;

    public HealthMeasureHistory() {
    }

    public int getIdMeasureHistory() {
	return this.idMeasureHistory;
    }

    public void setIdMeasureHistory(int idMeasureHistory) {
	this.idMeasureHistory = idMeasureHistory;
    }

    public Date getTimestamp() {
	return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @XmlTransient
    public MeasureDefinition getMeasureDefinition() {
	return measureDefinition;
    }

    public void setMeasureDefinition(MeasureDefinition param) {
	this.measureDefinition = param;
    }

    @XmlTransient
    public Person getPerson() {
	return person;
    }

    public void setPerson(Person param) {
	this.person = param;
    }

    // database operations
    public static HealthMeasureHistory getHealthMeasureHistoryById(int id) {
	EntityManager em = LifeCoachDao.instance.createEntityManager();
	HealthMeasureHistory p = em.find(HealthMeasureHistory.class, id);
	LifeCoachDao.instance.closeConnections(em);
	return p;
    }

    public static List<HealthMeasureHistory> getAll() {
	EntityManager em = LifeCoachDao.instance.createEntityManager();
	List<HealthMeasureHistory> list = em.createNamedQuery(
		"HealthMeasureHistory.findAll", HealthMeasureHistory.class)
		.getResultList();
	LifeCoachDao.instance.closeConnections(em);
	return list;
    }

    
  //GET persons measure history by personId and measure type 
    public static List<HealthMeasureHistory> getHealthMeasureHistoryByPersonIdandMeasureType(int pid, String mtype) {
	EntityManager em = LifeCoachDao.instance.createEntityManager();
	System.out.println("measuretype=" + mtype + " and pid=" + pid);
	@SuppressWarnings("unchecked")
	List<HealthMeasureHistory> listmeasurehistory = em.createQuery(
		"SELECT q FROM HealthMeasureHistory q where q.person.idPerson="
			+ pid + "").getResultList();
	LifeCoachDao.instance.closeConnections(em);
	return listmeasurehistory;
    }
    
	
//Request #7: GET /person/{id}/{measureType}/{mid} should return the value of {measureType} 
//(e.g. weight) identified by {mid} for person identified by {id}
    public static List<HealthMeasureHistory> getMeasureHistoryByPersonidAndMeasureHistoryidAndMeasureType(int pid, String mtype, int mid) {
	EntityManager em = LifeCoachDao.instance.createEntityManager();
	@SuppressWarnings("unchecked")
	List<HealthMeasureHistory> measurehistorylist = em.createQuery(
		"SELECT mh FROM HealthMeasureHistory mh where mh.person.idPerson="
			+ pid + " and mh.idMeasureHistory='" + mid + "'")
		.getResultList();
	LifeCoachDao.instance.closeConnections(em);
	return measurehistorylist;
    }
    
    public static HealthMeasureHistory saveHealthMeasureHistory(
    	    HealthMeasureHistory p) {
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.persist(p);
    	tx.commit();
    	LifeCoachDao.instance.closeConnections(em);
    	return p;
        }

        public static HealthMeasureHistory updateHealthMeasureHistory(
    	    HealthMeasureHistory p) {
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	p = em.merge(p);
    	tx.commit();
    	LifeCoachDao.instance.closeConnections(em);
    	return p;
        }

        public static void removeHealthMeasureHistory(HealthMeasureHistory p) {
    	EntityManager em = LifeCoachDao.instance.createEntityManager();
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	p = em.merge(p);
    	em.remove(p);
    	tx.commit();
    	LifeCoachDao.instance.closeConnections(em);
        }
    

}

