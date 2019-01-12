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

import daixiahu.totem.backstage.Sysclass;

public class Act1 extends AppCompatActivity {
    private EditText scanfSql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act1_layout);
        scanfSql = (EditText) findViewById(R.id.scanfSql);
        Button button1 = (Button) findViewById(R.id.Act1_Button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dosth();
            }
        });

    }

    void dosth(){
        Sysclass c = new Sysclass();
        c.init(this);
        String[] attr = {"sno","sex","age"};
        int[] type = {0,0,1};
        String[] tuple0 = {"xiaoming","male", "18"};
        String[] tuple1 = {"xiaogang", "male", "19"};
        c.newsysclass(this, "class1", attr, type);
        c.newsysclass(this, "class2", attr, type);
        c.inserttuple(this, "class1", tuple0);
        c.inserttuple(this, "class1", tuple1);
        Toast.makeText(Act1.this, "ok", Toast.LENGTH_SHORT).show();
    }




}
