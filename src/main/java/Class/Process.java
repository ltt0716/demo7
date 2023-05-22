package Class;

public class Process {
    String name;        //进程名
    Integer id = 0;         //进程id
    Integer arrivalTime;    //到达时间
    Integer burstTime;      //运行时间
    Integer finishTime;     //结束时间
    Integer waitTime;       //等待时间
    Integer turnaroundTime; //周转时间
    Integer priority;       //优先级
    float weighted_turnaroundTime;    //带权周转时间

    public Process(String name, Integer arrivalTime, Integer burstTime, Integer id) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.id = id;
    }
    // 重载构造函数，用于优先级(抢占式)算法
    public Process(String name, Integer arrivalTime, Integer burstTime, Integer id, Integer priority){
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.id = id;
        this.priority = priority;
    }
    public void init(){
        this.name = null;
        this.arrivalTime = null;
        this.burstTime = null;
        this.finishTime = null;
        this.waitTime = null;
        this.turnaroundTime = null;
        this.weighted_turnaroundTime = (float) 0.0;
    }
    public String getName(){
        return this.name;
    }

    public int getID(){
        return this.id;
    }
    public int getArrivalTime(){
        return this.arrivalTime;
    }

    public int getBurstTime(){
        return this.burstTime;
    }

    public int getFinishTime() {
        return this.finishTime;
    }

    public int getWaitTime(){ return this.waitTime; }

    public int getTurnaroundTime(){return this.turnaroundTime; }

    public float getWeighted_turnaroundTime() {
        return weighted_turnaroundTime;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setID(){ this.id = 0;}
    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(Integer burstTime) {
        this.burstTime = burstTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public void setWaitTime(Integer waitTime){ this.waitTime = waitTime; }

    public void setTurnaroundTime(Integer turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setWeighted_turnaroundTime(float weighted_turnaroundTime) {
        this.weighted_turnaroundTime = weighted_turnaroundTime;
    }
}
