package labo.jim.schematron;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class SaxonHolder {
	
	private static SaxonHolder instance;
	
	private Processor proc;
	private XsltCompiler compiler;
	private XPathCompiler xpathCompiler;
	private DocumentBuilder docBuilder;
	

	


	public static SaxonHolder getInstance(){
		if(instance == null) instance = new SaxonHolder();
		return instance;
	}
	
	private SaxonHolder(){
		proc = new Processor(false);
		docBuilder = proc.newDocumentBuilder();
		compiler = proc.newXsltCompiler();
		xpathCompiler = proc.newXPathCompiler();
	}
	
	public Processor getProcessor() {
		return proc;
	}
	
	// =========================================
	
	


	public XdmNode buildDocument(File file) throws SaxonApiException{
		return docBuilder.build(file);
	}
	
	public XdmNode buildDocument(Source source) throws SaxonApiException{
		return docBuilder.build(source);
	}
	
	// ==============================================
	
	public XdmNode runXslt(Source source, XsltTransformer xsl) throws SaxonApiException{
		xsl.setSource(source);
		XdmDestination destination = new XdmDestination();
		xsl.setDestination(destination);
		xsl.transform();
		
		return destination.getXdmNode();
	}
	
	public XdmNode runXslt(Source source, Source xslt) throws SaxonApiException{	
		return runXslt(source, compile(xslt));
	}
	
	public XsltTransformer compile(Source xslt) throws SaxonApiException{
		return compiler.compile(xslt).load();
	}
	
	// ==================================================
	
	public XPathSelector compileXpath(String xpath) throws SaxonApiException{
		return xpathCompiler.compile(xpath).load();
	}
	
	
	
	
	

}
