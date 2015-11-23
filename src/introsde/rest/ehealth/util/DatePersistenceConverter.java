package introsde.rest.ehealth.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DatePersistenceConverter implements AttributeConverter<java.util.Date, String> {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String convertToDatabaseColumn(Date date) {
		return dateFormat.format(date);
	}

	@Override
	public Date convertToEntityAttribute(String date) {
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}

