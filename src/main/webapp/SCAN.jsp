<%@ page import="classes.CT" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static java.lang.Integer.parseInt" %>
<%@ page import="java.util.Arrays" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>扫描算法</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" type="text/css" href="./CSS/whole.css"/>
    <script src="https://cdn.bootcdn.net/ajax/libs/Chart.js/2.9.4/Chart.bundle.min.js"></script>
</head>
<body background="./Picture/0402.jpg"
      style="background-repeat:no-repeat;
               background-attachment:fixed;
               background-size:100% 100%; ">

<div class="container">
    <div class="top-left">
        <img src="./Picture/b1.jpg"  class="img-back">
        <div class="head1">
            <h2>输入模块</h2>
        </div>

        <div class="head2">
            <form action="SCANServlet" method="post" style="margin-left: 10px">
                <input type="submit" value="初始化" name="s3"
                       class="button-intial">
            </form>

            <form action="SCANServlet"  method="post" style="margin-left: 20px">
                <strong   style="color:black">磁头初始位置:</strong>
                <input type="number" min="0" step="1" name="firstNum" value="${firstNum}">


                <% String selectdir="1";
                    String ss= (String)request.getAttribute("dir0");
                    if(ss==null)
                        selectdir="1";
                    else selectdir=ss;
                %>

                <input type="radio"   value="1" <%=selectdir.equals("1")?"Checked":" "%>
                       name="dir"/><strong   style="color:black">磁道号增大</strong>
                <input type="radio"   value="0" <%=selectdir.equals("0")?"Checked":" "%>
                       name="dir"/><strong   style="color:black">磁道号减小</strong>

                <input type="submit" value="确认" name="s4" class="button-intial" style="margin-left: 10px">
            </form>

        </div>

        <div class="head3">
            <form action="SCANServlet"  method="post" style="margin-left: 20px">
                <strong   style="color:orange">***--输入--***&nbsp&nbsp&nbsp&nbsp
                    &nbsp&nbsp&nbsp&nbsp&nbsp</strong>
                <strong   style="color:black"> 磁道号: </strong>
                <input name="tNum" type="number" required="required" min="0" step="1">
                <input type="submit" value="提交" name="s1" class="button-intial" style="margin-left: 20px">
            </form>

            <form action="SCANServlet" method="post" style="margin-left: 118px">
                <input type="submit" value="运行" name="s2"  class="button-run">
            </form>
        </div>
        <table border="1" class="table1">
            <tr style="text-align: center;height:40px">
                <th style="width: 260px">序号</th>
                <th style="width: 330px">磁道号</th>
            </tr>
            <%
                ArrayList ctss = (ArrayList) request.getAttribute("sk");
                if(ctss!=null)
                    for(int i=0;i<ctss.size();i++)
                    {
            %>

            <tr style="text-align: center;height:30px">
                <td class="font1"><%if(ctss!=null){%><%=i+1%><%}%></td>
                <td class="font2"><%if(ctss!=null){%><%=((CT)ctss.get(i)).gettNum()%><%}%></td>
            </tr>
            <%
                    }
            %>
        </table>

    </div>

    <div class="top-right">
        <img src="./Picture/b1.jpg"  class="img-back">
        <div class="head1">
            <h2>过程演示</h2>
        </div>

        <div class="head2">
            <div style="float:left;">
                <strong class="font3">待访问的磁道:&nbsp</strong>
            </div>
            <div id="queue">
                <%
                    ArrayList arr =  (ArrayList)request.getAttribute("myArray");
                    if(arr!=null){%><%=arr.toString().replaceAll("^\\[|\\]$", "").replaceAll(",\\s*", " ")%><%}
            %>

            </div>
        </div>

        <div class="head4">

            <div class="head41">
                <div class="circle">
                    <div id="innerc" class="inner-circle"> </div>
                </div>
            </div>

            <div class="head42">
                <div class="trow">
                    <div class="tbox1"><strong class="font3">上一个访问磁道</strong></div>
                    <div id="last" class="tbox2" ></div>
                </div>
                <div class="trow">
                    <div class="tbox1"><strong class="font3">正在访问磁道号</strong></div>
                    <div id="now" class="tbox2" ></div>
                </div>
                <div class="trow">
                    <div class="tbox1"><strong class="font3">下一个访问磁道</strong></div>
                    <div id="next" class="tbox2" ></div>
                </div>
            </div>

        </div>

        <div class="head2" style="margin-top: 10px;">
            <div style="float:left;">
                <strong class="font3">已访问的磁道:&nbsp</strong>
            </div>

            <div id="deleted"></div>

        </div>
    </div>
    <%!
        // 将 Java 数组转换为 JavaScript 数组
        public String toJavaScriptArray(ArrayList arr) {
            if(arr!=null){
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i = 0; i < arr.size(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(arr.get(i));
                }
                sb.append("]");
                return sb.toString();
            }
            else return null;
        }
    %>
    <script>

        var last, next, now;
        var queue = <%= toJavaScriptArray(arr) %>;
        var deleted = [];
        if(queue!=null) {
            next = queue.slice(0, 1)[0];
            document.getElementById("next").textContent = next;
            var intervalId = setInterval(function () {
                last = now;
                document.getElementById("last").textContent = last;

                deleted.push(last);
                var deletedStr = deleted.join(" ");
                document.getElementById("deleted").textContent = deletedStr;


                var element = queue.shift();
                var queueStr = queue.join(" ");
                document.getElementById("queue").textContent = queueStr;

                now = element;
                document.getElementById("now").textContent = now;
                document.getElementById("innerc").textContent = now;

                next = queue.slice(0, 1)[0];
                document.getElementById("next").textContent = next;
            }, 2000);
        }
    </script>




    <div class="bottom-left">
        <img src="./Picture/0512.jpg"  class="img-back">
        <div class="head1">
            <h2>运行结果</h2>
        </div>


        <table border="2" class="table1" style="max-height:380px">
            <tr style="text-align: center; height:40px">
                <th style="width: 90px;">序号</th>
                <th style="width: 250px;">磁道号</th>
                <th style="width: 250px;">寻道距离</th>
            </tr>
            <%
                ArrayList ctsss = (ArrayList) request.getAttribute("skk");
                if(ctsss!=null)
                    for(int i=0;i<ctsss.size();i++)
                    {
            %>

            <tr style="text-align: center;">
                <td class="font1"><%if(ctsss!=null){%><%=i+1%><%}%></td>
                <td class="font1"><%if(ctsss!=null){%><%=((CT)ctsss.get(i)).gettNum()%><%}%></td>
                <td class="font2"><%if(ctsss!=null){%><%=((CT)ctsss.get(i)).getDis()%><%}%></td>
            </tr>
            <%

                    }
            %>
        </table>

        <table border="2" class="table1">
            <%
                int sum=0;
                if(ctsss!=null)
                    for(int m=0;m<ctsss.size();m++)
                    {
                        sum+=((CT)ctsss.get(m)).getDis();
                    }

            %>

            <tr style="text-align: left;">
                <td style="width: 590px;">平均寻道时间：<%if(ctsss!=null){%><%=sum*1.0/ctsss.size()%><%}%></td>

            </tr>
        </table>








    </div>


    <div class="bottom-right">
        <img src="./Picture/b1.jpg"  class="img-back">
        <div class="head1">
            <h2>动态折线图展示</h2>
        </div>
        <div style="width: 92%; height: 80%;background-color: wheat;
    display: flex;justify-content: center;align-items: center;margin-top: 15px;
    margin-left: 35px;">
            <canvas id="myChart" style="width:96%; height: 90%;"></canvas>
        </div>

        <script>
            // 获取数据
            var labelList = ${requestScope.labellist};
            var dataList = ${requestScope.datalist};
            var ctx = document.getElementById('myChart').getContext('2d');
            var chart = new Chart(ctx, {
                type: 'line',
                // 配置数据
                data: {
                    labels: labelList,
                    datasets: [{
                        label: '过程展示',
                        data: dataList,
                        backgroundColor: 'rgba(255, 102, 0, 0.5)', // 折线下方的填充颜色
                        borderColor: 'rgba(255, 102, 0, 1)', // 折线的颜色
                        borderWidth: 2,
                        pointBackgroundColor: 'white', // 折线上方点的背景颜色
                        pointRadius: 5, // 点的半径
                        pointHoverRadius: 8 // 鼠标悬停时点的半径
                    }]
                },
                // 配置选项
                options: {
                    responsive: true,
                    backgroundColor:'#fde',
                    title: {
                        display: true,
                        text: '过程展示',
                        fontSize:25,
                        fontColor: '#333',
                        family:'lishu'
                    },
                    scales: {
                        xAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: '磁道号',
                                fontSize: 15
                            }
                        }],
                        yAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: '磁道访问次序',
                                fontSize: 15
                            }
                        }]
                    },
                    elements: {
                        line: {
                            tension: 0  // 设为0表示不使用曲线
                        }
                    },
                    animation: {  // 禁用默认动画效果
                        duration: 0
                    },
                    legend: {
                        display: false
                    },
                    hover: {
                        animationDuration: 0  // 禁用鼠标悬停时的动画
                    },
                    responsiveAnimationDuration: 0  // 禁用尺寸调整时的动画
                }
            });
            // 设置绘制间隔时间
            var drawInterval = 2000;
            // 定义绘制线段的函数
            function drawLineSegment(prevData, currData, index) {
                setTimeout(function() {
                    prevData.push(currData);
                    // 更新图表数据
                    chart.data.datasets[0].data = prevData;
                    chart.update({
                        duration: 0,
                        easing: 'linear'
                    });
                }, index * drawInterval);
                return prevData;
            }
            // 逐条绘制折线图
            var initialData = [];
            dataList.reduce(drawLineSegment, initialData);
        </script>

    </div>
</div>
</body>
</html>

