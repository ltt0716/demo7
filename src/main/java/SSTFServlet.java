import classes.CT;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

@WebServlet(name = "SSTFServlet", value = "/SSTFServlet")
public class SSTFServlet extends HttpServlet {
    ArrayList<CT> cts = new ArrayList<CT>();
    ArrayList<CT> cs = new ArrayList<CT>();
int  fenkuai=999;
    int firstbeg=0;
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        PrintWriter pWriter = response.getWriter();
        String k1 = request.getParameter("s1");
        String k2 = request.getParameter("s2");
        String k3 = request.getParameter("s3");
        String k4 = request.getParameter("s4");
        String sfenkuai = request.getParameter("fenkuai");
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
            request.setAttribute("fenkuai", fenkuai);

        }
        //运行
        else if(k2!=null)
        {

            request.setAttribute("sk", cts);
            request.setAttribute("firstNum", firstbeg);

            //一个new
            ArrayList<CT> cp = new ArrayList<CT>();
            ArrayList<CT> cp0 = new ArrayList<CT>();
            //cp.addAll(cts);
            int index=firstbeg;

            if(fenkuai>=cts.size())
            {
                ArrayList<Integer> labelList = new ArrayList<Integer>();
                ArrayList<Integer> dataList = new ArrayList<Integer>();

                request.setAttribute("skk",CT.SSTF(cs));

                ArrayList ctsss = (ArrayList)CT.SSTF(cs);
                for(int i=0;i<ctsss.size();i++) {
                    labelList.add(i + 1);
                    dataList.add(((CT) ctsss.get(i)).gettNum());
                }
                request.setAttribute("myArray",  dataList);
                // 折线图
                request.setAttribute("labellist", labelList);
                request.setAttribute("datalist", dataList);
                }
            else {
                for(int i=0;i< cts.size();i++)
                {
                    if(i!=0&&i%fenkuai==0)
                    {
                        ArrayList<CT> cp1 = new ArrayList<CT>();
                        cp1.addAll((ArrayList)CT.TF(cp,index));
                        index=cp1.get(cp1.size()-1).gettNum();
                        System.out.println(index);
                        cp1.remove(cp1.size() - 1);
                        cp0.addAll(cp1);

                        cp.clear();
                        cp.add(cts.get(i));
                    }
                    else
                        cp.add(cts.get(i));
                }
                ArrayList<CT> cp2 = new ArrayList<CT>();
                cp2.addAll((ArrayList) CT.TF(cp, index));

                cp2.remove(cp2.size() - 1);
                cp0.addAll(cp2);

                ArrayList<Integer> labelList = new ArrayList<Integer>();
                ArrayList<Integer> dataList = new ArrayList<Integer>();

                for(int i=0;i<cp0.size();i++)
                {
                    labelList.add(i+1);
                    dataList.add(((CT)cp0.get(i)).gettNum());
                }

                request.setAttribute("myArray",  dataList);
                // 折线图
                request.setAttribute("labellist", labelList);
                request.setAttribute("datalist", dataList);
                request.setAttribute("skk",cp0);
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
            request.setAttribute("fenkuai", 999);
        }
        //设置初始位置
        else if(k4!=null)
        {
            String firstNum = request.getParameter("firstNum");
            fenkuai=parseInt(sfenkuai);
            firstbeg=parseInt(firstNum);
            request.setAttribute("firstNum", firstbeg);
            request.setAttribute("fenkuai", fenkuai);

        }
        // 转发到jsp网页

        request.getRequestDispatcher("SSTF.jsp").forward(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
