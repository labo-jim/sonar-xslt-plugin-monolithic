package org.sonarxsl.test;

import org.junit.Test;
import org.sonarxsl.sonarPlugin.SonarXSLTPlugin;

public class TestLanguageDeclaration {
	
	
	@Test
	public void DeclareToFakeContext() {
		SonarXSLTPlugin testPlugin = new SonarXSLTPlugin();
		FakeSonarPluginContext pluginContext = new FakeSonarPluginContext();
		testPlugin.define(pluginContext);
		
		// the lines above launch almost all the code.
		// so far, this test case consists to have no exception here.

	}

}
