package labo.jim.schematron;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;




public class SchematronSensor implements Sensor{
	
	private SchematronReader reader;
	private String language;
	private String repositoryKey;
	
	private static final Logger LOG = Loggers.get(SchematronSensor.class);
	
	
	

	public SchematronSensor(SchematronReader reader, String language, String repositoryKey) {
		super();
		this.reader = reader;
		this.language = language;
		this.repositoryKey = repositoryKey;
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Schematron-Based sensor");
//		
//		// optimisation to disable execution of sensor if project does
//	    // not contain Java files or if the example rule is not activated
//	    // in the Quality profile
		descriptor.onlyOnLanguage(language);
		descriptor.createIssuesForRuleRepository(repositoryKey);
		
	}

	@Override
	public void execute(SensorContext context) {
		FileSystem fs = context.fileSystem();
	    Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(this.language));
	    for (InputFile file : files) {
	    	try {
	    		
				XdmNode report = SaxonHolder.getInstance().runXslt(inputFileSource(file), reader.getSchematronXSLT());
				processReport(report,context);
				
			} catch (SaxonApiException | IOException e) {
				LOG.error("An error occurend",e); // TODO gérer ça
			}
		}
	}

	private void processReport(XdmNode report, SensorContext context) {
		// TODO Auto-generated method stub
		
	}

	private Source inputFileSource(InputFile file) throws IOException {
		InputStream stream = file.inputStream();
		Source source = new StreamSource(stream);
		source.setSystemId(file.uri().toString());
		return source;	
	}

}
