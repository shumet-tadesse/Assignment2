package introsde.rest.ehealth.util;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name="measureTypes")
public class MeasureDefinitionTypeList {

	private List<String> measureType;

	@XmlElement(name = "measureType")
	public List<String> getMeasureType() {
		return measureType;
	}

	public void setMeasureType(List<String> measureType) {
		this.measureType = measureType;
	}
}
