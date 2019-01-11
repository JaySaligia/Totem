package daixiahu.totem;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import daixiahu.totem.backstage.DatabaseManager;

public class Act1 extends AppCompatActivity {
    private EditText chooseDb;
    private EditText scanfSql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act1_layout);
        chooseDb = (EditText) findViewById(R.id.chooseDb);
        scanfSql = (EditText) findViewById(R.id.scanfSql);
        Button button1 = (Button) findViewById(R.id.Act1_Button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chooseDbname = chooseDb.getText().toString();
                String scanfcontext = scanfSql.getText().toString();
                //Toast.makeText(Act1.this, chooseDbname, Toast.LENGTH_SHORT).show();
                if (chooseDbname.equals("")){
                    //Toast.makeText(Act1.this, "test1", Toast.LENGTH_SHORT).show();
                    String []scanflist = scanfcontext.split(" ");
                    newdatabase(scanflist[2]);
                    Toast.makeText(Act1.this, "数据库" + scanflist[2] + "创建成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    newtable(chooseDbname, scanfcontext);
                }
            }
        });

    }
        public int newtable(String chooseDbname, String tbmsg){
            DatabaseManager db = new DatabaseManager();
            db.choosedatabase(this, chooseDbname);
            String[] tbattr = tbmsg.split(",");
            String tbname = tbattr[0];
            String[] attrname = new String[tbattr.length - 1];
            int[] attrtype = new int[tbattr.length - 1];
            for (int i = 1; i < tbattr.length; i ++){
                String[] tmp = tbattr[i].split(" ");
                attrname[i - 1] = tmp[0];
                if (tmp[1].equals("string"))
                attrtype[i - 1] = 0;
                else
                    attrtype[i - 1] = 1;
            }
            db.newtable(this, tbname, attrname, attrtype);
            Toast.makeText(Act1.this, "表" + tbname + "创建成功",Toast.LENGTH_SHORT).show();
            return 1;
        }
        public int newdatabase(String dbname){
            DatabaseManager db = new DatabaseManager();
            db.initsystotal(this);
            db.newdatabase(this, dbname);
            return 1;
        }

        public int newdatabase1(){
            DatabaseManager db = new DatabaseManager();
            db.initsystotal(this);
            db.newdatabase(this, "School1");
            db.choosedatabase(this, "School1");
            String [] attr = {"name", "sex", "age"};
            int [] type = {0, 0, 1};
            db.newtable(this, "class1", attr, type);
            db.newtable(this, "class2", attr, type);
            DatabaseManager db1 = new DatabaseManager();
            db1.newdatabase(this, "School2");
            db1.choosedatabase(this, "School2");
            db1.newtable(this, "class1", attr, type);
            return 1;
        }


}
