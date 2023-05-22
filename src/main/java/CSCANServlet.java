import classes.CT;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;
@WebServlet(name = "CSCANServlet", value = "/CSCANServlet")
public class CSCANServlet extends HttpServlet {

    ArrayList<CT> cts = new ArrayList<CT>();
    ArrayList<CT> cs = new ArrayList<CT>();
    int up=1;
    int firstbeg=0;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        PrintWriter pWriter = response.getWriter();
        String k1 = request.getParameter("s1");//提交
        String k2 = request.getParameter("s2");//运行
        String k3 = request.getParameter("s3");//初始化
        String k4 = request.getParameter("s4");//初始位置
        String dir=request.getParameter("dir");//up方向

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
            request.setAttribute("dir0",String.valueOf(up));
            request.setAttribute("firstNum", firstbeg);
        }
        //运行
        else if(k2!=null)
        {

            request.setAttribute("dir0",String.valueOf(up));
            ArrayList<Integer> labelList = new ArrayList<Integer>();
            ArrayList<Integer> dataList = new ArrayList<Integer>();

            if(up==1)
            {

                request.setAttribute("sk", cts);
                request.setAttribute("skk",CT.CSCANup(cs));
                request.setAttribute("firstNum", firstbeg);

               ArrayList ctsss = (ArrayList)CT.CSCANup(cs);

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
            else
            {

                request.setAttribute("sk", cts);
                request.setAttribute("skk",CT.CSCANdown(cs));
                request.setAttribute("firstNum", firstbeg);

                ArrayList ctsss = (ArrayList)CT.CSCANdown(cs);

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
            if(dir!=null)
            {
                if(dir.equals("1"))
                    up = 1;
                else
                    up = 0;

            }
            String firstNum = request.getParameter("firstNum");
            firstbeg=parseInt(firstNum);

            request.setAttribute("dir0",dir);
            request.setAttribute("firstNum", firstbeg);

        }
        // 转发到jsp网页

        request.getRequestDispatcher("CSCAN.jsp").forward(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}