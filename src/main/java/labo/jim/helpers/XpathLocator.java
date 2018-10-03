package labo.jim.helpers;

import javax.xml.transform.Source;
import labo.jim.exception.ProcessingException;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;

public class XpathLocator {
	
	private DocumentBuilder docBuilder;
	private XPathCompiler xpathCompilo;
	private XdmNode builtDocument;


	public XpathLocator(Source xmlDocument) throws ProcessingException {
		Processor proc = SaxonHolder.getInstance().getProcessor();
		this.xpathCompilo = proc.newXPathCompiler();
		this.docBuilder = proc.newDocumentBuilder();
		this.docBuilder.setLineNumbering(true);
		try {
			builtDocument = this.docBuilder.build(xmlDocument);
		} catch (SaxonApiException e) {
			throw new ProcessingException(e);
		}
	}
	
	public XpathLocator(XdmNode xmlDocument) {
		Processor proc = SaxonHolder.getInstance().getProcessor();
		this.xpathCompilo = proc.newXPathCompiler();
		this.builtDocument = xmlDocument;	
	}
	
	
	public int locateSingle(String xpath) throws ProcessingException{
		try {
			XdmItem occurence = this.xpathCompilo.evaluateSingle(xpath, builtDocument);
			return ((XdmNode) occurence).getLineNumber();
		} catch (SaxonApiException | ClassCastException e) {
			throw new ProcessingException(e);
		}
	}
	
	
}
