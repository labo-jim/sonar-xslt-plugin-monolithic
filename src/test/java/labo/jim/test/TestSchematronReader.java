package labo.jim.test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import labo.jim.exception.ProcessingException;
import labo.jim.schematron.SchematronReader;

public class TestSchematronReader {
	
	@Test
	public void monPetitSchematron() {
		try {
			
			SchematronReader reader = new SchematronReader(fileBasedSchSource());
			reader.load();
			
			System.out.println(reader.getPendingRules());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Source fileBasedSchSource() throws URISyntaxException {
		URL url = TestSchematronReader.class.getClassLoader().getResource("petit-schematron.sch");
		System.out.println(url.toExternalForm());
		return new StreamSource(new File(url.toURI()));
	}

}
