<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
<% /*String selectdir;
    String ss= (String)request.getAttribute("dir0");
    if(ss==null)
        selectdir="1";
    else selectdir=ss;*/
    String selectdir="0";
%>
<input type="radio"   value="1" <%=selectdir=="1"?"Checked":""%>
       name="dir"/><strong   style="color:black">磁道号增大</strong>
<input type="radio"   value="0"   <%=selectdir=="0"?"Checked":""%>
       name="dir"/><strong   style="color:black">磁道号减小</strong>
</body>
</html>