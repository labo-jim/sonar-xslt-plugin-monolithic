package labo.jim.sonarPlugin;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Loggers;

import labo.jim.exception.ProcessingException;
import labo.jim.schematron.SchematronLanguageDeclaration;

public class SonarXSLTPlugin  implements Plugin{
	
	

	@Override
	public void define(Context context) {
		
		// Déclaration du langage
		SchematronLanguageDeclaration declaration = 
				
				new SchematronLanguageDeclaration()
				
				// Propriétés du langage
				// =====================
				
				// Nom affiché
				.name("Xslt")
				
				// cette chaine est l'ID du langage.
				// Doit être unique dans le Sonar
				.key("xslt")
							
				.addFileSuffix(".xsl")
				// Cette méthode peut être appelée autant de fois que nécessaire
				
				
				// Schematrons
				// ===========
				
				// TODO à modulariser
				.addSchematronResource("petit-schematron.sch")
				.addSchematronResource("xsl-quality/checkXSLTstyle.sch");
		  		// Cette méthode peut être appelée autant de fois que nécessaire
		
		try {
			
			declaration.declare(context);
		
		} catch (ProcessingException e) {
			Loggers.get(SonarXSLTPlugin.class).error("Sacrebleu !", e);
		}
		
	}

}
