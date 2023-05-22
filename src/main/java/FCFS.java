import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import classes.CT;

import static java.lang.Integer.parseInt;

@WebServlet(name = "FCFS", value = "/FCFS")
public class FCFS extends HttpServlet {

    ArrayList<CT> cts = new ArrayList<CT>();
    ArrayList<CT> cs = new ArrayList<CT>();

    int firstbeg=0;
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        PrintWriter pWriter = response.getWriter();
        String k1 = request.getParameter("s1");
        String k2 = request.getParameter("s2");
        String k3 = request.getParameter("s3");
        String k4 = request.getParameter("s4");
        //添加
        if (k1!=null) {
            CT ct = new CT();
            String tName = "None";
            String tNum = request.getParameter("tNum");
            ct.settName(tName);
            ct.settNum(parseInt(tNum));
            ct.setbegin(firstbeg);
            cts.add(ct);
            cs.add(ct);
            request.setAttribute("sk", cts);
            request.setAttribute("firstNum", firstbeg);

        }
        //运行
        else if(k2!=null)
        {
            request.setAttribute("sk", cts);
            request.setAttribute("skk",CT.FCFS(cs));
            request.setAttribute("firstNum", firstbeg);

            ArrayList<Integer> labelList = new ArrayList<Integer>();
            ArrayList<Integer> dataList = new ArrayList<Integer>();

                ArrayList ctsss = (ArrayList)CT.FCFS(cs);
                for(int i=0;i<ctsss.size();i++)
                {
                    labelList.add(i+1);
                    dataList.add(((CT)ctsss.get(i)).gettNum());
                }
                //过程展示
                request.setAttribute("myArray",  dataList);
                // 折线图
                request.setAttribute("labellist", labelList);
                request.setAttribute("datalist", dataList);
        }
        //初始化
        else if(k3!=null)
        {
            cts.clear();
            cs.clear();
            request.setAttribute("sk", null);
            request.setAttribute("skk",null);

            request.setAttribute("firstNum", 0);


        }
        //设置初始位置
        else if(k4!=null)
        {
            String firstNum = request.getParameter("firstNum");
            firstbeg=parseInt(firstNum);
            request.setAttribute("firstNum", firstbeg);

        }
        // 转发到jsp网页

        request.getRequestDispatcher("tFCFS.jsp").forward(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
