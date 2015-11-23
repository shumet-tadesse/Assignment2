package introsde.rest.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class HealthProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	private String height="";
	private String weight="";
	
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String wieght) {
		this.weight = wieght;
	}
}