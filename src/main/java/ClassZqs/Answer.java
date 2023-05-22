package ClassZqs;

public class Answer {
    public int id; //序号

    public int getId(){
        return id;
    }
    public int getNum() {
        return num;
    }



    public int[] getPage() {
        return page;
    }



    public int getCnt() {
        return cnt;
    }



    public boolean isMispage() {
        return mispage;
    }



    public int getPrepage() {
        return prepage;
    }



    public int getNowpage() {
        return nowpage;
    }




    public int num; //此次访问的页号
    public int[] page; //块表的情况
    public int cnt = 0;
    public boolean mispage; //是否缺页
    public int prepage; //被替换的页
    public int nowpage;// 替换的页
    public Answer(){

        prepage = -1;
        nowpage = -1;
        mispage = false;
    }
    public void Answer_init(){
        page = new int[cnt];
        for(int i=0; i<cnt; i++){
            page[i] = -1;
        }
    }


}
