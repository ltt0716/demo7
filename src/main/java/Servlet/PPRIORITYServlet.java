package Servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

import Class.Process_Sort;
import Class.Public_Variable;
import Class.Process;

@WebServlet(name = "PPRIORITYServlet", value = "/PPRIORITYServlet")
public class PPRIORITYServlet extends HttpServlet {
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

        // 初始化
        if(initialize != null){
            temp = new Process[20];
            public_variable.init();
            map = new HashMap();
            current_time += 0;
            flag = null;
            treemap = new TreeMap<>();
            message = "进程调度过程如下:\n";
        }
        // 添加
        if(add != null){
            // 获取输入的进程信息
            String name = request.getParameter("name");
            Integer arrivalTime = Integer.parseInt(request.getParameter("arrivalTime"));
            Integer burstTime = Integer.parseInt(request.getParameter("burstTime"));
            Integer priority = Integer.parseInt(request.getParameter("priority"));

            // 将进程加入数组
            temp[public_variable.getPnum()] = new Process(name, arrivalTime, burstTime, public_variable.getPnum()+1, priority);
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
            Integer priority[] = new Integer[]{2,3,1};

            for(int i=0;i<3;i++){
                temp[i] = new Process(name[i],arrivalTime[i],burstTime[i],i+1,priority[i]);
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
        // 运行
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
            process_sort.Sort2(process);
            // 将当前时间更新到最早到达进程的到达时间
            current_time = process[0].getArrivalTime();
            // 将进程插入进程队列
            for(int i=0;i< public_variable.getPnum();i++){
                processQueue.add(process[i]);
                // 记录初始需要服务的时间
                map.put(process[i].getID(),process[i].getBurstTime());
                // 将过程信息加入map容器
                pmadd(1,process[i].getArrivalTime(),process[i].getBurstTime(),process[i].getName());
            }

            // 处理数据
            scheduleProcesses(public_variable, process_sort);
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
        request.getRequestDispatcher("/JSP/PPRIORITY.jsp").forward(request,response);
    }

    // 就绪队列
    private static Queue<Process> readyQueue = new LinkedList<Process>();
    // 已创建的进程队列
    private static Queue<Process> processQueue = new LinkedList<Process>();

    // 进程调度方法
    public static void scheduleProcesses(Public_Variable public_variable, Process_Sort process_sort){
        while (!processQueue.isEmpty()){
            // 就绪队列为空，将进程插入就绪队列
            Process p = processQueue.remove();
            // 将当前时间更新为当前进程的到达时间
            current_time = p.getArrivalTime();
            // 将进程插入就绪队列
            readyQueue.add(p);
            // 循环遍历查看是否有好几个同时到达的
            while (!processQueue.isEmpty()){
                if(processQueue.element().getArrivalTime() == current_time){
                    readyQueue.add(processQueue.remove());
                }else{
                    break;
                }
            }

            int ntime = Integer.MAX_VALUE;  //用于存储下一个即将进入就绪队列的进程的到达时间
            if(!processQueue.isEmpty()){
                // 更新下一个进程到达就绪队列的时间
                ntime = processQueue.element().getArrivalTime();
            }

            // 处理就绪队列
            while (!readyQueue.isEmpty()){
                Process process = readyQueue.remove();
                // 获取当前进程还需服务的时间
                int needtime = (int)map.get(process.getID());
                // 将过程信息加入map容器
                pmadd(2,current_time,needtime,process.getName());

                // 判断此进程结束前是否会有新进程进入
                if(needtime+current_time > ntime){
                    // 更新仍需服务时间和当前系统时间
                    needtime = needtime + current_time - ntime;
                    map.replace(process.getID(),needtime);
                    current_time = ntime;
                    // ntime时刻同时到达的进程插入就绪队列
                    while (!processQueue.isEmpty()){
                        if(processQueue.element().getArrivalTime() == ntime){
                            Process nextp = processQueue.remove();
                            readyQueue.add(nextp);
                        }else{
                            break;
                        }
                    }

                    if(!processQueue.isEmpty()){
                        // 更新下一个进程到达就绪队列的时间
                        ntime = processQueue.element().getArrivalTime();
                    }else{
                        ntime = Integer.MAX_VALUE; // 没有进程就将时间设为-1
                    }
                    // 重新排序进程
                    Process resortp[] = new Process[readyQueue.size()+1];
                    // 将当前被卡住的进程放入数组第一个
                    resortp[0] = process;
                    int size = readyQueue.size();
                    for(int i=1;i<=size;i++){
                        resortp[i] = readyQueue.remove();
                    }
                    // 主优先级降序。次到达时间升序
                    process_sort.Sort3(resortp);
                    for(int i=0;i<resortp.length;i++){
                        readyQueue.add(resortp[i]);
                    }
                    // 判断当前进程是否会被抢占，若会择输出响应过程信息
                    if(readyQueue.element() != process){
                        pmadd(3,current_time,needtime,process.getName());
                    }
                    continue;
                }else{
                    // 没有新进程进入的话，直接执行完该进程
                    current_time += needtime;
                    pmadd(4,current_time,0,process.getName());
                }

                // 计算结束时间
                process.setFinishTime(current_time);
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
            msg = "T="+time+": 进程"+name+"被抢占,仍需服务时间: "+needtime+"。\n";
        } else {
            msg = "T="+time+": 进程"+name+"完成所有服务。\n";
        }

        if (!treemap.containsKey(time)){
            treemap.put(time,new ArrayList<>());
        }
        treemap.get(time).add(msg);
    }
}
