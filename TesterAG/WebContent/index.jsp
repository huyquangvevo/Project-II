<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="text-align: center">
<form action="IndexAG" method="post">
	<h2>Get Instance Of Class</h2>
        <input name="instanceClass" style="padding: 5px; width: 300px; border: 0.5px solid #A9A9A9; border-radius: 5px"><br>
        <input type="submit" value="Send" style="margin: 8px; padding: 6px 20px; font-size: 16px; border-radius:5px; background-color: red; color: white; border:  0px;">

        <h2>Get Property Of Instance</h2>
        <input name="propertyInstance" style="padding: 5px; width: 300px; border: 0.5px solid #A9A9A9; border-radius: 5px"><br>
        <input type="submit" value="Send" style="margin: 8px; padding: 6px 20px; font-size: 16px; border-radius:5px; background-color: red; color: white; border:  0px;">

        <h2>Get All Instance</h2>
        <input name="allInstance" style="padding: 5px; width: 300px; border: 0.5px solid #A9A9A9; border-radius: 5px"><br>
        <input type="submit" value="Send" style="margin: 8px; padding: 6px 20px; font-size: 16px; border-radius:5px; background-color: red; color: white; border:  0px;">
        <input type="hidden" name="automatic" value="yes" >
</form>
</body>
</html>