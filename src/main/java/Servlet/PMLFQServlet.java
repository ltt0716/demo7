package Servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

import Class.Process_Sort;
import Class.Public_Variable;
import Class.Process;

import static java.lang.Math.pow;

@WebServlet(name = "PMLFQServlet", value = "/PMLFQServlet")
public class PMLFQServlet extends HttpServlet {
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
        }
        // 添加
        if(add != null){
            // 获取输入的进程信息
            String name = request.getParameter("name");
            Integer arrivalTime = Integer.parseInt(request.getParameter("arrivalTime"));
            Integer burstTime = Integer.parseInt(request.getParameter("burstTime"));

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
        // 执行
        if(execute!=null){
            // 用一个大小刚好的数组装进程
            Process[] process = new Process[public_variable.getPnum()];
            // 用于保持输出添加的进程信息
            Process[] process1 = new Process[public_variable.getPnum()];
            for(int i=0;i<public_variable.getPnum();i++){
                process[i] = temp[i];
                process1[i] = temp[i];
            }

            // 按到达时间升序排序进程
            Process_Sort process_sort = new Process_Sort();
            process_sort.Sort1(process);
            // 将当前时间更新到最早到达进程的到达时间
            current_time = process[0].getArrivalTime();
            System.out.println("当前时间:"+current_time);
            // 将进程插入进程队列
            for(int i=0;i< public_variable.getPnum();i++){
                processQueue.add(process[i]);
                // 记录初始需要服务的时间
                map.put(process[i].getID(),process[i].getBurstTime());
                // 将过程信息加入map容器
                pmadd(1,process[i].getArrivalTime(),process[i].getBurstTime(),0,process[i].getName());
            }

            // 初始化每个队列元素
            for(int i=0;i<10;i++){
                readyQueue[i] = new LinkedList<Process>();
            }
            // 处理数据
            scheduleProcesses(public_variable);
            // 计算平均周转时间
            public_variable.calculateAverage_turnaroundTime();
            // 计算平均带权周转时间
            public_variable.calculateAverage_weighted_turnaroundTime();

            for(int i=0;i<public_variable.getPnum();i++){
                System.out.print(process[i].getName()+" ");
                System.out.println(process[i].getWaitTime());
            }

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
        request.getRequestDispatcher("/JSP/PMLFQ.jsp").forward(request,response);
    }

    // 就绪队列数组
    private static Queue<Process> readyQueue[] = new LinkedList[10];
    // 已创建的进程队列
    private static Queue<Process> processQueue = new LinkedList<Process>();

    public static void scheduleProcesses(Public_Variable public_variable){
        int n = 0;  //记录已结束进程数
        while (!processQueue.isEmpty()){
            // 存储即将到达就绪队列的进程的到达时间
            int ntime = Integer.MAX_VALUE;
            // 将同时间到达的进程插入一级队列
            while (!processQueue.isEmpty()){
                if(processQueue.element().getArrivalTime() == current_time){
                    readyQueue[0].add(processQueue.remove());
                }else{
                    break;
                }
            }
            // 记录下一个即将到达就绪队列的进程的到达时间
            if(!processQueue.isEmpty()){
                ntime = processQueue.element().getArrivalTime();
            }

            // 处理就绪队列
            int lastq = 0;  // 记录装有进程的优先级最低的就绪队列下标
            int ts,needtime;    // 时间片，仍需服务时间
            for(int i=0;i<=lastq;i++){
                while (!readyQueue[i].isEmpty()){
                    Process process = readyQueue[i].remove();
                    // 计算当前队列可用的时间片大小
                    ts = (int)pow(2,i);
                    // 仍需服务时间
                    needtime = (int)map.get(process.getID());
                    // 将过程信息加入map容器
                    pmadd(2,current_time,needtime,ts,process.getName());

                    // 若时间片内无法结束
                    if(needtime > ts){
                        // 若此时间片内有新进程到达
                        if(ntime < current_time+ts){
                            // 将所有ntime时刻到达的进程插入一级就绪队列队尾
                            while (!processQueue.isEmpty()){
                                if(processQueue.element().getArrivalTime() == ntime){
                                    readyQueue[0].add(processQueue.remove());
                                }else{
                                    break;
                                }
                            }

                            // 判断是否抢占cpu使用权
                            if(i == 0){ // 不抢占
                                // 更新当前时间和当前进程仍需服务的时间并把进程插入下一级队列
                                current_time += ts;
                                map.replace(process.getID(),needtime-ts);
                                readyQueue[i+1].add(process);
                                // 更新最后一个有进程的队列的下标
                                if(i+1 > lastq){
                                    lastq = i+1;
                                }

                                // 将过程信息加入map容器
                                pmadd(3,current_time,(int)map.get(process.getID()),0,process.getName());
                            }else{  // 抢占
                                // 更新当前时间和当前进程仍需服务的时间并把进程插入当前队列队尾
                                current_time = ntime;
                                map.replace(process.getID(),needtime+current_time-ntime);
                                readyQueue[i].add(process);
                                // 更改i，从第一级队列开始运行
                                i = -1;
                                // 将过程信息加入map容器
                                pmadd(4,current_time,(int)map.get(process.getID()),0,process.getName());

                                // 跳出当前队列循环
                                break;
                            }

                            // 更新ntime
                            if(!processQueue.isEmpty()){
                                ntime = processQueue.element().getArrivalTime();
                            }else{
                                ntime = Integer.MAX_VALUE;
                            }
                        }else{  // 无新进程进入
                            // 更新当前时间和当前进程仍需服务的时间并把进程插入下一级队列
                            current_time += ts;
                            map.replace(process.getID(),needtime-ts);
                            readyQueue[i+1].add(process);
                            if(i+1 > lastq){
                                lastq = i+1;
                            }

                            // 将过程信息加入map容器
                            pmadd(3,current_time,(int)map.get(process.getID()),0,process.getName());
                        }
                    }else{  // 时间片内可结束
                        // 若此结束前有新进程到达
                        if(ntime < current_time+needtime){
                            // 将所有ntime时刻到达的进程插入一级就绪队列队尾
                            while (!processQueue.isEmpty()){
                                if(processQueue.element().getArrivalTime() == ntime){
                                    readyQueue[0].add(processQueue.remove());
                                }else{
                                    break;
                                }
                            }

                            // 判断是否抢占cpu使用权
                            if(i == 0) { // 不抢占
                                // 更新当前时间和就绪队列中的进程数
                                current_time += needtime;
                                map.replace(process.getID(),0);

                                // 将过程信息加入map容器
                                pmadd(5,current_time,0,0,process.getName());
                            }else{  //抢占
                                // 更新当前时间和当前进程仍需服务的时间并把进程插入当前队列队尾
                                current_time = ntime;
                                map.replace(process.getID(),needtime+current_time-ntime);
                                readyQueue[i].add(process);
                                // 更改i，从第一级队列开始运行
                                i = -1;

                                // 将过程信息加入map容器
                                pmadd(4,current_time,(int)map.get(process.getID()),0,process.getName());
                            }

                            // 更新ntime
                            if(!processQueue.isEmpty()){
                                ntime = processQueue.element().getArrivalTime();
                            }else{
                                ntime = Integer.MAX_VALUE;
                            }
                            // 跳出当前队列循环
                            if(i == -1){
                                break;
                            }
                        }else{  // 若此结束前无新进程到达
                            // 更新当前时间和就绪队列中的进程数
                            current_time += needtime;
                            map.replace(process.getID(),0);

                            // 将过程信息加入map容器
                            pmadd(5,current_time,0,0,process.getName());
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
                        // 更新结束进程数变量
                        n++;
                        // 若所有进程都结束则结束处理函数
                        if(n == public_variable.getPnum()){
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void pmadd(int p, int time, int needtime,int ts, String name){
        String msg = null;
        // 进程进入就绪队列
        if (p == 1){
            msg = "T="+time+": 进程"+name+"进入就绪队列,需要服务总时间: "+needtime+"。\n";
        }else if(p == 2){
            msg = "T="+time+": 进程"+name+"开始执行,获得时间片大小为: "+ts+",仍需服务时间: "+needtime+"。\n";
        } else if (p == 3) {
            msg = "T="+time+": 进程"+name+"时间片用完，插入下一级队列队尾,仍需服务时间: "+needtime+"。\n";
        } else if (p == 4) {
            msg = "T="+time+": 进程"+name+"被抢占，插入当前队列队尾,仍需服务时间: "+needtime+"。\n";
        } else {
            msg = "T="+time+": 进程"+name+"完成所有服务。\n";
        }

        if (!treemap.containsKey(time)){
            treemap.put(time,new ArrayList<>());
        }
        treemap.get(time).add(msg);
    }
}
