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
                //dosth1(scanfSql.getText().toString());
                dosth();
                //String[] s = {"张三 21 男","李四 30 男","王五 16 女"};
                //updateResultTable(s);
            }
        });

    }

    void dosth(){
        Sysclass c = new Sysclass();
        c.init(this);
        String[] attr = {"sno","sex","age"};
        String[] attr_1 = {"sno", "age"};
        int[] type = {0,0,1};
        String[] attrgroup = new String[0];
        int[] typegroup = new int[0];
        String[] tuple0 = {"xiaoming3","male", "28"};
        String[] tuple1 = {"xiaogang3", "male", "39"};
        String[] tuple2 = {"xiaohong3", "female", "17"};
        String[] tuple3 = {"xiaoming2","male", "18"};
        String[] tuple4 = {"xiaogang2", "male", "19"};
        String[] tuple5 = {"xiaohong2", "female", "17"};
        String[] tuple6 = {"xiaoming1","male", "18"};
        String[] tuple7 = {"xiaogang1", "male", "19"};
        String[] tuple8 = {"xiaohong1", "female", "17"};

        c.newsysclass(this, "class1", attr, type);
        //c.newsysclass(this, "class2", attr, type);
        c.inserttuple(this, "class1", tuple0, -1);
        c.inserttuple(this, "class1", tuple1, -1);
        c.inserttuple(this, "class1", tuple2, -1);
        c.inserttuple(this, "class1", tuple3, -1);
        c.inserttuple(this, "class1", tuple4, -1);
        c.inserttuple(this, "class1", tuple5, -1);
        c.inserttuple(this, "class1", tuple6, -1);
        c.inserttuple(this, "class1", tuple7, -1);
        c.inserttuple(this, "class1", tuple8, -1);
        //c.deltuple(this, "0", "0");
        //c.deltuple(this, "0", "1");
        //c.newproxyclass(this, "group1", "class1", attr, attr, attrgroup, typegroup);
        //c.inserttuple(this, "group1", tuple0, 0);
        //c.inserttuple(this, "group1", tuple2, 2);
        //int[] t = {0,1};
        //String test = c.choosetuple(this, "0", "0", t);
        String[] strings = c.showselecttuple(this, "0", attr_1, "(sno = xiaohong3 OR sex = male) AND age > 18");
        updateResultTable(strings);
        Toast.makeText(Act1.this, "tttt", Toast.LENGTH_SHORT).show();
    }

    void dosth1(String expr){
        ANTLRInputStream in = new ANTLRInputStream(expr);
        TotemLexer lexer = new TotemLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TotemParser parser = new TotemParser(tokens);

        TotemParser.CreateStatementContext createStatementContext = parser.createStatement();
        String test = createStatementContext.s;
        Toast.makeText(Act1.this, test, Toast.LENGTH_SHORT).show();
    }

    void updateResultTable(String[] results)
    {
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
