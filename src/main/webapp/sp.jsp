<%--
  Created by IntelliJ IDEA.
  User: lt
  Date: 2023/5/12
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>操作系统算法模拟</title>
  <style>
    body {
      margin: 0;
      padding: 0;
      font-family: Arial, sans-serif;
      background-color: #f5f5f5;
    }
    header {
      height: 80px;
      background-color: #333;
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 20px;
    }
    h1 {
      font-size: 36px;
      margin: 0;
    }
    nav {
      background-color: #555;
      color: #fff;
      padding: 10px 20px;
    }
    nav ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    nav li {
      margin: 0 10px;
    }
    nav a {
      color: #fff;
      text-decoration: none;
      font-size: 18px;
    }
    main {
      padding: 20px;
      max-width: 800px;
      margin: 0 auto;
      background-color: #fff;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
      border-radius: 5px;
    }
    h2 {
      font-size: 24px;
      margin-top: 0;
    }
    p {
      margin: 10px 0;
    }
    .button {
      display: inline-block;
      padding: 10px 20px;
      background-color: #555;
      color: #fff;
      border-radius: 5px;
      text-decoration: none;
      margin-right: 10px;
      transition: background-color 0.3s ease;
    }
    .button:hover {
      background-color: #333;
    }
  </style>
</head>
<body>
<header>
  <h1>操作系统算法模拟</h1>
  <a href="#">登录</a>
</header>
<nav>
  <ul>
    <li><a href="#process-scheduling">进程调度算法模拟</a></li>
    <li><a href="#disk-scheduling">磁盘调度算法模拟</a></li>
    <li><a href="#page-replacement">请求分页置换算法模拟</a></li>
  </ul>
</nav>
<main>
  <section id="process-scheduling">
    <h2>进程调度算法模拟</h2>
    <p>请选择一种算法：</p>
    <a href="#">先来先服务算法</a>
    <a href="#">时间片轮转算法</a>
    <a href="#">优先级调度算法</a>
    <a href="#">多级反馈队列算法</a>
  </section>
  <hr>
  <section id="disk-scheduling">
    <h2>磁盘调度算法模拟</h2>
    <p>请选择一种算法：</p>
    <a href="#">先来先服务算法</a>
    <a href="#">最短寻道时间有限算法</a>
    <a href="#">扫描算法（SCAN）</a>
    <a href="#">循环扫描算法</a>
  </section>
  <hr>
  <section id="page-replacement">
    <h2>请求分页置换算法模拟</h2>
    <p>请选择一种算法：</p>
    <a href="#">先进先出页面置换</a>
    <a href="#">最近最久未使用置换</a>
    <a href="#">最少使用置换</a>
    <a href="#">轮转置换算法（CLOCK）</a>
  </section>
</main>
</body>
</html>