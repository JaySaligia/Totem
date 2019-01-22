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
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import daixiahu.totem.backstage.Sysclass;
import daixiahu.totem.backstage.TotemLexer;
import daixiahu.totem.backstage.TotemParser;

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
                init();
                //translate(scanfSql.getText().toString());
                //translate("update class1 set sname = xiaoming where sname=xiaoli;");
                testsql();
            }
        });
    }

    void init(){
        Sysclass c = new Sysclass();
        c.init(this);
    }

    void testsql(){
        init();
        translate("create class class1 (sno int,sname string, sex string, age int, grade int);");
        translate("insert into class1 values (1, Alice, male, 22, 100);");
        translate("insert into class1 values (2, Bob, female, 21, 90);");
        translate("insert into class1 values (3, Carolu, female, 22, 55);");
        translate("insert into class1 values (4, Dogge, male, 24, 83);");
        translate("insert into class1 values (5, Ellen, female, 21, 84);");
        translate("insert into class1 values (6, Frank, male, 20, 48);");
        translate("insert into class1 values (7, Garen, female, 23, 78);");
        translate("insert into class1 values (8, Hasee, male, 21, 92);");
        translate("insert into class1 values (9, Ice, female, 24, 50);");
        translate("insert into class1 values (10, Jason, male, 22, 77);");
        translate("update class1 set sname = Anna where sname=Alice;");
        translate("select sno,sname from class1 where grade>0;");
        //translate("create selectdeputy malegoodstu select sno as num, grade as goal from class1 where grade>80 AND sex=male;");
        //translate("create selectdeputy femalegoodstu select sno as num, grade as goal from class1 where grade>80 AND sex=female;");
        //translate("select num, goal from malegoodstu where goal>85 OR num>5;");
        //translate("select num, goal from malegoodstu where goal>80;");
        //translate("delete from class1 where sname=Hasee;");
        //translate("select num, goal from malegoodstu where goal>80;");
        //translate("select femalegoodstu->class1.sname from femalegoodstu where goal>80;");
        //translate("drop class class1;");
    }

    void translate(String expr){
        ANTLRInputStream in = new ANTLRInputStream(expr);
        TotemLexer lexer = new TotemLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TotemParser parser = new TotemParser(tokens);
        TotemParser.RootContext createStatementContext = parser.root();
        String test = createStatementContext.s;
        if (test == null) {
            test = "语法有错，请检查语法输入";
            Toast.makeText(Act1.this, test, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Act1.this, test, Toast.LENGTH_SHORT).show();
            Sysclass c = new Sysclass();
            String[] element = test.split(" *, *");
            String id = element[0].split(" *: *")[1];
            String showmsg = "";
            switch (id){
                case "0":c.trans_newsrcclass(Act1.this, element);showmsg = "新建源类成功";break;//新建源类
                case "1":c.trans_newdeputyclass(Act1.this, element);showmsg = "新建代理类成功";break;//新建代理类
                case "2":c.trans_inserttuple(Act1.this, element);showmsg = "插入对象成功";break;//插入对象
                case "3":c.trans_deletetuple(Act1.this, element);showmsg = "删除对象成功";break;//删除对象
                case "4":c.trans_deleteclass(Act1.this, element);showmsg = "删除类成功";break;//删除类
                case "5":updateResultTable(c.trans_selecttuple(Act1.this, element));showmsg = "选择对象成功";break;//选择特定对象
                case "6":updateResultTable(c.trans_selectdeputytuple(Act1.this, element));showmsg = "跨类查询成功";break;//跨类查询
                case "7":c.trans_updatetuple(Act1.this, element);showmsg = "修改属性成功";break;//修改对象属性

            }
            //Toast.makeText(Act1.this, showmsg, Toast.LENGTH_SHORT).show();
        }
    }

    void updateResultTable(String[] results)
    {
        result.removeAllViews();
        for(int i = 0;i<results.length;i++)
        {
            String[] tuple = results[i].split(" ");
            TableRow tr = new TableRow(getBaseContext());
            for(int j=0;j<tuple.length;j++)
            {
                TextView tv = new TextView(getBaseContext());
                tv.setBackgroundResource(R.drawable.table_shape);
                tv.setMaxHeight(200);
                tv.setMaxWidth(400);
                tv.setMinWidth(200);
                tv.setText(tuple[j]);
                tr.addView(tv);
            }
            result.addView(tr);
        }
    }
}
