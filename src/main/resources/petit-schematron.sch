<?xml version="1.0" encoding="UTF-8"?>
<sch:schema
    xmlns:sch="http://purl.oclc.org/dsdl/schematron"
    queryBinding="xslt2"
    xmlns:sonar="http://www.jimetevenard.com/ns/sonar-xslt">
    
    <sch:ns uri="http://www.w3.org/1999/XSL/Transform" prefix="xsl"/>
    
    <sch:pattern>
        <sch:rule context="xsl:variable">
            <sch:assert test="@as" id="typed-variables" sonar:type="TODO" sonar:severity="TODO" >
                Variables Should be Typed
            </sch:assert>
            <!-- more asserts/reports... -->
        </sch:rule>
        <sch:rule context="xsl:stylesheet/xsl:variable | xsl:stylesheet/xsl:param">
            <sch:assert test="@select | element()" id="global-var-param-default-value" sonar:type="TODO" sonar:severity="TODO">
                Global params/variables should have a default value
            </sch:assert>
            <sch:report test="namespace-uri(@name) = ''" id="global-var-param-namespace" sonar:type="TODO" sonar:severity="TODO">
                Global params/variables should have a namespace to avoid confusion
            </sch:report>
        </sch:rule>
    </sch:pattern>
</sch:schema>