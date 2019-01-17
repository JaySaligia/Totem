package daixiahu.totem.backstage;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;


public class Sysclass {

    public void init(Context cxt){//初始化系统表
            SharedPreferences.Editor editor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE).edit();
            editor.putString("idable", "0");
            editor.putInt("idcount", 0);
            editor.apply();
    }

    String getid(Context cxt){//得到新的类可用id
        SharedPreferences looker = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE);
        String[] tmp = looker.getString("idable", "0").split("-@-");
        return tmp[0];
    }

    void updatesystotal(Context cxt){//每次新建一个类后修改系统表
        SharedPreferences looker = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE);
        String[] tmp = looker.getString("idable", "0").split("-@-");
        String idcount = looker.getInt("idcount", 0)+"";
        SharedPreferences.Editor editor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE).edit();
        if (tmp[0].equals(idcount)){//没有墓碑id
            editor.putString("idable", (Integer.parseInt(tmp[0]) + 1)+"");
        }
        else
        {
            String idablenew = "";
            for (int i = 1; i< tmp.length; i ++){
                idablenew += (i == tmp.length - 1)?tmp[i]:tmp[i] + "-@-";//形如"1-@-2-@-3"
            }
        }
        editor.putInt("idcount", Integer.parseInt(idcount) + 1);
        editor.apply();
    }

    int checkclassname(Context cxt, String name, int id){//检查类名称是否匹配
        String Oid = id+"";
        SharedPreferences editor = cxt.getSharedPreferences("sysclass" + Oid, Context.MODE_PRIVATE);
        String classname = editor.getString("className", "");
        if (classname.equals(name)) return 1;
        return 0;
    }

    int getclassOid(Context cxt, String name){//由类名字得到id
        SharedPreferences looker = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE);
        int idmax = looker.getInt("idcount", 0);
        for ( int i = 0; i< idmax; i++) {
            if (checkclassname(cxt, name, i) == 1)
                return i;
        }
        return -1;//该类不存在
    }

    public int newsysclass(Context cxt, String classname, String attr[], int type[]){
        String classOid = getid(cxt);
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        editor.putString("classOid",classOid);
        editor.putString("className", classname);
        editor.putInt("classType", 0);
        editor.putString("classLink", "");
        int attrlen = attr.length;
        editor.putInt("attrReal_num", attrlen);
        String attrReal_name = makestr_s(attr);
        editor.putString("attrReal_name", attrReal_name);
        String attrReal_type = makestr_i(type);
        editor.putString("attrReal_type", attrReal_type);
        editor.putString("tupleAva","0");
        editor.putInt("tupleCount", 0);
        editor.apply();
        updatesystotal(cxt);
        return 1;
    }

    void createlinkclass(Context cxt, String sysid, String proxyid, String attr){//新建双向指针表
        SharedPreferences.Editor editor = cxt.getSharedPreferences("linkclass" + sysid + "-" + proxyid, Context.MODE_PRIVATE).edit();
        editor.putString("Attr", attr);
        editor.putInt("Count", 0);
        editor.apply();
        updatesysclass(cxt, sysid, proxyid);
    }

    String makestr_s(String[] strings){
        int len = strings.length;
        String ret = "";
        for (int i = 0; i < len; i ++)
            ret += (i == len - 1) ? strings[i]: strings[i] + "-@-";
        return ret;
    }

    String makestr_i(int[] strings){
        int len = strings.length;
        String ret = "";
        for (int i = 0; i < len; i ++)
            ret += (i == len - 1) ? strings[i] + "": strings[i] + "" + "-@-";
        return ret;
    }

    public int inserttuple(Context cxt, String classname, String[] tuple,int insert_type){//插入元组,insert_type为插入类型，-1为源类，其余为代理类中对象对应在源类中的id
        int tmp = getclassOid(cxt, classname);
        if ( tmp == -1)
            return -1;//该类不存在
        String classOid = tmp+"";
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String []tupleava = looker.getString("tupleAva", "0").split("-@-");
        String tuplecount = looker.getInt("tupleCount", 0) + "";
        String tupleid = tupleava[0];
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        String tuplestr = makestr_s(tuple);
        editor.putString("tuple" + tupleid, tuplestr);//新建键值对来储存元组
        if (tupleava.length == 1){//没有墓碑id
            editor.putString("tupleAva", (Integer.parseInt(tupleava[0]) + 1)+"");
            editor.putInt("tupleCount", Integer.parseInt(tuplecount) + 1);
        }
        else
        {
            String tupleavanew = "";
            for (int i = 1; i< tupleava.length; i ++){
                tupleavanew += (i == tupleava.length - 1)?tupleava[i]:tupleava[i] + "-@-";//形如"1-@-2-@-3"
                editor.putString("tupleAva", tupleavanew);
            }
        }

        editor.apply();
        if (insert_type >= 0){//如果是代理类，要修改连接表信息
            String linkclassid = looker.getString("classLink", "");
            SharedPreferences.Editor linkeditor = cxt.getSharedPreferences("linkclass" + linkclassid + "-" + classOid, Context.MODE_PRIVATE).edit();
            linkeditor.putString("src" + insert_type+"", tupleid);
            linkeditor.putString("proxy" + tupleid, insert_type+"");
            linkeditor.apply();
        }
        return 1;
    }

    void updatesysclass(Context cxt, String sysid, String proxyid){//更新源类的link信息
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + sysid, Context.MODE_PRIVATE);
        String classlink = looker.getString("classLink", "");
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + sysid, Context.MODE_PRIVATE).edit();
        if (classlink.equals(""))
            editor.putString("classLink", proxyid);
        else
            editor.putString("classLink", classlink + "-@-" + proxyid);
        editor.apply();
    }

    String getsrctype(Context cxt, String classOid, String[] attr_v){
        String type = "";
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String[] attr_r = looker.getString("attrReal_name", "").split("-@-");
        String[] type_r = looker.getString("attrReal_type", "").split("-@-");
        int len = attr_r.length;
        int len2 = attr_v.length;
        int count = 0;
        String[] type_v = new String[len2];
        for(int i = 0; i < len; i ++){
            if (Arrays.asList(attr_v).contains(attr_r[i])) {
                type_v[count] = type_r[i];
                count ++;
            }
        }
        return makestr_s(type_v);
    }

    public int newproxyclass(Context cxt, String classname, String sysclassname,String[] attr_v, String[] attr_rename, String boolstr){
        String classOid = getid(cxt);
        String sysclassOid = getclassOid(cxt, sysclassname)+"";
        int attrlen_v = attr_v.length;
        createlinkclass(cxt, sysclassOid, classOid, makestr_s(attr_v));
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        editor.putString("classOid",classOid);
        editor.putString("className", classname);
        editor.putInt("classType", 1);
        editor.putString("classLink", sysclassOid);//link到源类的id
        //虚属性
        editor.putInt("attrVirtual_num", attrlen_v);
        String attrVirtual_name = makestr_s(attr_rename);
        editor.putString("attrVirtual_name", attrVirtual_name);
        //String attrVirtual_type = makestr_i(type_v);
        editor.putString("attrVirtual_type", getsrctype(cxt, sysclassOid, attr_v));
        editor.putString("tupleAva","0");
        editor.putInt("tupleCount", 0);
        editor.apply();
        updatesystotal(cxt);
        insertdeputy(cxt,sysclassOid,classname,attr_v, boolstr);

        return 1;
    }

    /*考虑实属性
    public int newproxyclass(Context cxt, String classname, String sysclassname,String[] attr_v, String[] attr_rename, String[] attr_r, int type_r[]){
        String classOid = getid(cxt);
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        editor.putString("classOid",classOid);
        editor.putString("className", classname);
        editor.putInt("classType", 1);
        String sysclassOid = getclassOid(cxt, sysclassname)+"";
        editor.putString("classLink", sysclassOid);//link到源类的id

        int attrlen_v = attr_v.length;
        int attrlen_r = attr_r.length;
        //虚属性
        editor.putInt("attrVirtual_num", attrlen_v);
        String attrVirtual_name = makestr_s(attr_rename);
        editor.putString("attrVirtual_name", attrVirtual_name);
        //String attrVirtual_type = makestr_i(type_v);
        editor.putString("attrVirtual_type", getsrctype(cxt, sysclassOid, attr_v));
        //实属性
        if (attrlen_r == 0){//没有实属性
            editor.putInt("attrReal_num", 0);
            editor.putString("attrReal_name", "");
            editor.putString("attrReal_type", "");
        }
        else {//具有实属性
            editor.putInt("attrReal_num", attrlen_r);
            String attrReal_name = makestr_s(attr_r);
            editor.putString("attrReal_name", attrReal_name);
            String attrReal_type = makestr_i(type_r);
            editor.putString("attrReal_type", attrReal_type);
        }
        editor.putString("tupleAva","0");
        editor.putInt("tupleCount", 0);
        editor.apply();
        createlinkclass(cxt, sysclassOid, classOid, makestr_s(attr_v));
        updatesystotal(cxt);
        return 1;
    }
    */
    public int deltuple(Context cxt, String classOid, String tuplenum){
        //删除源类的元组
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String tupleava = looker.getString("tupleAva", "");
        int tuplecount = looker.getInt("tupleCount", 0);
        String[] link = looker.getString("classLink", "").split("-@-");
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        editor.remove("tuple" + tuplenum);//从源类删除对象
        editor.putString("tupleAva", tuplenum + "-@-" + tupleava);//增加墓碑元组id
        //editor.putInt("tupleCount", tuplecount - 1);//元组总数-1
        editor.apply();
        //删除link表中的元素和对应代理类中的代理对象
        if (link[0].equals(""))
            return 0;//没有link信息
        for (int i = 0; i < link.length; i ++){
            SharedPreferences linklooker = cxt.getSharedPreferences("linkclass" + classOid + "-" + link[i], Context.MODE_PRIVATE);
            SharedPreferences.Editor linkeditor = cxt.getSharedPreferences("linkclass" + classOid + "-" + link[i], Context.MODE_PRIVATE).edit();
            String proxytupleid = linklooker.getString("src" + tuplenum, "");
            if (!proxytupleid.equals("")){
                linkeditor.remove("src" + tuplenum);
                linkeditor.remove("proxy" + proxytupleid);//删除“双向指针”
                linkeditor.apply();
                SharedPreferences proxylooker = cxt.getSharedPreferences("sysclass" + link[i], Context.MODE_PRIVATE);
                SharedPreferences.Editor proxyeditor = cxt.getSharedPreferences("sysclass" + link[i], Context.MODE_PRIVATE).edit();
                String proxytupleava = proxylooker.getString("tupleAva", "");
                int proxytuplecount = proxylooker.getInt("tupleCount", 0);
                proxyeditor.remove("tuple" + proxytupleid);//从代理类中删除代理对象；
                proxyeditor.putString("tupleAva", proxytupleid + "-@-" + proxytupleava);//增加墓碑元组id
                proxyeditor.putInt("tupleCount", proxytuplecount - 1);//元组总数-1
                proxyeditor.apply();
            }
            else
                linkeditor.apply();
        }
        return 1;
    }

    public int delclass(Context cxt, String classname){//删除源类
        String classOid = getclassOid(cxt, classname)+"";

        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int classtype = looker.getInt("classType", 0);

        String[] link = looker.getString("classLink","").split("-@-");//获取link信息
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        if (classtype == 0) {
            if (!link[0].equals("")) {
                for (int i = 0; i < link.length; i++) {
                    SharedPreferences.Editor linkeditor = cxt.getSharedPreferences("linkclass" + classOid + "-" + link[i], Context.MODE_PRIVATE).edit();//清空link类
                    linkeditor.clear();
                    linkeditor.apply();
                    SharedPreferences.Editor proxyeditor = cxt.getSharedPreferences("sysclass" + link[i], Context.MODE_PRIVATE).edit();//清空代理类
                    proxyeditor.clear();
                    proxyeditor.apply();
                }

            }
        }
        editor.clear();//清空源类
        editor.apply();
        //修改系统表信息
        SharedPreferences syslooker = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE);
        String idable = syslooker.getString("idable","");
        int idcount = syslooker.getInt("idcount", 0);
        SharedPreferences.Editor syseditor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE).edit();
        syseditor.putString("idable", classOid + "-@-" + idable);//将删除的类id加入墓碑中
        syseditor.putInt("idcount", idcount - 1);//类总数-1
        syseditor.apply();
        return 1;
    }

    public String choosedeputytuple(Context cxt, String classOid, String tupleid,int[] attrid){//给代理类选择特定元组
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String[] tupleattr = looker.getString("tuple" + tupleid, "").split("-@-");
        String tupleret = "";
        for(int i = 0; i < attrid.length; i ++){
            tupleret+= tupleattr[attrid[i]] + "-@-";
        }
        return tupleret.substring(0, tupleret.length()-3);
    }

    public String choosetuple(Context cxt, String classOid, String tupleid,int[] attrid){//选择特定元组
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String[] tupleattr = looker.getString("tuple" + tupleid, "").split("-@-");
        String tupleret = "";
        for(int i = 0; i < attrid.length; i ++){
            tupleret+= tupleattr[attrid[i]] + " ";
        }
        return tupleret;
    }

    int getindex(String a, String[] b){//给下面函数服务的，得到指定字符串再字符数组中的序号
        for(int i = 0; i < b.length; i++){
            if (a.equals(b[i])) return i;
        }
        return 0;
    }

    Boolean booltest(String[] tupleattr, String attr_val, String val,String[] attr_name, String[] attr_type, int cal_type){//参数：指定的元组，符号左边（待测属性），符号右边（待测值），属性名称，属性类型，符号类型
        Boolean flag = false;
        int index = 0;
        switch (cal_type){
            case 0: //=
                    index = getindex(attr_val, attr_name);
                    if (attr_type[index].equals("0")){//string
                        if (tupleattr[index].equals(val))
                            return true;
                    }
                    else{//int
                        if (Integer.parseInt(tupleattr[index]) == Integer.parseInt(val))
                            return true;
                    }
                    break;
            case 1://>
                index = getindex(attr_val, attr_name);
                if (Integer.parseInt(tupleattr[index]) > Integer.parseInt(val))
                    return true;
                break;
            case 2://<
                index = getindex(attr_val, attr_name);
                if (Integer.parseInt(tupleattr[index]) < Integer.parseInt(val))
                    return true;
                break;
        }
        return false;
    }

    public Boolean booleaneval(Context cxt, String classOid, String tupleid, String boolstr){//对where表达式子中的bool表达式求值
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int class_type = looker.getInt("classType", 0);
        String[] attr_name;
        String[] attr_type;
        if (class_type == 0) {
            attr_name = looker.getString("attrReal_name", "").split("-@-");
            attr_type = looker.getString("attrReal_type", "").split("-@-");
        }
        else{
            attr_name = looker.getString("attrVirtual_name", "").split("-@-");
            attr_type = looker.getString("attrVirtual_type", "").split("-@-");
        }
        String[] tupleattr = looker.getString("tuple" + tupleid, "").split("-@-");
        String booltmp = boolstr.replace("(", "");
        booltmp = booltmp.replace(")", "");
        String[] boolsplit = booltmp.split(" *(AND|OR) *");
        int cal_type = 0;
        for (String b: boolsplit) {
            String[] tmp;
            if (b.contains("="))
                tmp = b.split(" *= *");
            else if (b.contains(">")){
                tmp = b.split(" *> *");
                cal_type = 1;
            }
            else {
                tmp = b.split(" *< *");
                cal_type = 2;
            }

            if (booltest(tupleattr, tmp[0], tmp[1], attr_name, attr_type, cal_type))
                boolstr = boolstr.replace(b, " T ");
            else
                boolstr = boolstr.replace(b, " F ");

        }

        return eval(boolstr);
    }

    int insertdeputy(Context cxt, String classOid, String proxyclassname, String[] attr_src, String boolstr){
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int classtype = looker.getInt("classType", 0);
        String[] attr_name = (classtype == 0)?looker.getString("attrReal_name", "").split("-@-"):looker.getString("attrVirtual_name","").split("-@-");
        int tuplecount = looker.getInt("tupleCount", 0);
        int[] attrindex = new int[attr_src.length];
        for (int i = 0; i < attr_src.length; i++){
            attrindex[i] = getindex(attr_src[i], attr_name);
        }
        String tmp = "";

        for (int i = 0; i < tuplecount; i ++){
            if(booleaneval(cxt, classOid, i+"", boolstr)) {
                tmp = choosedeputytuple(cxt, classOid, i + "", attrindex);
                String[] tmpstr = tmp.split("-@-");
                inserttuple(cxt, proxyclassname, tmpstr, i);
            }
        }
        return 1;
    }



    public String[] showselecttuple(Context cxt, String classOid, String[] attr_show, String boolstr){
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int classtype = looker.getInt("classType", 0);
        String[] attr_name = (classtype == 0)?looker.getString("attrReal_name", "").split("-@-"):looker.getString("attrVirtual_name","").split("-@-");
        int tuplecount = looker.getInt("tupleCount", 0);
        int[] attrindex = new int[attr_show.length];
        for (int i = 0; i < attr_show.length; i++){
            attrindex[i] = getindex(attr_show[i], attr_name);
        }
        String tmp = "";
        for (int i = 0; i < attr_show.length; i ++){
            tmp += attr_show[i] + " ";
        }
        tmp += "-@-";
        for (int i = 0; i < tuplecount; i ++){
            if(booleaneval(cxt, classOid, i+"", boolstr))
              tmp += choosetuple(cxt, classOid, i+"", attrindex) + "-@-";
        }
        return tmp.split("-@-");

    }

    int getclasstype(Context cxt, String classname){
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + getclassOid(cxt, classname)+"", Context.MODE_PRIVATE);
        return looker.getInt("classType",0);
    }

    String docrossselect(Context cxt,String[] crosspath,String tupleid) {
        String ret;
        int classtype = getclasstype(cxt, crosspath[0]);
        if (classtype == 0) {//起点是源类
            SharedPreferences linklooker = cxt.getSharedPreferences("linkclass" + getclassOid(cxt, crosspath[0]) + "" + "-" + getclassOid(cxt, crosspath[1] + ""), Context.MODE_PRIVATE);
            ret = linklooker.getString("src" + tupleid,"none");
            }
            else {//起点是代理类
            SharedPreferences linklooker = cxt.getSharedPreferences("linkclass" + getclassOid(cxt, crosspath[1]) + "" + "-" + getclassOid(cxt, crosspath[0] + ""), Context.MODE_PRIVATE);
            ret = linklooker.getString("proxy" + tupleid,"none");
        }
        if (crosspath.length > 2){
            SharedPreferences linklooker = cxt.getSharedPreferences("linkclass" + getclassOid(cxt, crosspath[1]) + "" + "-" + getclassOid(cxt, crosspath[2] + ""), Context.MODE_PRIVATE);
            ret = linklooker.getString("src" + tupleid,"none");

        }
            return ret;
        }


    String[] showcrosstuple(Context cxt, String classname,String boolstr,String attr,String[] crosspath){
        String tuple = "";
        String classOid = getclassOid(cxt, classname)+"";
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int tuplecount = looker.getInt("tupleCount", 0);
        for (int i = 0; i < tuplecount; i ++){
            if(booleaneval(cxt, classOid, i+"", boolstr)) {
                 String tmp = docrossselect(cxt, crosspath, i + "");
                 if(!tmp.equals("none"))
                    tuple += tmp + "-@-";
                    //tuple += i+"" + "-@-";
            }
        }

        String[] tuplegroup = tuple.split("-@-");//得到了在起点符合要求的元组
        
        SharedPreferences targetlooker = cxt.getSharedPreferences("sysclass" + getclassOid(cxt,crosspath[crosspath.length-1])+"", Context.MODE_PRIVATE);
        int classtype = looker.getInt("classType", 0);
        String[] attr_name = (classtype == 0)?looker.getString("attrReal_name", "").split("-@-"):looker.getString("attrVirtual_name","").split("-@-");
        String temp = attr;
        temp += "-@-";
        int[] attrindex = new int[1];
        attrindex[0] = getindex(attr,attr_name);
        for (int i = 0; i < tuplegroup.length; i ++){
                temp += choosetuple(cxt, classOid, i+"", attrindex) + "-@-";
        }
        return temp.split("-@-");

    }


    public int delselecttuple(Context cxt, String classOid, String boolstr){
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int classtype = looker.getInt("classType", 0);
        int tuplecount = looker.getInt("tupleCount", 0);
        for (int i = 0; i < tuplecount; i ++){
            if(booleaneval(cxt, classOid, i+"", boolstr))
                deltuple(cxt, classOid, i+"");
        }
        return 1;
    }

    public String[] showalltuple(Context cxt, String classOid, String[] attr_show){
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        int classtype = looker.getInt("classType", 0);
        String[] attr_name = (classtype == 0)?looker.getString("attrReal_name", "").split("-@-"):looker.getString("attrVirtual_name","").split("-@-");
        int tuplecount = looker.getInt("tupleCount", 0);
        int[] attrindex = new int[attr_show.length];
        for (int i = 0; i < attr_show.length; i++){
            attrindex[i] = getindex(attr_show[i], attr_name);
        }
        String tmp = "";
        for (int i = 0; i < attr_show.length; i ++){
            tmp += attr_show[i] + " ";
        }
        tmp += "-@-";
        for (int i = 0; i < tuplecount; i ++){
             tmp += choosetuple(cxt, classOid, i+"", attrindex) + "-@-";
        }
        return tmp.split("-@-");
    }

    private ArrayList<String> infixToPostfix(ArrayList<String> infix) {
        Stack<String> ops = new Stack<String>();
        ArrayList<String> postfix = new ArrayList<String>();

        for (String s : infix) {
            if (s.equals("(")) {
                ops.push(s);
            } else if (s.equals("AND") || s.equals("OR")) {
                if (!ops.empty() && priority(s) <= priority(ops.peek())) {
                    while (!ops.empty() && !ops.peek().equals("(")) {
                        postfix.add(ops.pop());
                    }
                    ops.push(s);
                } else {
                    ops.push(s);
                }
            } else if (s.equals(")")) {
                while (!ops.empty()) {
                    String v = ops.pop();
                    if (v.equals("(")) {
                        break;
                    } else {
                        postfix.add(v);
                    }
                }
            } else if (s.equals("T")) {
                postfix.add(s);
            } else if (s.equals("F")) {
                postfix.add(s);
            }
        }

        // pop all remaining OPs in the stack
        while (!ops.empty()) {
            postfix.add(ops.pop());
        }

        return postfix;
    }

    // Evaluate the boolean expr by postfix
    private boolean eval(String bexpr) {
        Stack<Boolean> vals = new Stack<Boolean>();
        ArrayList<String> infix = new ArrayList<String>();
        for (String s : bexpr.split(" +")) {
            infix.add(s);
        }
        ArrayList<String> postfix = infixToPostfix(infix);

        // debug
        for (String s : postfix) {
            System.out.print(s+" ");
        }
        System.out.println();

        for (String s : postfix) {
            if (s.equals("AND")) {
                boolean v = vals.pop();
                v = vals.pop() && v;
                vals.push(v);
            } else if (s.equals("OR")) {
                boolean v = vals.pop();
                v = vals.pop() || v;
                vals.push(v);
            } else {
                if (s.equals("T")) {
                    vals.push(true);
                } else if (s.equals("F")) {
                    vals.push(false);
                } else {
                    // Error
                }
            }
        }

        // If everything goes right, the result will be the only element in stack.
        return vals.pop();
    }

    private int priority(String op) {
        if (op.equals("AND")) {
            return 2;
        } else if (op.equals("OR")) {
            return 1;
        } else if (op.equals("(")) {
            return 0;
        } else {
            return -1;
        }
    }

    //最上层的接口
    public int trans_newsrcclass(Context cxt, String[] element){//新建源类
        String classname = "";
        String[] attr_name = new String[10];
        int[] attr_type = new int[10];
        for (int i = 1; i < element.length; i ++){
            String[] direct = element[i].split(" *: *");
            switch (direct[0]){
                case "classname":
                    classname = direct[1];
                case "attr":
                     attr_name = direct[1].substring(0, direct[1].length()-3).split("-@-");
                case "type":
                    String[] typestr = direct[1].toLowerCase().split("-@-");
                    attr_type = new int[typestr.length];
                    for (int index = 0 ;index < typestr.length; index++) {
                        if (typestr[index].equals("int"))
                            attr_type[index] = 1;
                        else
                            attr_type[index] = 0;
                    }
            }
        }
        newsysclass(cxt, classname, attr_name, attr_type);
        return 1;
    }

    public int trans_inserttuple(Context cxt, String[] element){//源类插入元组
        String[] direct = element[1].split(" *: *");
        String classname = direct[1];
        String[] tuple= element[2].split(" *: *")[1].split("-@-");
        inserttuple(cxt, classname, tuple, -1);
        return 1;
    }

    public String[] trans_selecttuple(Context cxt, String[] element){//选择元组
        String[] direct = element[2].split(" *: *");
        String classOid = getclassOid(cxt, direct[1])+"";
        String[] attr = element[1].split(" *: *")[1].split("-@-");
        String cond = element[3].split(" *: *")[1];
        return showselecttuple(cxt, classOid, attr, cond);

    }

    public int trans_deletetuple(Context cxt, String[] element){//删除元组
        String[] direct = element[1].split(" *: *");
        String classOid = getclassOid(cxt, direct[1])+"";
        String cond = element[2].split(" *: *")[1];
        delselecttuple(cxt, classOid, cond);
        return 1;
    }

    public int trans_deleteclass(Context cxt, String[] element){//删除类
        String[] direct = element[1].split(" *: *");
        delclass(cxt, direct[1]);
        return 1;
    }

    public int trans_newdeputyclass(Context cxt, String[] element){//新建代理类
        String classname = element[1].split(" *: *")[1];
        String srcclassname = element[2].split(" *: *")[1];
        String cond = element[3].split(" *: *")[1];
        String tmp = element[4].split(" *: *")[1];
        String[] attr_src = tmp.substring(0, tmp.length()-3).split("-@-");
        tmp = element[5].split(" *: *")[1];
        String[] attr_deputy = tmp.split("-@-");
        newproxyclass(cxt,classname,srcclassname,attr_src,attr_deputy,cond);
        return 1;
    }

    public String[] trans_selectdeputytuple(Context cxt, String[] element){//跨类查询
        String[] crosspath = element[1].split(" *: *")[1].split("-@-");
        String attr = element[2].split(" *: *")[1];
        String classname = element[3].split(" *: *")[1];
        String cond = element[4].split(" *: *")[1];


        return showcrosstuple(cxt, classname, cond, attr, crosspath);
    }

}
