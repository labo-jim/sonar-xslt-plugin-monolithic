package labo.jim.schematron;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import labo.jim.exception.ProcessingException;
import labo.jim.helpers.SaxonHolder;
import labo.jim.helpers.XpathLocator;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;




public class SchematronSensor implements Sensor{
	
	private static final String SVRL_NS = "http://purl.oclc.org/dsdl/svrl";
	private static final String ASSERT_REPORT = "//failed-assert | //successful-report";
	private static final String ID_ATTR_STRING_VALUE = "string(@id)";
	private static final String LOCATION_ATTR_STRING_VALUE = "string(@location)";
	private static final String TEXT_CONTENT = "normalize-space(text)"; // <= the tag called "text", not a joke.
	
	private static XPathExecutable xpathAssertReport;
	private static XPathCompiler localCompiler;
	private static final Logger LOG = Loggers.get(SchematronSensor.class);
	
	private SchematronReader reader;
	private String language;
	private String repositoryKey;
	
	
	static {
		localCompiler = SaxonHolder.getInstance().getProcessor().newXPathCompiler();
		localCompiler.declareNamespace("", SVRL_NS);
		try {
			xpathAssertReport = localCompiler.compile(ASSERT_REPORT);
		} catch (SaxonApiException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	
	

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
	    		
	    		XdmNode inputFileDocument = inputFileDocument(file);
				XdmNode report = SaxonHolder.getInstance().runXslt(inputFileDocument, reader.getSchematronXSLT());
				processReport(file,inputFileDocument,report,context);
				
			} catch (SaxonApiException | IOException | ProcessingException e) {
				LOG.error("An error occurend",e); // TODO gérer ça
			}
		}
	}

	private void processReport(InputFile inputFile,XdmNode inputFileDocument, XdmNode report, SensorContext context) throws SaxonApiException, ProcessingException, IOException {
		List<PendingIssue> pendingIssues = prepareIssues(report);
		
		for (PendingIssue pendingIssue : pendingIssues) {
			NewIssue newIssue = context.newIssue();
			newIssue.forRule(pendingIssue.rule(repositoryKey));
			
			XpathLocator locator = new XpathLocator(inputFileDocument);
			int lineNumber = locator.locateSingle(pendingIssue.getXpathLocation());	
			
			newIssue.at(newIssue.newLocation()
					.on(inputFile)
					.message(pendingIssue.getMessage())
					.at(inputFile.selectLine(lineNumber)));
			
			newIssue.save();
		}
		
	}

	private List<PendingIssue> prepareIssues(XdmNode report) throws SaxonApiException, ProcessingException {
		List<PendingIssue> pendingIssues = new LinkedList<>();
		XPathSelector selector = xpathAssertReport.load();
		selector.setContextItem(report);
		selector.evaluate();
		
		for (XdmItem triggeredItem : selector) {
			
			XdmValue idValue = localCompiler.evaluate(ID_ATTR_STRING_VALUE, triggeredItem);
			XdmValue locationValue = localCompiler.evaluate(LOCATION_ATTR_STRING_VALUE, triggeredItem);
			XdmValue messageValue = localCompiler.evaluate(TEXT_CONTENT, triggeredItem);
			
			pendingIssues.add(PendingIssue.of(idValue , locationValue, messageValue ));
		}
		return pendingIssues;
	}

	private XdmNode inputFileDocument(InputFile file) throws IOException, SaxonApiException {
		InputStream stream = file.inputStream();
		Source source = new StreamSource(stream);
		source.setSystemId(file.uri().toString());
		return SaxonHolder.getInstance().buildDocument(source);	
	}
	
	

}
