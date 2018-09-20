package labo.jim.schematron;

import org.sonar.api.rule.RuleKey;

import labo.jim.exception.ProcessingException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmValue;

public class PendingIssue {
	
	private String ruleKey;
	private String xpathLocation;
	
	
	
	
	public PendingIssue() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static PendingIssue of(XdmValue idValue, XdmValue locationValue) throws ProcessingException{
		PendingIssue pendingIssue = new PendingIssue();
		try {
			pendingIssue.ruleKey = ((XdmAtomicValue)idValue).getStringValue();
			pendingIssue.xpathLocation = ((XdmAtomicValue)locationValue).getStringValue();
			return pendingIssue;
		} catch (ClassCastException e) {
			throw new ProcessingException(e);
		}
	}
	
	public RuleKey rule(String repositoryKey){
		return RuleKey.of(repositoryKey, getRuleKey());
	}
	public String getRuleKey() {
		return ruleKey;
	}
	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}
	public String getXpathLocation() {
		return xpathLocation;
	}
	public void setXpathLocation(String xpathLocation) {
		this.xpathLocation = xpathLocation;
	}
	
	

}
