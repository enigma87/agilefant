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
        <div id="productfeaturePie" style="margin: 0; background-image: ;">&nbsp;</div>
          
      </td>
    </tr>
  </table>
<script type="text/javascript">
jQuery.getJSON(
	      "ajax/widgets/productfeatureMetricsJSON.action",
	      {
	    	  objectId: "${backlog.id}"
	      },
	      function(data,status) {
	    	  
	        if (status !== "success") {
	          return false;
	        }
	    	  
	    	  /*
	    	  D3 pie chart, CREDIT: http://zeroviscosity.com/d3-js-step-by-step/step-1-a-basic-pie-chart
	    	  
	    	  */
	    	 var data4Pie = [];
	    	 for (portfoliotype in data) {
	    		 data4Pie.push({
	    			 label: portfoliotype,
	    			 count: data[portfoliotype]
	    			}
	    		 ); 
	    	 }
	    	 
	    	 var width = 250;
	    	 var height = 250;
	    	 var radius = Math.min(width, height) / 2;
	    	 var donutWidth = 40;
	    	 var legendRectSize = 18;                                  // NEW
	         var legendSpacing = 4;                                    // NEW

	    	 var color = d3.scaleOrdinal(d3.schemeCategory20b);
	    	 
	    	 var svg = d3.select('#productfeaturePie')
	    	 .append('svg')
	    	 .attr('width', width)
	    	 .attr('height', height)
	    	 .append('g')
	    	 .attr('transform', 'translate('+ width/2 + ',' + height/2 + ')');
	    	 
	    	 var arc = d3.arc()
	    	 .innerRadius(radius - donutWidth)
	    	 .outerRadius(radius);
	    	 
	    	 var pie = d3.pie()
	    	 .value(function(d){ return d.count;})
	    	 .sort(null);
	    	 
	    	 var path = svg.selectAll('path')
	    	 .data(pie(data4Pie))
	    	 .enter()
	    	 .append('path')
	    	 .attr('d', arc)
	    	 .attr('fill', function(d) {
	    		 return color(d.data.label);
	    	 });
	    	 
	    	 var legend = svg.selectAll('.legend')                     // NEW
	          .data(color.domain())                                   // NEW
	          .enter()                                                // NEW
	          .append('g')                                            // NEW
	          .attr('class', 'legend')                                // NEW
	          .attr('transform', function(d, i) {                     // NEW
	            var height = legendRectSize + legendSpacing;          // NEW
	            var offset =  height * color.domain().length / 2;     // NEW
	            var horz = -2 * legendRectSize;                       // NEW
	            var vert = i * height - offset;                       // NEW
	            return 'translate(' + horz + ',' + vert + ')';        // NEW
	          });      
	    	 
	    	 legend.append('rect')                                     // NEW
	          .attr('width', legendRectSize)                          // NEW
	          .attr('height', legendRectSize)                         // NEW
	          .style('fill', color)                                   // NEW
	          .style('stroke', color);                                // NEW
	          
	        legend.append('text')                                     // NEW
	          .attr('x', legendRectSize + legendSpacing)              // NEW
	          .attr('y', legendRectSize - legendSpacing)              // NEW
	          .text(function(d) { return d; });    
	    	 console.log(data4Pie);
	      });
</script>
</struct:widget>
</c:when>
<c:otherwise>
  <div>Missing product-feature metric - You do not have access rights to the backlog</div>
</c:otherwise>
</c:choose>