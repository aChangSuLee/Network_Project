package com.example.leejs4937.networkproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ok_btn = (Button)findViewById(R.id.ok_button);
        ok_btn.setOnClickListener(ClickLintener);
    }

    Button.OnClickListener ClickLintener = new View.OnClickListener(){
        public void onClick (View v) {
            EditText id_edit = (EditText)findViewById(R.id.id_edit);
            EditText pw_edit = (EditText)findViewById(R.id.pw_edit);
            String id = null;
            String pw = null;
            switch (v.getId()) {
                case R.id.ok_button:
                    id = id_edit.getText().toString();
                    pw = pw_edit.getText().toString();
                    if ( id.equals("network") && pw.equals("network") ){
                        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "login", "로그인중입니다", true);
                        ok_btn.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                }
                                catch (Exception e) {}
                                dialog.dismiss();
                                Intent intent = new Intent( getApplicationContext(), TabActivity.class );
                                startActivity(intent);
                                finish();
                            }
                        }).start();
                    }
                    else {
                        final ProgressDialog dialog2 = ProgressDialog.show(MainActivity.this, "login", "로그인중입니다", true);
                        ok_btn.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                }
                                dialog2.dismiss();
                            }
                        }).start();
                        Toast toast = Toast.makeText(MainActivity.this, "id 또는 password 가 잘못되었습니", Toast.LENGTH_SHORT);
                        toast.show();
                        ok_btn.setEnabled(true);
                    }
            }
        }
    };
}
