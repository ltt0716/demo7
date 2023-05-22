<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LFU</title>
  <link href="http://localhost:8080/Curriculum_Design/ReqPage/FIFO.css" rel="stylesheet">

</head>
<body>
  <form action="http://localhost:8080/Curriculum_Design/index.jsp">
    <button class="btn">首页</button></form>
  <form action="/Curriculum_Design/LFUServlet" method="POST">

    <div>

      <div class="input">
        <%
          boolean t = false;
          if(request.getAttribute("true")!=null){
            t = (boolean) request.getAttribute("true");
          }

          if(t){
        %>
        <label>程序长度(页面数):</label>
        <input type="text" name="len" disabled ><br><br>

        <label>页框大小(块大小):</label>
        <input type="text" name="num" disabled ><br><br>
        <%
        }else{
        %>
        <label>程序长度(页面数):</label>
        <input type="text" name="len" ><br><br>

        <label>页框大小(块大小):</label>
        <input type="text" name="num" }><br><br>
        <%
          }
        %>
        <label>到达页面序号:</label>
        <input type="text" name="arrnumber"><br><br>
      </div>
      <%--提交表单到服务器--%>
      <div class="submit">
        <%--提交表单到服务器--%>
        <input type="submit" value="添加" name="add">
        <input type="submit" value="运行" name="execute">
        <input type="submit" οnclick="javascript:window.location.reload();" value="初始化" name="initialize">
      </div>
    </div>
  </form>
  <br><br>


  <%
    int flag = 0;
    if(request.getAttribute("flag")!=null){
      flag = (int) request.getAttribute("flag");
    }
//        System.out.println(flag);
    if(flag>=1){
  %>
  <table>
    <tr>
      <th>序号</th>
      <th>访问页号</th>
    </tr>

    <c:forEach var="temp" items="${temp}" varStatus="loop" >
      <tr>
        <td><c:out value="${loop.index+1}"></c:out></td>
        <td>${temp}</td>
      </tr>
    </c:forEach>
  </table>
  <%
    }
  %>
  <%

    if(flag>=2){
  %>
  <table>
    <tr>
      <th>序号</th>
      <th>访问页号</th>
      <th>页表情况</th>
      <th>是否缺页</th>
      <th>页面置换</th>
    </tr>



    <c:forEach var="temp" items="${ans}" >
      <tr>
        <td>${temp.id}</td>
        <td>${temp.num}</td>
        <td>
          <c:forEach var="page" items="${temp.page}">
            ${page},
          </c:forEach>
        </td>
        <td>${temp.mispage}</td>
        <td>"${temp.prepage}" ----> "${temp.nowpage}"</td>
      </tr>
    </c:forEach>

  </table>
  <lable>缺页次数:${mispage}次</lable>
  <lable>缺页率:${mispage}/${total} = ${total2}%</lable>
  <%
    }
  %>
</body>
</html>
