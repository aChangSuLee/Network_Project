package com.example.leejs4937.networkproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class MainActivity extends AppCompatActivity {
    Button ok_btn;
    Button del_btn;
    Button join_btn;
    String id = null;
    String pw = null;
    EditText id_edit;
    EditText pw_edit;

    ProgressDialog dialog;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ok_btn = (Button)findViewById(R.id.ok_button);
        ok_btn.setOnClickListener(ClickLintener);

        del_btn = (Button)findViewById(R.id.login_del_button);
        del_btn.setOnClickListener(ClickLintener);

        join_btn = (Button) findViewById(R.id.join_button);
        join_btn.setOnClickListener(ClickLintener);

        id_edit = (EditText)findViewById(R.id.id_edit);
        pw_edit = (EditText)findViewById(R.id.pw_edit);

        file = new File("/sdcard/LogInfo.txt");
        if( file.exists() == false ) {
            WriteTextFile("/sdcard/LogInfo.txt", "");
        } else {
            String[] id_pw = ReadTextFile().split("/");
            if ( id_pw.length > 1) {
                id = id_pw[0];
                pw = id_pw[1];
                id_edit.setText(id);
                pw_edit.setText(pw);
            }
        }
    }

    Button.OnClickListener ClickLintener = new View.OnClickListener(){
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.ok_button:
                    id = id_edit.getText().toString();
                    pw = pw_edit.getText().toString();
                    if ( !id.equalsIgnoreCase("") && !pw.equalsIgnoreCase("") ) {
                        dialog = ProgressDialog.show(MainActivity.this, "login", "로그인중입니다", true);
                        ok_btn.setEnabled(false);
                        new Handler().post(mRunnable);
                    } else {
                        Toast.makeText(getApplicationContext(), "id 또는 pw 를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.login_del_button:
                    id_edit.setText("");
                    pw_edit.setText("");
                    break;
                case R.id.join_button:
                    Intent intent1 = new Intent( getApplicationContext(), joinActivity.class );
                    startActivity(intent1);
            }
        }
    };

    public boolean WriteTextFile(String strFileName, String strBuf) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(strBuf);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public String ReadTextFile() {
        String text = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            Reader in = new InputStreamReader(fis);
            int size = fis.available();
            char[] buffer = new char[size];
            in.read(buffer);
            in.close();

            text = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return text;
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            final Handler handler = new Handler () {
                public void handleMessage( Message msg )
                {
                    Bundle bundle = msg.getData();
                    String receive_data = bundle.getString("data");
                    Log.i ("networker", "receive_data: "+receive_data);

                    if ( receive_data.equals("OK") ) {
                        WriteTextFile("/sdcard/LogInfo.txt", id+"/"+pw);
                        NetworkManager.getInstance().setID(id);
                        NetworkManager.getInstance().setPW(pw);
                        Intent intent = new Intent( getApplicationContext(), TabActivity.class );
                        startActivity(intent);
                        finish();
                    }
                    else if ( receive_data.equals("DENY") ) {
                        WriteTextFile("/sdcard/LogInfo.txt", "");
                        Toast.makeText(getApplicationContext(), "잘못된 id 또는 pw 입니다", Toast.LENGTH_SHORT).show();
                        ok_btn.setEnabled(true);
                    }
                    dialog.dismiss();
                }
            };

            new Thread (){
                public void run()
                {
                    NetworkManager.getInstance().sendMag("login/"+id+"/"+pw);
                    NetworkManager.getInstance().readMsg();
                    String receive_data = NetworkManager.getInstance().getData();
                    while ( receive_data.equalsIgnoreCase("") )
                    {
                        receive_data = NetworkManager.getInstance().getData();
                    }
                    Message dd = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("data", receive_data );
                    dd.setData(b);
                    handler.sendMessage(dd);
                }
            }.start();
        }
    };
}
