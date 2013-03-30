<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	int i;
	Integer counter = (Integer) session.getAttribute("counter");
	if (counter != null) {
		i = counter + 1;
	} else {
		i = 1;
	}
	Integer counter2 = i;
	session.setAttribute("counter", counter2);
	System.out.println(i);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%= i %>
</body>
</html>
