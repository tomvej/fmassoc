<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/">
		<xsl:text>digraph model {
		node [shape="box",style="rounded"]
		</xsl:text>
		<xsl:apply-templates select="datamodel/table" />
		<xsl:text>}
		</xsl:text>
	</xsl:template>
	<xsl:template match="table">
		<xsl:value-of select="@name" />
		<xsl:text> [label="</xsl:text>
		<xsl:value-of select="@name" />
		<xsl:apply-templates select="property" />
		<xsl:text>"]
		</xsl:text>
		<xsl:apply-templates select="association" />
	</xsl:template>
	<xsl:template match="association">
		<xsl:value-of select="parent::table/@name" />
		<xsl:text> -> </xsl:text>
		<xsl:value-of select="@table" />
		<xsl:text>[label="</xsl:text>
		<xsl:value-of select="@name" />
		<xsl:text>"]
		</xsl:text>
	</xsl:template>
	<xsl:template match="property">
		<xsl:text>\n</xsl:text>
		<xsl:value-of select="@name" />
	</xsl:template>
</xsl:transform> 