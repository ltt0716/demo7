package ClassZqs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class class1 {
    public int number; //内存块数量
    public int p; //进程的总页数
    public  int [] page; //页面顺序
    public Answer[] answers;
    public void init(int num, int p){
        this.number = num;
        this.p = p;
        page = new int[p];
        answers = new Answer[p];
        for(int i=0; i<p; i++){
            answers[i] = new Answer();
        }
    }


    List<Integer> block = new ArrayList<>();

    public int mispage=0; //缺页的次数
    public int getpage = 0;





    public void FIFO(){ //双向链表
        block.clear();
//        for(int i=0; i<p; i++){
//            System.out.print(page[i]+" ");
//        }
//        System.out.println();
        for(int i=0; i<p; i++){

            answers[i].id = i+1;
            int x = page[i];
            answers[i].num = x;
            if(block.indexOf(x)==-1){//发生缺页
                answers[i].mispage = true;
                if(block.size()==number && block.size()!=0){ //块表已经满
                    answers[i].prepage = block.get(0);
                    block.remove(0);
                }
                block.add(x);
                answers[i].nowpage = x;
                mispage++;
            }else{
                getpage++;
            }

            answers[i].cnt = block.size();
            answers[i].Answer_init();
            for(int j=0; j< block.size(); j++){
                answers[i].page[j] = block.get(j);
//                System.out.print(block.get(j) + " ");
            }
//            System.out.println();
        }





    }

    public void LRU(){
//        那就是利用链表和hashmap。当需要插入新的数据项的时候，
//        如果新数据项在链表中存在（一般称为命中），则把该节点移到链表头部，
//        如果不存在，则新建一个节点，放到链表头部，若缓存满了，则把链表最后一个节点删除即可。
//        在访问数据的时候，如果数据项在链表中存在，则把该节点移到链表头部，否则返回-1。
//        这样一来在链表尾部的节点就是最近最久未访问的数据项。
        block.clear();
        for(int i=0; i<p; i++){
            answers[i].id = i+1;
            int x = page[i];
            answers[i].num = x;
            if(block.indexOf(x)==-1){ //发生缺页
                answers[i].mispage = true;
                if(block.size()==number&&block.size()>0){
                    answers[i].prepage = block.get(0);
                    block.remove(0);//删除队列的头部
                }
                answers[i].nowpage = x;
                block.add(x);
                mispage++;
            }else{
                block.remove((Integer) x);//删除x
                block.add(x);
                getpage++;
            }


            answers[i].cnt = block.size();
            answers[i].Answer_init();
            for(int j=0; j< block.size(); j++){
                answers[i].page[j] = block.get(j);
//                System.out.print(block.get(j) + " ");
            }
        }
    }


    HashMap<Integer,Integer> map = new HashMap<>();
    public void LFU(){
        //做一个队列 权重从队头到队尾  从 小 到 大
        // 越靠近尾 越不容易被删
        block.clear();
        map.clear();
        for(int i=0; i<p; i++){
            answers[i].id = i+1;
            answers[i].num = page[i];
            int x = page[i];
            if(!block.contains(x)){//发生缺页，块表里面没有
                if(block.size()==number){//块满
                    answers[i].prepage = block.get(0);
                    map.put(block.get(0),0);
                    block.remove(0);
                }
                answers[i].nowpage = x;
                block.add(0,x);
                if(map.get(x)==null){
                    map.put(x,1);
                }else{
                    map.put(x,map.get(x)+1);
                }
                mispage++;
            }else{
                block.remove((Integer) x);
                if(map.get(x)==null){
                    map.put(x,1);
                }else{
                    map.put(x,map.get(x)+1);
                }
                int tp = 0;
                while(tp<block.size()){
                    if(map.get(x)>=map.get(block.get(tp))){
                        break;
                    }
                    tp++;
                }
                block.add(tp,x);

                getpage++;
            }


            answers[i].cnt = block.size();
            answers[i].Answer_init();
            for(int j=0; j< block.size(); j++){
                answers[i].page[j] = block.get(j);
//                System.out.print(block.get(j) + " ");
            }
        }
    }




    public void Clock(){
        block.clear();
        map.clear();
        int idx=0;
        for(int i=0; i<p; i++){

            answers[i].id = i+1;
            answers[i].num = page[i];
            int x = page[i];
            if(!block.contains(x)){ //发生缺页
                answers[i].mispage = true;
                if(block.size()==number){//块满
                    while(!block.contains(x)){
                        int tp = block.get(idx);
                        if(map.get(tp)==1){
                            map.put(tp,0);
                        }else{
                            answers[i].prepage = block.get(idx);
                            block.set(idx, x);
                            map.put(x,1);

                        }
                        idx++;
                        if(idx==number) idx=0;
                    }
                }else{
                    block.add(x);
                    map.put(x,1);
                    idx++;
                    if(idx==number) idx = 0;
                }
                answers[i].nowpage = x;
                mispage++;
            }else{
                getpage++;
            }
            answers[i].cnt = block.size();
            answers[i].Answer_init();
            for(int j=0; j< block.size(); j++){
                answers[i].page[j] = block.get(j);
//                System.out.print(block.get(j) + " ");
            }
        }

    }
//     public static void main(String[] args) {
//         HashMap<Integer,Integer> map = new HashMap<>();
//         map.put(3,0);
//         map.put(3,map.get(3)+1);
//         System.out.println(map.get(3));
//    }
}
