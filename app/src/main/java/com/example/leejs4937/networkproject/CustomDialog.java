package com.example.leejs4937.networkproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by leejs4937 on 2016. 5. 21..
 */
public class CustomDialog extends Dialog {

    private String mTitle;
    private TextView mTitleView;
    private TextView IteminfoView;
    private Button btn;
    private Button buy_btn;
    private String id;
    private ImageView imageview;
    private ProgressDialog buydialog;


    ProgressDialog infodialog;
    Context con;

    public CustomDialog (Context context, String title, String ID, String Price, String Seller) {
        super(context);
        con = context;
        mTitle = "상품명: " + title + " / 금액: " + Price + "원 \n 판매자: " + Seller;
        id = ID;

        infodialog = ProgressDialog.show(context, "수신", "상품 정보를 전송중입니다", true);
        new Handler().post(mRunnable);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getContext(), "구매가 완료되었습니다", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.custom_dialog);

        mTitleView = (TextView) findViewById(R.id.dialog_title);
        IteminfoView = (TextView) findViewById(R.id.item_info);
        IteminfoView.setMovementMethod(new ScrollingMovementMethod());
        btn = (Button) findViewById(R.id.dialog_btn);
        imageview = (ImageView) findViewById(R.id.item_image);
        buy_btn = (Button) findViewById(R.id.buy_btn);
        buy_btn.setEnabled(false);

        // 제목과 내용을 생성자에서 셋팅한다.
        mTitleView.setText(mTitle);
        imageview.setImageBitmap(BitmapFactory.decodeResource(con.getResources(), R.drawable.loading_image));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buydialog = ProgressDialog.show(getContext(), "수신", "구매 중입니다", true);
                new Handler().post(mRunnable1);
            }
        });
    }

    Runnable mRunnable1 = new Runnable() {
        @Override
        public void run() {
            final Handler handler = new Handler () {
                public void handleMessage( Message msg )
                {
                    Bundle bundle = msg.getData();
                    String receive_data = bundle.getString("data");
                    Log.i ("networker", "receive_data: "+receive_data);

                    if ( receive_data.equals("OK") ) {
                        Toast.makeText(getContext(), "구매가 완료되었습니다", Toast.LENGTH_SHORT).show();
                    }
                    else if ( receive_data.equals("DENY") ) {
                        Toast.makeText(getContext(), "이미 팔린 상품입니다", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            };

            new Thread (){
                public void run()
                {
                    NetworkManager.getInstance().sendMag("item_num/"+id);
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

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            final Handler handler = new Handler () {
                public void handleMessage( Message msg )
                {
                    Bundle bundle = msg.getData();
                    String receive_data = bundle.getString("data");
                    byte[] receive_byte_data = bundle.getByteArray("bytedata");
                    Log.i ("networker", "receive_byte_data in dialog: "+receive_byte_data + "/length: "+receive_byte_data.length);


                    if ( receive_data.equals("DENY") ) {
                        Toast.makeText(con, "이미팔린상품입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeByteArray( receive_byte_data, 0, receive_byte_data.length ) ;
                        imageview.setImageBitmap(bitmap);
                        IteminfoView.setText(receive_data);
                    }
                    infodialog.dismiss();
                    buy_btn.setEnabled(true);
                }
            };

            new Thread (){
                public void run()
                {
                    NetworkManager.getInstance().sendMag("item_num/"+id);
                    NetworkManager.getInstance().readMsg();
                    String receive_data = NetworkManager.getInstance().getData();
                    while ( receive_data.equalsIgnoreCase("") )
                    {
                        receive_data = NetworkManager.getInstance().getData();
                    }

                    if ( !receive_data.equals("DENY") ) {
                        NetworkManager.getInstance().sendMag("image/" + id);

                        try {
                            Thread.sleep(2500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        NetworkManager.getInstance().readMsg();
                    }



                    byte[] receive_byte_data = NetworkManager.getInstance().getByteData();

                    Message dd = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("data", receive_data );
                    b.putByteArray("bytedata", receive_byte_data );
                    dd.setData(b);
                    handler.sendMessage(dd);
                }
            }.start();
        }
    };
}
