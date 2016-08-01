<%@ include file="./inc/_taglibs.jsp"%>
<%@ page language="java" import="java.util.*" %>

<struct:htmlWrapper navi="none">

<struct:testPie />

<c:out value="this is iter : ${pie}" />

<c:forEach var="entry" items="${portfolioPoints}">
<br>
  Key: <c:out value="${entry.key}"/>
  Value: <c:out value="${entry.value}"/>
</c:forEach>

<script> 

jQuery.getJSON(
	      "ajax/testPieData.action",
	      "",
	      function(data,status) {
	    	  debugger
	        if (status !== "success") {
	          return false;
	        }
	    	  console.log(data);
	      });


</script>


</struct:htmlWrapper>
