<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="css/styles.css" rel="stylesheet" type="text/css">
<html>
<head>
    <title>Counting2 Page</title>
</head>
<body>

<div class="form-style-9">
    <table class="table">
        <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Rating</th>
        </tr>

        <c:forEach items="${catList}" var="cat">
            <tr>
                <td>    <img src="data:image/jpg;base64,${cat.photo}" width="300" height="300" />   </td>
                <td>    ${cat.name}   </td>
                <td>    ${cat.rating}   </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
