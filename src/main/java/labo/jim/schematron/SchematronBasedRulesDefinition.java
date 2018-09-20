package labo.jim.schematron;

import java.util.List;

import org.sonar.api.server.rule.RulesDefinition;

import labo.jim.exception.ProcessingException;

public class SchematronBasedRulesDefinition implements RulesDefinition{
	
	private List<PendingRule> pendingRules;
	private String repoKey;
	private String LanguageKey;

	public SchematronBasedRulesDefinition(List<PendingRule> pendingRules, String repoKey, String languageKey) {
		super();
		this.pendingRules = pendingRules;
		this.repoKey = repoKey;
		LanguageKey = languageKey;
	}

	@Override
	public void define(Context context) {
		NewRepository repo = context.createRepository(repoKey, LanguageKey);
		for (PendingRule pendingRule : pendingRules) {
			repo.createRule(pendingRule.getKey()).setName(pendingRule.getName())
			.setMarkdownDescription(pendingRule.getName());
			
			// TODO Severity
			// TODO type
			// TODO Description, remediation machinChose, etc.
		}
		repo.done();
		
	}

}
