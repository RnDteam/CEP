<%
// Licensed Materials - Property of IBM
// 5725-B69 5655-Y17 5655-Y31 5724-X98 5724-Y15 5655-V82
// Copyright IBM Corp. 1987, 2015. All Rights Reserved.
//
// Note to U.S. Government Users Restricted Rights: 
// Use, duplication or disclosure restricted by GSA ADP Schedule 
// Contract with IBM Corp.
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Send event</title>
<LINK REL="STYLESHEET"
      HREF="./css/styles.css"
      TYPE="text/css">
</head>
<body>
<table width="100%" height="95" cellspacing="0" cellpadding="0"
	background="images/bg_header.jpg" class="tab_group">
	<tbody>
		<tr>
		  <td nowrap="nowrap" background="images/bg_header.jpg">
			<table class="tab_group" cellspacing="0" cellpadding="0">
				<tr>
			<td valign="middle" class="site-title" nowrap="nowrap" background="images/title_left.png"><font color="white"> Connectivity Sample <br /> </font>
			</td>
			<td><img src="images/title_right.jpg" /></td>
			<td align="right" width="100%"><img src="images/IBM_1px_white.gif" /></td>
				</tr>
			</table>
		    </td>

		   <td nowrap="nowrap" valign="middle" align="right"></td>
		</tr>
		<tr>
			<td colspan="3" class="site-desc" align="left" height="25" valign="middle">Give the transaction parameters and choose the protocol before sending the event. &nbsp;</td>
		</tr>
	</tbody>
</table>
<CENTER>
	<form id="eventSent" action="/eventSender/response" method="get" enctype="text/plain">
	<table>
	  <tr>
	    <td>Account : </td> 
	    <td>
	      <select id="accountId" name="accountId" form="eventSent">
	      	<option value="accountId1">accountId1</option>
	      	<option value="accountId2">accountId2</option>
	      	<option value="accountId3">accountId3</option> 
	      </select>
	  </tr>
	  <tr>
	    <td>Country : </td>
	    <td><input id="country" name="country" type="text" value="FR"></td>
	  </tr>
	  <tr>
	    <td>Amount : </td>
	    <td><input id="amount" name="amount" type="text" value="10000"></td>
	  </tr>
	  <tr>
	    <td>Merchant ID : </td>
	    <td><input id="merchantId" name="merchantId" type="text" value="Super K"></td>
	  </tr>
	  <tr>
	    <td>Merchant type : </td> 
	    <td>
	      <select id="merchantType" name="merchantType" form="eventSent">
	      	<option value="Store">Store</option>
	      	<option value="Online">Online</option>
	      </select>
	  </tr>
	  
	  <tr>
	    <td>Merchant Location : </td>
	    <td><input id="merchantLocation" name="merchantLocation" type="text" value="Valbonne"></td>
	  </tr>
	  
	  <tr>
	    <td> </td>
	    <td><input type="radio" name="event-method" value="jms" checked="checked">JMS<br>
	    <input type="radio" name="event-method" value="http">HTTP</td>
	  </tr>
	  <p>	  
	  <tr>
	  	<td></td>
	    <td>
		  <input type="submit" value="Send event" styleClass="validation">
	    </td>
	    <td></td>
	  </tr>
	  </p>
	  </table>
	</form>
	</CENTER>
</body>

</html>