package com.example.leejs4937.networkproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by leejs4937 on 2016. 5. 21..
 */
public class logout_dialog extends Dialog {

    private Button yes;
    private Button no;
    private Context con;

    public logout_dialog (Context context) {
        super(context);
        con = context;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.logout_dialog);

        yes = (Button) findViewById(R.id.logout_yes);
        no = (Button) findViewById(R.id.logout_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( con, MainActivity.class );
                con.startActivity(intent);
                ((Activity)con).finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
