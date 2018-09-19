package labo.jim.tmp;

import java.io.File;
import java.net.URISyntaxException;

import labo.jim.exception.ProcessingException;
import labo.jim.schematron.SchematronReader;

public class TmpLauchTest {

	public static void main(String[] args) throws URISyntaxException, ProcessingException {
		File petitSch = new File(TmpLauchTest.class.getClassLoader().getResource("petit-schematron.sch").toURI());
		SchematronReader r = new SchematronReader(petitSch);
		
		r.load();
		
		System.out.println(r.getPendingRules());
		

	}

}
