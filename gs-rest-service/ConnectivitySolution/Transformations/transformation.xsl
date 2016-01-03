<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:event="http://www.ibm.com/ia/xmlns/default/ConnectivitySolutionBOM/model"
	>

  <xsl:output method="xml" />

<!-- Transform the inbound message into a "Transaction" event. -->

 <xsl:template match="event:Transaction">
	<event:Transaction>
		<event:account><xsl:value-of select="account" /></event:account>
		<event:amount><xsl:value-of select="amount" /></event:amount>
		<event:countryCode><xsl:value-of select="countryCode" /></event:countryCode>
		<event:merchant>
		   <id><xsl:value-of select="merchantId" /></id>
		   <type><xsl:value-of select="merchantType" /></type>
		   <location><xsl:value-of select="location" /></location>
		</event:merchant>
	</event:Transaction>
</xsl:template>

</xsl:stylesheet>