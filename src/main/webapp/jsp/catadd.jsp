<%--
  Created by IntelliJ IDEA.
  User: Daniil
  Date: 13.04.2018
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="css/styles.css" rel="stylesheet" type="text/css">
<html>
<head>
    <title>Cat Add Page</title>
</head>
<body>

<div class="form-style-9">
    Add a Cat:
    <form action="" method="post" enctype="multipart/form-data">
        <input type="input-field" name="name" id = "name"/>
        <input type="file" name="file" id ="file"/> <br>
        <input type="submit" name="submit"/>
    </form>
    <a href =<%=(request.getContextPath())%>/vote>Перейти к голосованию</a>
</div>
</body>
</html>
