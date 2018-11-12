<?xml version="1.0" encoding="UTF-8"?>
<schema 
  xmlns="http://purl.oclc.org/dsdl/schematron" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  xmlns:saxon="http://saxon.sf.net/"
  queryBinding="xslt2" 
  id="xsl-common.sch"
  >
  
  <ns prefix="xsl" uri="http://www.w3.org/1999/XSL/Transform"/>
  <ns prefix="xd" uri="http://www.oxygenxml.com/ns/doc/xsl"/>
  <ns prefix="saxon" uri="http://saxon.sf.net/"/>
  
  <xsl:key name="getElementById" match="*" use="@id"/>
  
  <let name="NCNAME.reg" value="'[\i-[:]][\c-[:]]*'"/>
  <let name="xslt.version" value="/*/@version"/>
  
  <!--====================================-->
  <!--            DIAGNOSTICS             -->
  <!--====================================-->
  
  <diagnostics>
    <diagnostic id="addType">Add @as attribute</diagnostic>
  </diagnostics>
  
  <!--====================================-->
  <!--              PHASE                 -->
  <!--====================================-->
  
  <!--<phase id="test">
    <active pattern="xslt-quality_common"/>
    <active pattern="xslt-quality_documentation"/>
    <active pattern="xslt-quality_typing"/>
    <active pattern="xslt-quality_namespaces"/>
    <active pattern="xslt-quality_writing"/>
	</phase>-->

  <!--====================================-->
  <!--              MAIN                 -->
  <!--====================================-->
  
  <pattern id="xslt-quality_common">
    <rule context="xsl:for-each">
      <report test="ancestor::xsl:template 
        and not(starts-with(@select, '$'))
        and not(starts-with(@select, 'tokenize('))
        and not(starts-with(@select, 'distinct-values('))
        and not(matches(@select, '\d'))" 
        role="warning" id="xslt-quality_avoid-for-each">
        [common] Should you use xsl:apply-template instead of xsl:for-each 
      </report>
    </rule>
    <rule context="xsl:template/@match | xsl:*/@select | xsl:when/@test">
      <report test="contains(., 'document(concat(') or contains(., 'doc(concat(')" id="xslt-quality_use-resolve-uri-instead-of-concat">
        [common] Don't use concat within document() or doc() function, use resolve-uri instead (you may use static-base-uri() or base-uri())
      </report>
    </rule>
  </pattern>
  
  <pattern id="xslt-quality_documentation">
    <rule context="/xsl:stylesheet">
      <assert test="xd:doc[@scope = 'stylesheet']" id="xslt-quality_documentation-stylesheet">
        [documentation] Please add a documentation block for the whole stylesheet : &lt;xd:doc scope="stylesheet">
      </assert>
    </rule>
  </pattern>
  
  <pattern id="xslt-quality_typing">
    <rule context="xsl:variable | xsl:param  | xsl:with-param | xsl:function">
      <assert test="@as" diagnostics="addType" id="xslt-quality_typing-with-as-attribute">
        [typing] <name/> is not typed
      </assert>
    </rule>
  </pattern>
  
  <pattern id="xslt-quality_namespaces">
    <rule context="xsl:template/@name | /*/xsl:variable/@name | /*/xsl:param/@name">
      <assert test="every $name in tokenize(., '\s+') satisfies matches($name, concat('^', $NCNAME.reg, ':'))" role="warning" id="xslt-quality_ns-global-statements-need-prefix">
        [namespaces] <value-of select="local-name(parent::*)"/> <name/>="<value-of select="tokenize(., '\s+')[not(matches(., concat('^', $NCNAME.reg, ':')))]"/>" should be namespaces prefixed, so they don't generate conflict with imported XSLT (or when this xslt is imported)
      </assert>
    </rule>
    <rule context="xsl:template/@mode">
      <assert test="every $name in tokenize(., '\s+') satisfies matches($name, concat('^', $NCNAME.reg, ':'))" role="warning" id="xslt-quality_ns-mode-statements-need-prefix">
        [namespaces] <value-of select="local-name(parent::*)"/> @<name/> value "<value-of select="tokenize(., '\s+')[not(matches(., concat('^', $NCNAME.reg, ':')))]"/>" should be namespaces prefixed, so they don't generate conflict with imported XSLT (or when this xslt is imported)
      </assert>
    </rule>
    <rule context="@match | @select">
      <report test="contains(., '*:')" id="xslt-quality_ns-do-not-use-wildcard-prefix">
        [namespaces] Use a namespace prefix instead of *:
      </report>
    </rule>
  </pattern>
  
  <pattern id="xslt-quality_writing">
    <rule context="xsl:attribute | xsl:namespace | xsl:variable | xsl:param | xsl:with-param">
      <report id="xslt-quality_writing-use-select-attribute-when-possible"
        test="not(@select) and (count(* | text()[normalize-space(.)]) = 1) and (count(xsl:value-of | xsl:sequence | text()[normalize-space(.)]) = 1)">
        [writing] Use @select to assign a value to <name/>
      </report>
    </rule>
  </pattern>
  
  <pattern id="xslt-quality_xslt-3.0">
    <rule context="xsl:import[$xslt.version = '3.0']">
      <report id="xslt-quality_xslt-3.0-import-first" 
        test="following-sibling::xd:doc" role="info">
        [XSLT-3.0] When using XSLT 3.0 xsl:import may come after the xd:doc block
      </report>
    </rule>
  </pattern>
  
</schema>
