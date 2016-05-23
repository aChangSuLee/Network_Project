package com.example.leejs4937.networkproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class joinActivity extends AppCompatActivity {

    Button join_apply;
    ProgressDialog dialogJoin;

    String id = null;
    String pw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_apply = (Button) findViewById(R.id.join_apply_button);
        join_apply.setOnClickListener(ClickLintener);

    }

    Button.OnClickListener ClickLintener = new View.OnClickListener(){
        public void onClick (View v) {
            switch (v.getId()) {
                case R.id.join_apply_button:
                    dialogJoin = ProgressDialog.show(joinActivity.this, "join", "회원가입 진행중입니다", true);
                    id = ((EditText)findViewById(R.id.id_join)).getText().toString();
                    pw = ((EditText)findViewById(R.id.pw_join)).getText().toString();
                    if ( !id.equalsIgnoreCase("") && !pw.equalsIgnoreCase("") ) {
                        new Handler().post(mRunnable);
                    } else {
                        Toast.makeText(getApplicationContext(), "id 또는 pw 를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            final Handler handler = new Handler () {
                public void handleMessage( Message msg )
                {
                    Bundle bundle = msg.getData();
                    String receive_data = bundle.getString("data");
                    Log.i ("networker", receive_data);

                    if ( receive_data.equals("OK")) {
                        dialogJoin.dismiss();
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if (receive_data.equals("DENY")) {
                        Toast.makeText(getApplicationContext(), "잘못된 id 또는 pw 입니다", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            new Thread (){
                public void run()
                {
                    dialogJoin.show();
                    NetworkManager.getInstance().sendMag("join/"+id+"/"+pw);
                    NetworkManager.getInstance().readMsg();
                    String receive_data = NetworkManager.getInstance().getData();
                    while ( receive_data == null )
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
