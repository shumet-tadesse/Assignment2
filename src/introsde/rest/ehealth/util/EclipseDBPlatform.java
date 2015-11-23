package introsde.rest.ehealth.util;

import org.eclipse.persistence.platform.database.DatabasePlatform;

@SuppressWarnings("serial")
public class EclipseDBPlatform extends DatabasePlatform  {
	public boolean supportsForeignKeyConstraints() {
        return true;
}
}
