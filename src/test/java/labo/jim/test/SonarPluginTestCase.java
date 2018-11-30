package labo.jim.test;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Loggers;

import labo.jim.exception.SchematronProcessingException;
import labo.jim.schematron.SchematronLanguageDeclaration;

public class SonarPluginTestCase implements Plugin {

	@Override
	public void define(Context context) {

		try {
			SchematronLanguageDeclaration declaration = xslLanguageDeclaration();

			declaration.declare(context);

		} catch (SchematronProcessingException e) {
			Loggers.get(SonarPluginTestCase.class).error("Sacrebleu !", e);
		}

	}

	public SchematronLanguageDeclaration xslLanguageDeclaration() throws SchematronProcessingException {
		return new SchematronLanguageDeclaration()
				
				.addSchematronsFromDependencies()			
				.name("Foo Language")
				.key("foo")
				.addFileSuffix(".foo");

	}

}
