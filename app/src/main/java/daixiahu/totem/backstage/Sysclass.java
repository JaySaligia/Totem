package daixiahu.totem.backstage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;


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

    public int inserttuple(Context cxt, String classname, String[] tuple){//插入元组
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
        if (tupleava[0].equals(tuplecount)){//没有墓碑id
            editor.putString("tupleAva", (Integer.parseInt(tupleava[0]) + 1)+"");
        }
        else
        {
            String tupleavanew = "";
            for (int i = 1; i< tupleava.length; i ++){
                tupleavanew += (i == tupleava.length - 1)?tupleava[i]:tupleava[i] + "-@-";//形如"1-@-2-@-3"
            }
        }
        editor.putInt("tupleCount", Integer.parseInt(tuplecount) + 1);
        editor.apply();
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

    public int newproxyclass(Context cxt, String classname, String sysclassname,String[] attr_v, String[] attr_rename, String[] attr_r, int type_r[]){
        String classOid = getid(cxt);
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        editor.putString("classOid",classOid);
        editor.putString("className", classname);
        editor.putInt("classType", 1);
        String sysclassOid = getclassOid(cxt, sysclassname)+"";
        editor.putString("classLink", sysclassOid);//link到源类的id
        /*
        建立link类
        */
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
    public int deltuple(Context cxt, String classOid, String tuplenum){
        //删除源类的元组
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String tupleava = looker.getString("tupleAva", "");
        int tuplecount = looker.getInt("tupleCount", 0);
        String[] link = looker.getString("classLink", "").split("-@-");
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        editor.remove("tuple" + tuplenum);//从源类删除对象
        editor.putString("tupleAva", tuplenum + "-@-" + tupleava);//增加墓碑元组id
        editor.putInt("tupleCount", tuplecount - 1);//元组总数-1
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

    public int delsrcclass(Context cxt, String classname){//删除源类
        String classOid = getclassOid(cxt, classname)+"";
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String[] link = looker.getString("classLink","").split("-@-");//获取link信息
        SharedPreferences.Editor editor = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE).edit();
        for (int i = 0; i< link.length; i++){
            SharedPreferences.Editor linkeditor = cxt.getSharedPreferences("linkclass" + classOid + "-" + link[i], Context.MODE_PRIVATE).edit();//清空link类
            linkeditor.clear();
            linkeditor.apply();
            SharedPreferences.Editor proxyeditor = cxt.getSharedPreferences("sysclass" + link[i], Context.MODE_PRIVATE).edit();//清空代理类
            proxyeditor.clear();
            proxyeditor.apply();
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
    public String choosetuple(Context cxt, String classOid, String tupleid,int[] attrid){//选择特定元组
        SharedPreferences looker = cxt.getSharedPreferences("sysclass" + classOid, Context.MODE_PRIVATE);
        String[] tupleattr = looker.getString("tuple" + tupleid, "").split("-@-");
        String tupleret = "";
        for(int i = 0; i < attrid.length; i ++){
            tupleret+= "\t" + tupleattr[attrid[i]];
        }
        return tupleret;
    }

}
