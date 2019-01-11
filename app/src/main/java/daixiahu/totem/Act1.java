package daixiahu.totem;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import daixiahu.totem.backstage.DatabaseManager;

public class Act1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act1_layout);
        Button button1 = (Button) findViewById(R.id.Act1_Button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newdatabase("Schools");
                newdatabase("Companies");
            }
        });
    }
        public int newdatabase(String databasename){
            DatabaseManager db = new DatabaseManager();
            db.newdatabase(this, databasename);
            return 1;
        }


}
