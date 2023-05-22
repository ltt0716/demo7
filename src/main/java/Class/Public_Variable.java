package Class;

public class Public_Variable {
    int pnum = 0;   //进程数
    float average_turnaroundTime = (float) 0.0;     //平均周转时间
    float average_weighted_turnaroundTime = (float) 0.0;  //平均带权周转时间
    int timeSlices = 0;     //时间片

    public void init(){
        this.pnum = 0;
        this.average_turnaroundTime = (float) 0.0;
        this.average_weighted_turnaroundTime = (float) 0.0;
        this.timeSlices = 0;
    }
    public int getPnum(){
        return pnum;
    }

    public float getAverage_turnaroundTime(){ return average_turnaroundTime; }

    public float getAverage_weighted_turnaroundTime() {
        return average_weighted_turnaroundTime;
    }

    public int getTimeSlices() {
        return timeSlices;
    }

    public void setPnum(){
        pnum++;
    }

    public void setTimeSlices(int timeSlices) {
        this.timeSlices = timeSlices;
    }

    // 更新平均周转时间
    public void updateAverage_turnaroundTime(Integer turnaroundTime){
        average_turnaroundTime += (float) turnaroundTime;
    }
    // 更新平均带权周转时间
    public void updateAverage_weighted_turnaroundTime(float weighted_turnaroundTime) {
        this.average_weighted_turnaroundTime += weighted_turnaroundTime;
    }
    // 计算平均周转时间
    public void calculateAverage_turnaroundTime() {
        this.average_turnaroundTime /= this.pnum;
    }
    // 计算平均带权周转时间
    public void calculateAverage_weighted_turnaroundTime(){
        this.average_weighted_turnaroundTime /= this.pnum;
    }
}
