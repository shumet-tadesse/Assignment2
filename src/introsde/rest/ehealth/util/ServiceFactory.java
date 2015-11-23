package introsde.rest.ehealth.util;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

public class ServiceFactory {

    public  WebTarget getService(URI baseUrI) {
	ClientConfig clientConfig = new ClientConfig();
	Client client = ClientBuilder.newClient(clientConfig);
	
	return  client.target(baseUrI);
    }
}