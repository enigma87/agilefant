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
  <!-- calculate total sotry points first -->
  <c:set var="totalPoints" value="${0}"/>
<c:forEach var="pType" items="${portfolioPoints}">
    <c:set var="totalPoints" value="${totalPoints + pType.value}"/>
</c:forEach>
  
  <table>
    <tr>
      <td>
      <table>
     	 <c:forEach var="entry" items="${portfolioPoints}">
  			<tr><td><c:out value="${entry.key}"/></td>
  				<td>
  					<div style="width: 50px; height: 1em;"  class="portfolioStateNOT_STARTED">
      				<div class="portfolioStateDONE" style='display: inline-block; width: <fmt:formatNumber type="number" maxFractionDigits="2" value="${(entry.value div totalPoints) * 100}" />%; height: 1em;'></div>
      				</div>
      			</td>
  				<td><fmt:formatNumber type="number" maxFractionDigits="1" value="${(entry.value div totalPoints) * 100}" />%</td>
  			</tr>
		</c:forEach>
      	
      </table>
      </td>
      
      <td style="padding-left: 1em; vertical-align: middle;">
        <div class="portfolioPie" style="margin: 0; background-image: ;">&nbsp;</div>
          <div style="width: 100px; height: 1em; margin-top: 0.5em;" class="storyStateNOT_STARTED">
          
           <!--  PORTFOLIO HEALTH? -->
            <div class="storyStateDONE" style="display: inline-block; float: right; width: 90%; height: 1em;">
          
          </div>
        </div>
        <div style="text-align: center;">
          10 days to release
        </div>
      </td>
    </tr>
  </table>

</struct:widget>
</c:when>
<c:otherwise>
  <div>Missing portfolio metric - You do not have access rights to the backlog</div>
</c:otherwise>
</c:choose>