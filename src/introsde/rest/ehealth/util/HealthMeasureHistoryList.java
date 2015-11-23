package introsde.rest.ehealth.util;

import introsde.rest.model.HealthMeasureHistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="measureHistory")
public class HealthMeasureHistoryList implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	private List<HealthMeasureHistory> healthMeasureHistoryLst = new ArrayList<HealthMeasureHistory>();

	@XmlElement(name = "Measure")
	public List<HealthMeasureHistory> getHealthMeasureHistoryLst() {
		return healthMeasureHistoryLst;
	}

	public void setHealthMeasureHistoryLst(
			List<HealthMeasureHistory> healthMeasureHistoryLst) {
		this.healthMeasureHistoryLst = healthMeasureHistoryLst;
	}
}
