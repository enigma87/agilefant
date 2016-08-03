<%@include file="/WEB-INF/jsp/inc/_taglibs.jsp"%>
<c:choose>
<c:when test="${access}">
<struct:widget name="Backlog: ${backlog.name}"
  widgetId="${widgetId}">
  


  <%-- Breadcrumb --%>
  <div class="widget-top-info">
 
 <c:choose>
    <c:when test="${empty backlog.parent.parent.id}">
    </c:when>
    <c:otherwise>
    <a href="editBacklog.action?backlogId=${backlog.parent.parent.id}">
    <c:out value="${backlog.parent.parent.name}" /> </a> &gt;
    </c:otherwise>
</c:choose>
 
 <!--   <a href="editBacklog.action?backlogId=${backlog.parent.parent.id}">
    <c:out value="${backlog.parent.parent.name}" /> </a> &gt; -->
  <a href="editBacklog.action?backlogId=${backlog.parent.id}"> 
    <c:out  value="${backlog.parent.name}" /> </a> &gt; 
  <a href="editBacklog.action?backlogId=${backlog.id}"> 
    <c:out  value="${backlog.name}" /></a></div>

  <%-- Metrics --%>
  <!-- calculate total story points first -->
  <c:set var="totalPoints" value="${0}"/>
<c:forEach var="pType" items="${productfeatureMetrics}">
    <c:set var="totalPoints" value="${totalPoints + pType.value}"/>
</c:forEach>
  
  <table>
    <tr>
      <td>
      <table style="width: 200px;height: 150px;">
     	 <c:forEach var="entry" items="${productfeatureMetrics}">
  			<tr><td><c:out value="${entry.key}"/></td>
  				<td>
  					<div style="width: 50px; height: 1em;"  class="productfeatureStateNOT_STARTED">
      				<div class="productfeatureStateDONE" style='display: inline-block; width: <fmt:formatNumber type="number" maxFractionDigits="2" value="${(entry.value div totalPoints) * 100}" />%; height: 1em;'></div>
      				</div>
      			</td>
  				<td><fmt:formatNumber type="number" maxFractionDigits="1" value="${(entry.value div totalPoints) * 100}" />%</td>
  			</tr>
		</c:forEach>
      	
      </table>
      </td>
      
      <td style="padding-left: 1em; vertical-align: middle;">
        <div class="productfeaturePie" style="margin: 0; background-image: ;">&nbsp;</div>
          
      </td>
    </tr>
  </table>

</struct:widget>
</c:when>
<c:otherwise>
  <div>Missing product-feature metric - You do not have access rights to the backlog</div>
</c:otherwise>
</c:choose>