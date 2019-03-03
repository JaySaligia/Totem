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
        Button button2 = (Button) findViewById(R.id.Act1_Button2);
        result = (TableLayout) findViewById(R.id.result);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate(scanfSql.getText().toString());
            }
        });


    }

    void init(){
        Sysclass c = new Sysclass();
        c.init(this);
    }

    void testsql(){
        init();
        translate("create class goods(gno int, type string, price int);");
        translate("insert into goods values(1, pc, 3000);");
        translate("insert into goods values(2, pc, 3100);");
        translate("insert into goods values(3, pc, 3200);");
        translate("insert into goods values(4, pc, 3300);");
        translate("insert into goods values(5, laptop, 2100);");
        translate("insert into goods values(6, laptop, 2200);");
        translate("insert into goods values(7, laptop, 2300);");
        translate("insert into goods values(8, laptop, 2400);");
        translate("insert into goods values(9, pc, 4000);");
        translate("insert into goods values(10, pc, 4100);");
        translate("insert into goods values(11, laptop, 4200);");
        translate("insert into goods values(12, pc, 4300);");
        translate("insert into goods values(13, laptop, 3100);");
        translate("insert into goods values(14, laptop, 3200);");
        translate("insert into goods values(15, laptop, 3300);");
        translate("insert into goods values(16, laptop, 3400);");
        translate("select gno,type,price from goods where gno>0;");
        translate("create selectdeputy Japangoods select gno as num, type as model, price as value, tax string from goods where gno<12;");
        translate("select num,model,value,tax from Japangoods where num>0;");
        translate("create selectdeputy USgoods select gno as num, type as model, price as value, tax string from goods where gno>8;");
        translate("select num,model,value,tax from USgoods where num>0;");
        translate("update goods set gno = 17 where gno=1;");
        translate("select num,model,value,tax from Japangoods where num>0;");
        translate("select num,model,value,tax from USgoods where num>0;");
        translate("update Japangoods set tax = 5% where model=pc;");
        translate("update Japangoods set tax = 4% where model=laptop;");
        translate("update USgoods set tax = 3% where model=pc;");
        translate("update USgoods set tax = 2% where model=laptop;");
        translate("select num,model,value,tax from Japangoods where num>0;");
        translate("select num,model,value,tax from USgoods where num>0;");
        translate("select USgoods->goods->Japangoods.tax from USgoods where num>8 AND num<12;");
        translate("select Japangoods->goods->USgoods.tax from Japangoods where num>8 AND num<12;");
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
            //Toast.makeText(Act1.this, test, Toast.LENGTH_SHORT).show();
            result.removeAllViews();
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
            Toast.makeText(Act1.this, showmsg, Toast.LENGTH_SHORT).show();
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
