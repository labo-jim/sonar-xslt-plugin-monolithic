package labo.jim.schematron;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;




public class SchematronSensor implements Sensor{
	
	private String language;
	
	

	public SchematronSensor(String language) {
		super();
		this.language = language;
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
//		descriptor.name("Sensor to check if there is untyped variables");
//		
//		// optimisation to disable execution of sensor if project does
//	    // not contain Java files or if the example rule is not activated
//	    // in the Quality profile
//		descriptor.onlyOnLanguage(XslLanguage.KEY);
//		descriptor.createIssuesForRuleRepository(XslRules.REPOSITORY);
		
	}

	@Override
	public void execute(SensorContext context) {
		FileSystem fs = context.fileSystem();
	    Iterable<InputFile> xslFiles = fs.inputFiles(fs.predicates().hasLanguage(this.language));
	    for (InputFile inputFile : xslFiles) {
	    	
		}
	}

}
