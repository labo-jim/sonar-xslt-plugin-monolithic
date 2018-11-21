package labo.jim.sonarPlugin;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Loggers;

import labo.jim.exception.SchematronProcessingException;
import labo.jim.schematron.SchematronLanguageDeclaration;

public class SonarXSLTPlugin implements Plugin {

	@Override
	public void define(Context context) {

		try {
			SchematronLanguageDeclaration declaration = xslLanguageDeclaration();

			declaration.declare(context);

		} catch (SchematronProcessingException e) {
			Loggers.get(SonarXSLTPlugin.class).error("Sacrebleu !", e);
		}

	}

	public SchematronLanguageDeclaration xslLanguageDeclaration() throws SchematronProcessingException {
		return new SchematronLanguageDeclaration()
				
				.name("Xslt")

				// cette chaine est l'ID du langage.
				// Doit être unique dans le Sonar
				.key("xslt")

				.addFileSuffix(".xsl");

	}

}
