package Servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

import Class.Process_Sort;
import Class.Public_Variable;
import Class.Process;

@WebServlet(name = "PCLOCKServlet", value = "/PCLOCKServlet")
public class PCLOCKServlet extends HttpServlet {
    public static int current_time = 0; //计数系统当前时间
    public static String flag  = null; //标识位，控制jsp代码执行哪一段
    Public_Variable public_variable = new Public_Variable();    // 公共类
    Process[] temp = new Process[20];
    public static Map map = new HashMap();  // 存储各进程还需服务的时间
    public static TreeMap<Integer, List<String>> treemap = new TreeMap<>();
    String message = "进程调度过程如下:\n";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String add = request.getParameter("add");
        String execute = request.getParameter("execute");
        String initialize = request.getParameter("initialize");
        String systemdv = request.getParameter("systemdv");

        if(initialize != null){
            temp = new Process[20];
            public_variable.init();
            map = new HashMap();
            current_time += 0;
            flag = null;
            treemap = new TreeMap<>();
            message = "进程调度过程如下:\n";
        }

        if(add != null){
            // 获取输入的进程信息
            String name = request.getParameter("name");
            Integer arrivalTime = Integer.parseInt(request.getParameter("arrivalTime"));
            Integer burstTime = Integer.parseInt(request.getParameter("burstTime"));
            public_variable.setTimeSlices(Integer.parseInt(request.getParameter("timeSlices")));

            // 将进程加入数组
            temp[public_variable.getPnum()] = new Process(name, arrivalTime, burstTime, public_variable.getPnum()+1);
            public_variable.setPnum();  //进程计数更新
            // 用一个大小刚好的数组装进程
            Process[] process1 = new Process[public_variable.getPnum()];
            for(int i=0;i<public_variable.getPnum();i++){
                process1[i] = temp[i];
            }
            flag = "false";
            request.setAttribute("flag",flag);
            request.setAttribute("process1",process1);
        }
        // 用系统默认值
        if(systemdv != null){
            String name[] = new String[]{"a","b","c"};
            Integer arrivalTime[] = new Integer[]{2,3,6};
            Integer burstTime[] = new Integer[]{5,3,1};
            public_variable.setTimeSlices(2);

            for(int i=0;i<3;i++){
                temp[i] = new Process(name[i],arrivalTime[i],burstTime[i],i+1);
                public_variable.setPnum();
            }

            // 用一个大小刚好的数组装进程
            Process[] process1 = new Process[public_variable.getPnum()];
            for(int i=0;i<public_variable.getPnum();i++){
                process1[i] = temp[i];
            }
            flag = "false";
            request.setAttribute("flag",flag);
            request.setAttribute("process1",process1);
        }

        if(execute != null){
            // 用一个大小刚好的数组装进程
            Process[] process = new Process[public_variable.getPnum()];
            // 用于保持输出添加的进程信息
            Process[] process1 = new Process[public_variable.getPnum()];
            for(int i=0;i<public_variable.getPnum();i++){
                process[i] = temp[i];
                process1[i] = temp[i];
            }

            // 排序进程
            Process_Sort process_sort = new Process_Sort();
            process_sort.Sort1(process);
            // 将当前时间更新到最早到达进程的到达时间
            current_time = process[0].getArrivalTime();
            for(int i=0;i<public_variable.getPnum();i++){   //加入就绪队列
                readyQueue.add(process[i]);
                // 记录初始需要服务的时间
                map.put(process[i].getID(),process[i].getBurstTime());
                // 将过程信息加入map容器
                pmadd(1,process[i].getArrivalTime(),process[i].getBurstTime(),process[i].getName());
            }
            // 处理数据
            scheduleProcesses(public_variable);
            // 计算平均周转时间
            public_variable.calculateAverage_turnaroundTime();
            // 计算平均带权周转时间
            public_variable.calculateAverage_weighted_turnaroundTime();
            // 发送数据
            flag = "true";
            // 处理过程信息
            for(Map.Entry<Integer,List<String>> entry : treemap.entrySet()){
                List<String> values = entry.getValue();
                for (String value : values){
                    message += value;
                }
            }
            message += "所有进程调度完毕。\n";

            request.setAttribute("message",message);
            request.setAttribute("flag",flag);
            request.setAttribute("process",process);
            request.setAttribute("process1",process1);
            request.setAttribute("public_variable",public_variable);
        }
        // 转发到jsp网页
        request.getRequestDispatcher("/JSP/PCLOCK.jsp").forward(request,response);
    }

    // 就绪队列
    private static Queue<Process> readyQueue = new LinkedList<Process>();
    // 进程调度方法
    private static void scheduleProcesses(Public_Variable public_variable) {
        int ts = public_variable.getTimeSlices();
        int n = 0;  // 记录在队列里，但未到达的队列数
        int size = readyQueue.size();   //记录队列大小
        while(!readyQueue.isEmpty()){
            Process process = readyQueue.remove();
            int needtime = (int)map.get(process.getID());
            if(process.getArrivalTime() > current_time){
                readyQueue.add(process);
                n++;
                // 若恰好队列里所有的进程都无法执行
                if(n == size){
                    // 将当前时间调整为队列里最先到达的进程的到达时间
                    current_time = process.getArrivalTime();
                    n = 0;
                }
                continue;
            }

            // 将过程信息加入map容器
            pmadd(2,current_time,needtime,process.getName());
            n = 0;  // 有进程可以执行，重置n
            if(needtime <= ts){   // 此次时间片即可结束进程
                size--;
                current_time += needtime;
                // 更新需要服务的时间
                map.replace(process.getID(),0);
                // 计算结束时间
                process.setFinishTime(current_time);
                // 将过程信息加入map容器
                pmadd(4,process.getFinishTime(),0,process.getName());
                // 计算等待时间
                process.setWaitTime(process.getFinishTime() - process.getArrivalTime() - process.getBurstTime());
                // 计算周转时间
                process.setTurnaroundTime(process.getFinishTime()-process.getArrivalTime());
                // 计算带权周转时间
                process.setWeighted_turnaroundTime(process.getTurnaroundTime()/process.getBurstTime());
                // 记录总周转时间
                public_variable.updateAverage_turnaroundTime(process.getTurnaroundTime());
                // 记录总带权周转时间
                public_variable.updateAverage_weighted_turnaroundTime(process.getWeighted_turnaroundTime());
            }else{
                current_time += ts;
                // 更新需要服务的时间
                map.replace(process.getID(),needtime-ts);
                // 重新插入就绪队列队尾
                readyQueue.add(process);
                // 将过程信息加入map容器
                pmadd(3,current_time, (int)map.get(process.getID()),process.getName());
            }
        }
    }

    private static void pmadd(int p, int time, int needtime,String name){
        String msg = null;
        // 进程进入就绪队列
        if (p == 1){
            msg = "T="+time+": 进程"+name+"进入就绪队列,需要服务总时间: "+needtime+"。\n";
        }else if(p == 2){
            msg = "T="+time+": 进程"+name+"开始执行,仍需服务时间: "+needtime+"。\n";
        } else if (p == 3) {
            msg = "T="+time+": 进程"+name+"时间片用完，插入就绪队列队尾,仍需服务时间: "+needtime+"。\n";
        } else {
            msg = "T="+time+": 进程"+name+"完成所有服务。\n";
        }

        if (!treemap.containsKey(time)){
            treemap.put(time,new ArrayList<>());
        }
        treemap.get(time).add(msg);
    }
}
