package labo.jim.language;

import static java.util.Arrays.asList;

import java.util.List;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.api.resources.Qualifiers;

public class XslLanguage extends AbstractLanguage{
	
	public static final String NAME = "XSLT";
	public static final String KEY = "xslt";
	
	public static final String XSL_NS = "http://www.w3.org/1999/XSL/Transform";
	
	public static final String FILE_SUFFIXES_KEY = "sonar.xslt.file.suffixes";
	public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".xsl";
	


	public XslLanguage() {
		super(KEY, NAME);
	}

	public String[] getFileSuffixes() {
		return new String[] {".xsl",".xslt"};
	}
	
	 public static List<PropertyDefinition> getProperties() {
		    return asList(PropertyDefinition.builder(FILE_SUFFIXES_KEY)
		      .defaultValue(FILE_SUFFIXES_DEFAULT_VALUE)
		      .category("Xsl")
		      .name("File Suffixes")
		      .description("Comma-separated list of suffixes for files to analyze.")
		      .onQualifiers(Qualifiers.PROJECT)
		      .build());
		  }

}
