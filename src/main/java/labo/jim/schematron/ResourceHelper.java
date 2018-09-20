package labo.jim.schematron;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class ResourceHelper {
	
	public static Source resource(Class classe, String resource){
		InputStream stream = classe.getClassLoader().getResourceAsStream(resource);
		Source source = new StreamSource(stream);
		source.setSystemId(classe.getClassLoader().getResource(resource).toString());
		return source;
	}

}
