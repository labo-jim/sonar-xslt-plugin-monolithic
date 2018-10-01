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
import labo.jim.helpers.ResourceHelper;
import labo.jim.helpers.SaxonHolder;
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
	
	private boolean loaded = false;
	
	private Source schematron;
	
	private Source schematronXSLT;
	private List<PendingRule> pendingRules;

	private static XPathCompiler localXpathCompiler;
	
	static {
		localXpathCompiler = SaxonHolder.getInstance().getProcessor().newXPathCompiler();
		localXpathCompiler.declareNamespace("", SCHEMATRON_NS);
	}
	
	public SchematronReader(Source schematron){
		super();
		this.schematron = schematron;
		this.pendingRules = new ArrayList<>();
	}

	public SchematronReader(File schematron) {
		this(new StreamSource(schematron));
	}
	
	// ===
	
	public Source getSchematronXSLT() {
		return schematronXSLT;
	}



	public List<PendingRule> getPendingRules() {
		return pendingRules;
	}
	
	// ====


	public void load() throws ProcessingException{
		SaxonHolder sh = SaxonHolder.getInstance();
		try {
			// TODO keep compiled step XSLTs
			XdmNode step1 = sh.runXslt(schematron, stepAsSource(SchematronStep.STEP1));
			XdmNode step2 = sh.runXslt(step1.asSource(), stepAsSource(SchematronStep.STEP2));
			
			// in Step2 <extends> are resolved
			processAssertsReports(step2);
			
			XdmNode step3 = sh.runXslt(step2.asSource(), stepAsSource(SchematronStep.STEP3));
			this.schematronXSLT = step3.asSource();
			
			this.loaded = true;
			
		} catch (SaxonApiException  e) {
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
		
		PendingRule assertReport = new PendingRule();
		 XdmValue id = localXpathCompiler.evaluate(ID_ATTR, item);
		 XdmValue textContent = localXpathCompiler.evaluate(TEXT_CONTENT, item);
		 
		 assertReport.setKey(((XdmAtomicValue) id).getStringValue());
		 assertReport.setName(((XdmAtomicValue) textContent).getStringValue());
		
		 this.pendingRules.add(assertReport);
	}

	private Source stepAsSource(SchematronStep step)  {
		String src = RSRC_PREFIX + step.getStepFile();
		return ResourceHelper.resource(SchematronReader.class, src);
	}

	public boolean isLoaded() {
		return loaded;
	}


	

}
