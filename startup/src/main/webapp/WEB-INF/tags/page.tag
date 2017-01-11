<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" %>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%

  if (paginationSize == null || paginationSize < 0) {
    paginationSize = 5;
  }

  int pageSize = page.getSize();
  int current = page.getNumber() + 0;
  int begin = Math.max(0, current - paginationSize / 2);
  int end = Math.min(begin + (paginationSize - 1), (page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1));

  request.setAttribute("current", current);
  request.setAttribute("begin", begin);
  request.setAttribute("end", end);
  request.setAttribute("pageSize", pageSize);
%>

<div class="am-cf">
  第${current+1} 页 /共 ${page.getTotalPages()} 页，
  每页 ${pageSize}条记录，共${page.totalElements}条记录
  <div class="am-fr">
    <ul class="am-pagination">
      <% if (page.hasPrevious()) { %>
        <li><a href="?page=0&size=${pageSize}">&lt;&lt;</a></li>
        <li><a href="?page=${current - 1}&size=${pageSize}">&lt;</a></li>
      <% } else { %>
        <li class="am-disabled"><a href="#">&lt;&lt;</a></li>
        <li class="am-disabled"><a href="#">&lt;</a></li>
      <% } %>

      <c:forEach var="i" begin="${begin}" end="${end}">
        <c:choose>
          <c:when test="${i == current}">
            <li class="am-active"><a href="?page=${i}&size=${pageSize}">${i+1}</a></li>
          </c:when>
          <c:otherwise>
            <li><a href="?page=${i}&size=${pageSize}">${i+1}</a></li>
          </c:otherwise>
        </c:choose>
      </c:forEach>

      <% if (page.hasNext()) { %>
        <li><a href="?page=${current+1}&size=${pageSize}">&gt;</a></li>
        <li><a href="?page=${page.totalPages - 1}&size=${pageSize}">&gt;&gt;</a></li>
      <% } else { %>
        <li class="am-disabled"><a href="#">&gt;</a></li>
        <li class="am-disabled"><a href="#">&gt;&gt;</a></li>
      <% } %>
    </ul>
  </div>
</div>