package daixiahu.totem.backstage;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


class TableInfo{
    String tableName;//name of table
    int tableOid;//table id
    int tupleNum;//num of tuple
    String[] tupleName = new String[10];//
    int[] tupleType = new int[10];//Type of tuple,0 is string,1 is int,default is 0
}

public class DatabaseManager{
    String dbName;//name of db
    int dbOid;//id
    int tableNum;//num of table
    SharedPreferences.Editor dbeditor;
    SharedPreferences dblooker;
    TableInfo[] table = new TableInfo[10];//max table num is 10

    void initsystotal(Context cxt){//初始化系统表
        SharedPreferences.Editor editor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE).edit();
        editor.putInt("idble", 0);
        editor.putInt("idmax", 0);
        editor.apply();
    }

    String getdatabaseid(Context cxt){//得到数据库可用id
        SharedPreferences editor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE);
        int dbidint = editor.getInt("idble", 0);
        String dbid = ((Integer) dbidint).toString();
        return dbid;
    }

    void createdbsys(Context cxt, String dbid, String databasename){//新建数据库表
        SharedPreferences.Editor editor = cxt.getSharedPreferences("db"+dbid+"Tdb", Context.MODE_PRIVATE).edit();
        editor.putString("dbName", databasename);
        editor.putInt("state", 1);
        editor.putInt("tbNum", 0);
        editor.putString("tb", "");
        editor.apply();
    }

    void editsystotal(Context cxt, String dbid){//修改系统表
        SharedPreferences.Editor editor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE).edit();
        editor.putInt("idble", Integer.parseInt(dbid) + 1);
        editor.putInt("idmax", Integer.parseInt(dbid) + 1);
        editor.apply();
    }

    public int newdatabase(Context cxt, String databasename){//新建数据库
        String dbid = getdatabaseid(cxt);
        createdbsys(cxt, dbid, databasename);
        editsystotal(cxt, dbid);
        return 1;
    }

    int checkdbname(Context cxt, String name, int id){//检查数据库名称是否正确
        String Oid = id+"";
        SharedPreferences editor = cxt.getSharedPreferences("db" + Oid + "Tdb", Context.MODE_PRIVATE);
        if (editor.getInt("state",0) == 0)
            return 0;
        String dbname = editor.getString("dbName", "");
        if (dbname == name) return 1;
        return 0;
    }

    int getdbOid(Context cxt, String name ){//由数据库名字得到id
        if (name == null)
                return -1;//未选择任何数据库，视为新建数据库
        SharedPreferences editor = cxt.getSharedPreferences("systotal", Context.MODE_PRIVATE);
        int idmax = editor.getInt("idmax", 0);
        for ( int i = 0; i< idmax; i++){
            if (checkdbname(cxt, name, i) == 1)
                return i;
        }
        return -2;//该数据库不存在
    }

    public int choosedatabase(Context cxt, String name){//根据数据库名称选择数据库
        int label;
        label = getdbOid(cxt, name);
        switch (label){
            case -1: return -1;
            case -2: return -2;
            default: {
                dbeditor = cxt.getSharedPreferences("db" + label + "Tdb", Context.MODE_PRIVATE).edit();
                dblooker = cxt.getSharedPreferences("db" + label + "Tdb", Context.MODE_PRIVATE);
            }
        }
        return 1;
    }

    public int newtable(Context cxt, String name, String[] attr, int[] type){//新建表
        int count = dblooker.getInt("tbNum", 0);
        String tbstate = dblooker.getString("tb", "");
        dbeditor.putInt("tbNum", count + 1);
        dbeditor.putString("tb", tbstate+"1");
//undo
        return 1;
    }

}


