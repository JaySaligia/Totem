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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import daixiahu.totem.backstage.Sysclass;

public class Act1 extends AppCompatActivity {
    private EditText scanfSql;
    private TableLayout result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act1_layout);
        scanfSql = (EditText) findViewById(R.id.scanfSql);
        Button button1 = (Button) findViewById(R.id.Act1_Button1);
        result = (TableLayout) findViewById(R.id.result);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dosth();
                String[] s = {"张三 21 男"," 李四 30 男","王五 16 女"};
                updateResultTable(s);
            }
        });

    }

    /*void dosth(){
        Sysclass c = new Sysclass();
        c.init(this);
        String[] attr = {"sno","sex","age"};
        int[] type = {0,0,1};
        String[] attrgroup = new String[0];
        int[] typegroup = new int[0];
        String[] tuple0 = {"xiaoming","male", "18"};
        String[] tuple1 = {"xiaogang", "male", "19"};
        String[] tuple2 = {"xiaohong", "female", "17"};
        c.newsysclass(this, "class1", attr, type);
        //c.newsysclass(this, "class2", attr, type);
        //c.inserttuple(this, "class1", tuple0);
        //c.inserttuple(this, "class1", tuple1);
        //c.inserttuple(this, "class1", tuple2);
        //c.deltuple(this, "0", "0");
        //c.deltuple(this, "0", "1");
        c.newproxyclass(this, "group1", "class1", attr, attr, attrgroup, typegroup);
        //int[] t = {0,1};
        //String test = c.choosetuple(this, "0", "0", t);
        Toast.makeText(Act1.this, "ok", Toast.LENGTH_SHORT).show();
    }*/

    void updateResultTable(String[] results)
    {
        for(int i = 0;i<results.length;i++)
        {
            String[] tuple = results[i].split(" ");
            TableRow tr = new TableRow(getBaseContext());
            for(int j=0;j<tuple.length;j++)
            {
                TextView tv = new TextView(getBaseContext());
                tv.setBackgroundResource(R.drawable.ic_launcher_background);
                tv.setPadding(1,1,1,1);
                tv.setText(tuple[j]);
                tr.addView(tv);
            }
        }
    }





}
