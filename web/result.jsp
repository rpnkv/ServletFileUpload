<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<p style="color: ${color}">Кнопка нажата через ${res} сек ${time}</p>
<p>Servlet was initialized at: ${initTime.toString()}</p>
<p>Admin's email: ${email}</p>
<p>Server context params:</p>
<ul>
    <c:forEach var="name" items="${contextParams}">

        <li>${name}</li>

    </c:forEach>
</ul>
</body>
</html>