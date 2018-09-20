package labo.jim.test;

import org.junit.Test;

import labo.jim.language.SchematronLanguageDeclaration;
import labo.jim.schematron.ResourceHelper;
import labo.jim.schematron.SchematronReader;

public class TestSchematronReader {
	
	public static final String PETIT_SCHEMATRON = "petit-schematron.sch";
	
	@Test
	public void monPetitSchematron() {
		try {
			
			SchematronReader reader = new SchematronReader(ResourceHelper.resource(getClass(), PETIT_SCHEMATRON));
			reader.load();
			
			System.out.println(reader.getPendingRules());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	

}
