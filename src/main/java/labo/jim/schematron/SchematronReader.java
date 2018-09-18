package labo.jim.schematron;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import labo.jim.exception.ProcessingException;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;

public class SchematronReader {
	
private static final String RSRC_PREFIX = "schematron-code/";
	
	public static final String SCHEMATRON_NS = "http://purl.oclc.org/dsdl/schematron";
	private static final String ASSERT_REPORT = "//assert|//report";
	private static final String TEXT_CONTENT = "normalize-space(text())";
	private static final String ID_ATTR = "string(@id)";
	
	
	private File schematron;
	
	private Source schematronXSLT;
	private List<SchematronAssertReport> assertsReports;

	private static XPathCompiler localXpathCompiler;
	
	static {
		localXpathCompiler = SaxonHolder.getInstance().getProcessor().newXPathCompiler();
		localXpathCompiler.declareNamespace("", SCHEMATRON_NS);
	}

	public SchematronReader(File schematron) {
		super();
		this.schematron = schematron;
		this.assertsReports = new ArrayList<>();
		
		
	}
	
	// ===
	
	public Source getSchematronXSLT() {
		return schematronXSLT;
	}



	public List<SchematronAssertReport> getAssertsReports() {
		return assertsReports;
	}
	
	// ====


	public void load() throws ProcessingException{
		SaxonHolder sh = SaxonHolder.getInstance();
		try {
			// TODO keep compiled step XSLTs
			XdmNode step1 = sh.runXslt(new StreamSource(schematron), stepAsSource(SchematronStep.STEP1));
			XdmNode step2 = sh.runXslt(step1.asSource(), stepAsSource(SchematronStep.STEP2));
			
			// in Step2 <extends> are resolved
			processAssertsReports(step2);
			
			XdmNode step3 = sh.runXslt(step2.asSource(), stepAsSource(SchematronStep.STEP3));
			this.schematronXSLT = step3.asSource();
			
			
		} catch (SaxonApiException | URISyntaxException e) {
			throw new ProcessingException(e);
		}
	}
	
	// ===
	
	private void processAssertsReports(XdmNode step2) throws SaxonApiException {
		
		XPathSelector sel = localXpathCompiler.compile(ASSERT_REPORT).load();
		
		sel.setContextItem(step2);
		
		Iterator<XdmItem> iterator = sel.iterator();
		while (iterator.hasNext()) {
			XdmItem item = iterator.next();
			
			processSingleAssertReport(item);
		}
		
	}

	private void processSingleAssertReport(XdmItem item) throws SaxonApiException {
		// TODO refacto de tout ça !
		
		SchematronAssertReport assertReport = new SchematronAssertReport();
		 XdmValue id = localXpathCompiler.evaluate(ID_ATTR, item);
		 XdmValue textContent = localXpathCompiler.evaluate(TEXT_CONTENT, item);
		 
		 assertReport.setId(((XdmAtomicValue) id).getStringValue());
		 assertReport.setTextContent(((XdmAtomicValue) textContent).getStringValue());
		
		 this.assertsReports.add(assertReport);
	}

	private Source stepAsSource(SchematronStep step) throws URISyntaxException {
		// TODO externalize : SchematronCodeProvider...
		URL url = SchematronStep.class.getClassLoader().getResource(RSRC_PREFIX + step.getStepFile());
		return new StreamSource(new File(url.toURI()));
	}
	

}
