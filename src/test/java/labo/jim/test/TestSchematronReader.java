package labo.jim.test;

import static org.junit.Assert.*;

import org.junit.Test;

import labo.jim.helpers.ResourceHelper;
import labo.jim.schematron.SchematronLanguageDeclaration;
import labo.jim.schematron.SchematronReader;

public class TestSchematronReader {
	
	public static final String PETIT_SCHEMATRON = "petit-schematron.sch";
	public static final String SCHEMATRON_XSL_QUALITY = "xsl-quality/checkXSLTstyle.sch";
	
	@Test
	public void monPetitSchematron() {
		try {
			
			SchematronReader reader = new SchematronReader(ResourceHelper.resource(getClass(), PETIT_SCHEMATRON));
			reader.load();
			
			assertTrue(reader.getPendingRules().size() == 3);
			assertEquals("Variables Should be Typed",reader.getPendingRules().get(0).getName());
						
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void xsltQualitySchematron() {
		try {
			
			SchematronReader reader = new SchematronReader(ResourceHelper.resource(getClass(), SCHEMATRON_XSL_QUALITY));
			reader.load();
						
			assertTrue(reader.getPendingRules().size() == 32);
			
			System.out.println("JIM "+reader.getPendingRules().get(0).getName());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	

}
