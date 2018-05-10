<%--
  Created by IntelliJ IDEA.
  User: Daniil
  Date: 14.04.2018
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="css/styles.css" rel="stylesheet" type="text/css">
<html>
<head>
    <title>Vote Page</title>
</head>
<body>

<div class="form-style-8">
    Vote for best cat! <br>
    Stage: <%= session.getAttribute("stage") %>
    <form action="" method = "POST">
        <table>
            <tr>
                <td> <img src="data:image/jpg;base64,<%= request.getAttribute("image1") %>" width="300" height="300" />  </td>
                <td> <img src="data:image/jpg;base64,<%= request.getAttribute("image2") %>" width="300" height="300" />  </td>
            <tr>

            <tr>
                <td> <%= session.getAttribute("lcat1") %>  </td>
                <td> <%= session.getAttribute("lcat2")%>   </td>
            <tr>

            <tr>
                <td> <input type="submit" name="bcat1" value="Vote!"> </td>
                <td> <input type="submit" name="bcat2" value="Vote!"> </td>
            <tr>

        </table>
    </form>
</div>

</body>
</html>
