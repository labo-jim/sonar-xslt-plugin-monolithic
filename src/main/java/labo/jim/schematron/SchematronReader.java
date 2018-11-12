package labo.jim.schematron;

import java.io.File;
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
	public static final String SONAR_SCHEMATRON_NS = "http://www.jimetevenard.com/ns/sonar-xslt";
	public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	
	private static final String ASSERT_REPORT = "//assert|//report";
	private static final String ID_ATTRIBUTE_VALUE = "string(@id)";
	
	private boolean loaded = false;
	
	private Source schematron;
	
	private Source schematronXSLT;
	private List<PendingRule> pendingRules;
	private SimpleHtml simpleHtml;

	private static XPathCompiler localXpathCompiler;
	
	
	static {
		localXpathCompiler = SaxonHolder.getInstance().getProcessor().newXPathCompiler();
		localXpathCompiler.declareNamespace("", SCHEMATRON_NS);
		localXpathCompiler.declareNamespace("sonar", SONAR_SCHEMATRON_NS);
		localXpathCompiler.declareNamespace("html", XHTML_NS);
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
		if(this.loaded){
			throw new IllegalStateException("Already loaded.");
		}
		
		SaxonHolder sh = SaxonHolder.getInstance();
		try {
			if(this.simpleHtml == null) this.simpleHtml = new SimpleHtml();
			 
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
	
	private void processAssertsReports(XdmNode resolvedSchematron) throws SaxonApiException {
		
		XPathSelector sel = localXpathCompiler.compile(ASSERT_REPORT).load();
		
		sel.setContextItem(resolvedSchematron);
		
		Iterator<XdmItem> iterator = sel.iterator();
		while (iterator.hasNext()) {
			XdmItem item = iterator.next();
			
			processSingleAssertReport(item, resolvedSchematron);
		}
		
	}

	private void processSingleAssertReport(XdmItem item, XdmNode resolvedSchematron) throws SaxonApiException {
		// TODO refacto de tout ça !
		
		
		 PendingRule pendingRule = new PendingRule();
		 XdmValue idXdm = localXpathCompiler.evaluate(ID_ATTRIBUTE_VALUE, item);
		 String id = ((XdmAtomicValue) idXdm).getStringValue();
		 
		 XdmValue nameXdm = localXpathCompiler.evaluate(relatedNameXPath(id), resolvedSchematron);
		 String name = ((XdmAtomicValue) nameXdm).getStringValue();
		 
		 XdmValue description = localXpathCompiler.evaluate(relatedDescriptionXPath(id), resolvedSchematron);
		 
		 pendingRule.setKey(id);
		 pendingRule.setName(name.isEmpty() ? id : name);
		 
		 // La description est facultative.
		 if(description instanceof XdmNode){
			 pendingRule.setDescription(this.simpleHtml.simpleHtml((XdmNode) description));
		 } else {
			 pendingRule.setDescription(name);
		 }
		
		 this.pendingRules.add(pendingRule);
	}
	
	
	
	private String relatedNameXPath(String id){
		return "normalize-space(//sonar:name[@rel = '" + id + "'])";
	}
	private String relatedDescriptionXPath(String id){
		return "//sonar:description[@rel = '" + id + "']";
	}

	private Source stepAsSource(SchematronStep step)  {
		String src = RSRC_PREFIX + step.getStepFile();
		return ResourceHelper.resource(SchematronReader.class, src);
	}
	
	

	public boolean isLoaded() {
		return loaded;
	}


	

}
