package labo.jim.xsltSonarPlugin;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Loggers;

import labo.jim.exception.ProcessingException;
import labo.jim.language.SchematronLanguageDeclaration;

public class SonarXSLTPlugin  implements Plugin{
	
	

	@Override
	public void define(Context context) {
		SchematronLanguageDeclaration declaration = new SchematronLanguageDeclaration()
				.name("Xslt").key("xslt")
				.addFileSuffix(".xsl")
				.addSchematronResource("petit-schematron.sch");
		
		try {
			declaration.declare(context);
		} catch (ProcessingException e) {
			Loggers.get(SonarXSLTPlugin.class).error("Oupsy !", e);
		}
		
	}

}
