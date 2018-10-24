package labo.jim.schematron;

import labo.jim.helpers.ResourceHelper;
import labo.jim.helpers.SaxonHolder;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltTransformer;

public class SimpleHtml {
	
	private XsltTransformer simpleHtmlXsl;

	public SimpleHtml() throws SaxonApiException {
		simpleHtmlXsl = SaxonHolder.getInstance().compile(ResourceHelper.resource(getClass(), "simple-html.xsl"));
	}
	
	public String simpleHtml(XdmNode description) throws SaxonApiException{
		XdmNode html = SaxonHolder.getInstance().runXslt(description.asSource(), this.simpleHtmlXsl);
		return html.toString();
	}

}
