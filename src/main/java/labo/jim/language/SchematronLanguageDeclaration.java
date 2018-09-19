package labo.jim.language;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.sonar.api.Plugin.Context;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.server.rule.RulesDefinition;

import labo.jim.exception.ProcessingException;
import labo.jim.schematron.PendingRule;
import labo.jim.schematron.SchematronBasedQualityProfile;
import labo.jim.schematron.SchematronBasedRulesDefinition;
import labo.jim.schematron.SchematronReader;

public class SchematronLanguageDeclaration {
	
	
	private String name;
	private String key;
	private List<String> fileSuffixes = new ArrayList<>();;
	private String qualityProfileName;
	private String ruleRepositoryName;
	
	private List<SchematronReader> schematrons = new ArrayList<>();
	private List<PendingRule> pendingRules = new ArrayList<>(50);
		

	
	public void declare(Context pluginContext) throws ProcessingException{
		checkState();
		SchematronBasedLanguage language = new SchematronBasedLanguage(key, name, fileSuffixes);
		
		for (SchematronReader reader : this.schematrons) {
			reader.load();
			this.pendingRules.addAll(reader.getPendingRules());
		}
		
		RulesDefinition rulesDefinition = new SchematronBasedRulesDefinition(pendingRules, ruleRepositoryName, key);
		BuiltInQualityProfilesDefinition qualityProfileDefinition = new SchematronBasedQualityProfile(key, qualityProfileName, ruleRepositoryName, pendingRules);
		
		pluginContext.addExtension(language);
		pluginContext.addExtension(language.getProperties());
		pluginContext.addExtension(rulesDefinition);
		pluginContext.addExtension(qualityProfileDefinition);
	}
	
	

	public SchematronLanguageDeclaration name(String name){
		this.name = name;
		return this;
	}
	
	public SchematronLanguageDeclaration key(String key){
		this.key = key;
		return this;
	}
	
	public SchematronLanguageDeclaration addFileSuffix(String suffix){
		this.fileSuffixes.add(suffix);
		return this;
	}
	
	public SchematronLanguageDeclaration addSchematronResource(String resourceName){
		URL url = SchematronLanguageDeclaration.class.getClassLoader().getResource(resourceName);
		try {
			return addSchematron(new File(url.toURI()));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
	public SchematronLanguageDeclaration addSchematron(Source schematron){
		this.schematrons.add(new SchematronReader(schematron));
		return this;
	}
	public SchematronLanguageDeclaration addSchematron(File schematron){
		this.schematrons.add(new SchematronReader(schematron));
		return this;
	}
	
	public SchematronLanguageDeclaration qualityProfileName(String qualityProfileName){
		this.qualityProfileName = qualityProfileName;
		return this;
	}
	
	public SchematronLanguageDeclaration ruleRepositoryName(String ruleRepositoryName){
		this.ruleRepositoryName = ruleRepositoryName;
		return this;
	}
	
	private void checkState() {
		if(this.key == null || this.fileSuffixes.isEmpty() || this.schematrons.isEmpty()){
			throw new IllegalStateException("Language key, at least one fileSuffix and at least on Schematron are mandatory.");
		}
		
		if(this.name == null) this.name = this.key;
		if(this.qualityProfileName == null) this.qualityProfileName = this.key;
		if(this.ruleRepositoryName == null) this.ruleRepositoryName = this.key;		
	}
	
	
	
}
