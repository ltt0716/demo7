package classes;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class CT {
    String tName; // 磁道名称
    int tNum; // 磁道号
    int dis; // 到此磁道所需跨越的磁道数
    int g; // 访问次序
    int begin;

    int flagSSTF=0;
    public int getG() {
        return g;
    }
    public int flagSSTF() {
        return flagSSTF;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getbegin() {
        return begin;
    }

    public void setbegin(int begin) {
        this.begin = begin;
    }
    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public int gettNum() {
        return tNum;
    }

    public void settNum(int tNum) {
        this.tNum = tNum;
    }

    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

   //FCFS
    public static ArrayList<CT> FCFS(ArrayList<CT> list) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);

            }
            int first=0;
//先来先服务算法计算
if(list != null)
    if(list.size()>0)
           first = data[0].begin;

            for (int i = 0; i < list.size(); i++) {
                data[i].dis = data[i].tNum - first >= 0 ? data[i].tNum - first : first - data[i].tNum;
                first = data[i].tNum;
            }

            for (int i = 0; i < list.size(); i++) {
                list1.add(data[i]);

            }
//返回最终结果
            return list1;
        }
        return list;
    }

    //SSTF
    public static ArrayList<CT> SSTF(ArrayList<CT> list) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];
            int first=0;
//获取初始位置
            if(list.size()>0)
                    first = list.get(0).begin;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).flagSSTF = 0;
                }

            int mm=0;
            int min=999;
            for (int j = 0; j < list.size(); j++) {
                mm=0;
                min=999;
                for (int i = 0; i < list.size(); i++) {
                    if (abs(list.get(i).tNum - first) < min && list.get(i).flagSSTF == 0) {
                        mm = i;
                        min = abs(list.get(i).tNum - first);
                    }
                }
                list.get(mm).flagSSTF = 1;
                list.get(mm).dis = abs(list.get(mm).tNum - first);
                data[j]=list.get(mm);
                first=list.get(mm).tNum;


            }
            for (int i = 0; i < list.size(); i++) {
                list1.add(data[i]);

            }
//返回最终结果
            return list1;
        } else   return list;
    }

    //SCAN
    public static ArrayList<CT> SCANup(ArrayList<CT> list) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
                data[i].flagSSTF=0;
            }
            int first=0;//循环算法计算
            if(list != null)
                if(list.size()>0)
                    first = data[0].begin;
            //排序
            for (int i = 0; i < list.size()-1; i++)
                for (int j = 0; j < list.size()-i-1; j++)
                {
                    if(data[j].tNum>data[j+1].tNum)
                    {
                        CT c=new CT ();
                        c=data[j];
                        data[j]=data[j+1];
                        data[j+1]=c;
                    }
                }
if(first>=data[list.size()-1].tNum)
{
    for (int i = list.size()-1; i >=0; i--) {

        data[i].dis = first-data[i].tNum;
        first = data[i].tNum;
        data[i].flagSSTF=1;
        list1.add(data[i]);
    }
}
else if(first<=data[0].tNum)
{
    for (int i = 0; i <list.size(); i++) {
        data[i].dis = data[i].tNum-first;
        first = data[i].tNum;
        data[i].flagSSTF=1;
        list1.add(data[i]);
    }
}
else
{
    int mn=0;
    for (int i = 0; i <list.size()-1; i++) {
        if (data[i].tNum <first && data[i + 1].tNum >=first) {
            mn = i;
            break;
        }
    }
for(int i=mn+1;i<list.size();i++)
{
    data[i].dis=data[i].tNum-first;
    data[i].flagSSTF=1;
    first = data[i].tNum;
    list1.add(data[i]);
}
    for (int i = list.size()-1; i >=0; i--) {
        if(data[i].flagSSTF==0)
        {
            data[i].dis = first-data[i].tNum;
            first = data[i].tNum;
            data[i].flagSSTF=1;
            list1.add(data[i]);
        }
    }
}
//返回最终结果
            return list1;
        }
        return list;
    }
    public static ArrayList<CT> SCANdown(ArrayList<CT> list) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];
            for (int i = 0; i < list.size(); i++) {

                data[i] = list.get(i);
                data[i].flagSSTF=0;
            }
            int first=0;//循环算法计算
            if(list != null)
                if(list.size()>0)
                    first = data[0].begin;
            //排序
            for (int i = 0; i < list.size()-1; i++)
                for (int j = 0; j < list.size()-i-1; j++)
                {
                    if(data[j].tNum>data[j+1].tNum)
                    {
                        CT c=new CT ();
                        c=data[j];
                        data[j]=data[j+1];
                        data[j+1]=c;
                    }
                }
            if(first>=data[list.size()-1].tNum)
            {
                for (int i = list.size()-1; i >=0; i--) {

                    data[i].dis = first-data[i].tNum;
                    first = data[i].tNum;
                    data[i].flagSSTF=1;
                    list1.add(data[i]);
                }
            }
            else if(first<=data[0].tNum)
            {
                for (int i = 0; i <list.size(); i++) {
                    data[i].dis = data[i].tNum-first;
                    first = data[i].tNum;
                    data[i].flagSSTF=1;
                    list1.add(data[i]);
                }
            }
            else
            {
                int mn=0;
                for (int i = 0; i <list.size()-1; i++) {
                    if (data[i].tNum <=first && data[i + 1].tNum > first) {
                        mn = i;
                        break;
                    }
                }
                for(int i=mn;i>=0;i--)
                {
                    data[i].dis=first-data[i].tNum;
                    data[i].flagSSTF=1;
                    first = data[i].tNum;
                    list1.add(data[i]);
                }
                for (int i =0;i< list.size(); i++) {
                    if(data[i].flagSSTF==0)
                    {
                        data[i].dis = data[i].tNum-first;
                        first = data[i].tNum;
                        data[i].flagSSTF=1;
                        list1.add(data[i]);
                    }
                }
            }
//返回最终结果
            return list1;
        }
        return list;
    }
    public static ArrayList<CT> CSCANup(ArrayList<CT> list) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];
            for (int i = 0; i < list.size(); i++) {

                data[i] = list.get(i);
                data[i].flagSSTF=0;
            }
            int first=0;//循环算法计算
            if(list != null)
                if(list.size()>0)
                    first = data[0].begin;
            //排序
            for (int i = 0; i < list.size()-1; i++)
                for (int j = 0; j < list.size()-i-1; j++)
                {
                    if(data[j].tNum>data[j+1].tNum)
                    {
                        CT c=new CT ();
                        c=data[j];
                        data[j]=data[j+1];
                        data[j+1]=c;
                    }
                }
            //至此以获得排序后的数组，一下开始为算法设计

            if(first>=data[list.size()-1].tNum)
            {

                for (int i = 0; i <list.size(); i++) {
                   if(i==0) data[i].dis = first-data[i].tNum;
                   else data[i].dis = data[i].tNum-first;
                    first = data[i].tNum;
                    data[i].flagSSTF=1;
                    list1.add(data[i]);
                }
            }
            else if(first<=data[0].tNum)
            {
                for (int i = 0; i <list.size(); i++) {
                    data[i].dis = data[i].tNum-first;
                    first = data[i].tNum;
                    data[i].flagSSTF=1;
                    list1.add(data[i]);
                }
            }
            else
            {
                int mn=0;
                for (int i = 0; i <list.size()-1; i++) {
                    if (data[i].tNum <=first && data[i + 1].tNum > first) {
                        mn = i;
                        break;
                    }
                }
                for(int i=mn;i<list.size();i++)
                {
                    if(i==mn&&data[i].tNum<first) continue;
                    else
                    {
                    data[i].dis=data[i].tNum-first>=0?data[i].tNum-first:first-data[i].tNum;
                    data[i].flagSSTF=1;
                    first = data[i].tNum;
                    list1.add(data[i]);
                    }

                }
                for (int i =0;i< list.size(); i++) {
                    if(data[i].flagSSTF==0)
                    {
                        data[i].dis = data[i].tNum-first>=0?data[i].tNum-first:first-data[i].tNum;
                        first = data[i].tNum;
                        data[i].flagSSTF=1;
                        list1.add(data[i]);
                    }
                }
            }
//返回最终结果
            return list1;
        }
        return list;
    }
    public static ArrayList<CT> CSCANdown(ArrayList<CT> list) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];
            for (int i = 0; i < list.size(); i++) {

                data[i] = list.get(i);
                data[i].flagSSTF=0;
            }
            int first=0;//循环算法计算
            if(list != null)
                if(list.size()>0)
                    first = data[0].begin;
            //排序
            for (int i = 0; i < list.size()-1; i++)
                for (int j = 0; j < list.size()-i-1; j++)
                {
                    if(data[j].tNum>data[j+1].tNum)
                    {
                        CT c=new CT ();
                        c=data[j];
                        data[j]=data[j+1];
                        data[j+1]=c;
                    }
                }
            //至此以获得排序后的数组，一下开始为算法设计

            if(first>=data[list.size()-1].tNum)
            {

                for (int i = 0; i <list.size(); i++) {
                    data[i].dis = first-data[i].tNum>=0?first-data[i].tNum:data[i].tNum-first;
                    first = data[i].tNum;
                    data[i].flagSSTF=1;
                    list1.add(data[i]);
                }
            }
            else if(first<=data[0].tNum)
            {
                for (int i = 0; i <list.size(); i++) {
                    data[i].dis = first-data[i].tNum>=0?first-data[i].tNum:data[i].tNum-first;
                    first = data[i].tNum;
                    data[i].flagSSTF=1;
                    list1.add(data[i]);
                }
            }
            else
            {
                int mn=0;
                for (int i = 0; i <list.size()-1; i++) {
                    if (data[i].tNum <=first && data[i + 1].tNum > first) {
                        mn = i;
                        break;
                    }
                }
                for(int i=mn;i>=0;i--)
                {
                    data[i].dis=first-data[i].tNum>=0?first-data[i].tNum:data[i].tNum-first;
                    data[i].flagSSTF=1;
                    first = data[i].tNum;
                    list1.add(data[i]);
                }
                for (int i =list.size()-1;i>=0; i--) {
                    if(data[i].flagSSTF==0)
                    {
                        data[i].dis = data[i].tNum-first>=0?data[i].tNum-first:first-data[i].tNum;
                        first = data[i].tNum;
                        data[i].flagSSTF=1;
                        list1.add(data[i]);
                    }
                }
            }
//返回最终结果
            return list1;
        }
        return list;
    }

    public static ArrayList<CT> TF(ArrayList<CT> list,int fft) {
        if (list != null) {
            ArrayList<CT> list1 = new ArrayList<CT>();
            //输入序列
            CT[] data = new CT[list.size()];

//获取初始位置

            for (int i = 0; i < list.size(); i++) {
                list.get(i).flagSSTF = 0;
            }

            int mm=0;
            int min=999;
            for (int j = 0; j < list.size(); j++) {
                mm=0;
                min=999;
                for (int i = 0; i < list.size(); i++) {
                    if (abs(list.get(i).tNum - fft) < min && list.get(i).flagSSTF == 0) {
                        mm = i;
                        min = abs(list.get(i).tNum - fft);

                    }
                }
                list.get(mm).flagSSTF = 1;
                list.get(mm).dis = abs(list.get(mm).tNum - fft);
                data[j]=list.get(mm);
                fft=list.get(mm).tNum;

            }
            for (int i = 0; i < list.size(); i++) {
                list1.add(data[i]);

            }
//返回最终结果
            CT ee=new CT();
            ee.tNum=fft;
            list1.add(ee);

            return list1;
        } else   return list;
    }

}

