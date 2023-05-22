package TotalZ;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.*;
import java.io.IOException;
import ClassZqs.class1;
import ClassZqs.Answer;
@WebServlet(name = "FIFOServlet", value = "/FIFOServlet")
public class FIFOServlet extends HttpServlet {
    public static int current_time = 0; //计数系统当前时间


    int flag = 0;
    Integer len=0,num=0;
    int id = 0;
    class1 temp = new class1();
    boolean request1=false;
    int[] tempint;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String add = request.getParameter("add");
        String execute = request.getParameter("execute");
        String initialize = request.getParameter("initialize");

        if(initialize !=  null){
            len = 0;
            num = 0;
            id = 0;
            temp = new class1();
            flag = 0;
            request1 = false;
        }
        if(add!=null && execute==null){
            request1 = true;

            if(flag!=1){ //只接收一次
                len = Integer.parseInt(request.getParameter("len"));
                num = Integer.parseInt(request.getParameter("num"));
                temp.init(num, len);

            }
            flag = 1;
            String str = request.getParameter("arrnumber");
            String[] strarray = str.split(" ");

            if(id+ strarray.length<=len && strarray.length>0){

                tempint = new int[id+strarray.length];
                for(int i=0; i<id; i++){
                    tempint[i] = temp.page[i];
                }
                for (int i = 0; i < strarray.length; i++) {
                    tempint[id+i] = Integer.parseInt(strarray[i]);
                }

                for(int i=0; i< strarray.length; i++){
                    temp.page[id+i] = tempint[id+i];
                }
                request.setAttribute("temp",tempint);
                id+=strarray.length;
            }else if(id+ strarray.length>len){//页面数目超出范围
                JOptionPane.showMessageDialog(null, "现在的数目已经超出范围", "警告", JOptionPane.WARNING_MESSAGE);

            }else{
                JOptionPane.showMessageDialog(null, "输入的数据为空", "警告", JOptionPane.WARNING_MESSAGE);

            }

        }else if(add==null && execute!=null){
            if(id==len){
                flag = 2;
                request1 = false;
                temp.FIFO();

                Answer[] ans = temp.answers;
//            for(int i=0; i<len; i++){

                request.setAttribute("len",len);
                request.setAttribute("ans",ans);
                request.setAttribute("temp", temp.page);
//            }
            }else{
                request.setAttribute("temp",tempint);
            }


        }
        request.setAttribute("true",request1);
        int total = temp.mispage+temp.getpage;
        double total2 = temp.mispage*1.0/total*100;
        request.setAttribute("mispage",temp.mispage);
        request.setAttribute("total",total);
        request.setAttribute("total2",total2);
        request.setAttribute("flag",flag);

        request.getRequestDispatcher("/ReqPage/FIFO.jsp").forward(request,response);
    }
}
