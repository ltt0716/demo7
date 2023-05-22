package Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class Process_Sort{
    // 根据到达时间排序
    public static int compare1(Process p1, Process p2){
        return Integer.compare(p1.getArrivalTime(),p2.getArrivalTime());
    }
    // 先根据到达时间升序排序，若相同则根据优先级降序排序
    public static int compare2(Process p1, Process p2) {
        int ans = Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        if(ans == 0){
            ans = ans - Integer.compare(p1.getPriority(), p2.getPriority());
        }

        return ans;
    }
    // 先根据到达优先级升序排序，若相同则根据时间降序排序
    public static int compare3(Process p1, Process p2){
        int ans = 0 - Integer.compare(p1.getPriority(), p2.getPriority());
        if(ans == 0){
            ans = Integer.compare(p1.getArrivalTime(),p2.getArrivalTime());
        }

        return ans;
    }

    public Process[] Sort1(Process[] process){
        Arrays.sort(process,Process_Sort::compare1);
        return process;
    }

    public Process[] Sort2(Process[] process){
        Arrays.sort(process,Process_Sort::compare2);
        return process;
    }

    public Process[] Sort3(Process[] process){
        Arrays.sort(process,Process_Sort::compare3);
        return process;
    }
}
