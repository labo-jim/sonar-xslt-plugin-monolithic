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
		Processor proc = new Processor(false);
		this.docBuilder = proc.newDocumentBuilder();
		this.docBuilder.setLineNumbering(true);
		this.xpathCompilo = proc.newXPathCompiler();
		try {
			builtDocument = this.docBuilder.build(xmlDocument);
		} catch (SaxonApiException e) {
			throw new ProcessingException(e);
		}
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
