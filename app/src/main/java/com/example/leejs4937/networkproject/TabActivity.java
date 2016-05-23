package com.example.leejs4937.networkproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class TabActivity extends android.app.TabActivity {

    final int REQ_CODE_SELECT_IMAGE=100;

    TabHost mTab;
    ListView mListview;
    CustomAdapter mAdapter;
    logout_dialog dialog;

    EditText sellBox;
    EditText sellBox_name;
    EditText sellBox_price;
    Button sellbtn;
    Button getImagebtn;

    ProgressDialog selldialog;
    ProgressDialog itemdialog;

    String content;
    String name;
    String price;

    ImageView image;

    Bitmap image_bitmap;
    boolean image_select;

    @Override
    protected void onPause () {
        super.onPause();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

        @Override
    public void onBackPressed() {
        dialog = new logout_dialog(this);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        image_select = false;

        mTab = getTabHost();
        TabHost.TabSpec spec;
        LayoutInflater.from(this).inflate(R.layout.activity_tab, mTab.getTabContentView(), true);
        spec = mTab.newTabSpec("tab1").setIndicator("구매").setContent(R.id.tab1);
        mTab.addTab(spec);
        spec = mTab.newTabSpec("tab2").setIndicator("판매").setContent(R.id.tab2);
        mTab.addTab(spec);

        for(int i=0;i<mTab.getTabWidget().getChildCount();i++) {
            mTab.getTabWidget().getChildAt(i).setPadding(16,16,0,0);
            mTab.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF1493")); //unselected
        }

        mTab.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#FFFFFF")); //unselected

        mTab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for(int i=0;i<mTab.getTabWidget().getChildCount();i++) {
                    mTab.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF1493")); //unselected
                }
                mTab.getTabWidget().getChildAt(mTab.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


        image = (ImageView)findViewById(R.id.sellImage);
        image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
        image.setImageBitmap(image_bitmap);

        mAdapter = new CustomAdapter();

        // Xml에서 추가한 ListView 연결
        mListview = (ListView) findViewById(R.id.list_view);

        // ListView에 어댑터 연결
        mListview.setAdapter(mAdapter);

        itemdialog = ProgressDialog.show(TabActivity.this, "수신", "상품 목록을 전송중입니다", true);
        new Handler().post(mRunnable1);


        sellBox = (EditText) findViewById(R.id.sellBox);
        sellBox_name = (EditText) findViewById(R.id.sellBox_name);
        sellBox_price = (EditText) findViewById(R.id.sellBox_price);

        sellbtn = (Button) findViewById(R.id.sellBtn);
        getImagebtn = (Button) findViewById(R.id.getImage);

        getImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        sellbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = sellBox_name.getText().toString();
                price = sellBox_price.getText().toString();
                content = sellBox.getText().toString();
                if ( !name.equalsIgnoreCase("") && !price.equalsIgnoreCase("") && !content.equalsIgnoreCase("") ) {
                    selldialog = ProgressDialog.show(TabActivity.this, "전송", "전송중입니다", true);
                    new Handler().post(mRunnable);
                } else {
                    Toast.makeText(getApplicationContext(), "상품명 또는 상품가격 또는 상품설명을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                        Toast.makeText(getApplicationContext(), "업로드에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    }
                    else if ( receive_data.equals("DENY") || receive_data.equals("fail") ) {
                        Toast.makeText(getApplicationContext(), "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                    sellBox.setText("");
                    sellBox_name.setText("");
                    sellBox_price.setText("");
                    image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
                    image.setImageBitmap(image_bitmap);
                    selldialog.dismiss();
                }
            };

            new Thread (){
                public void run()
                {
                    NetworkManager.getInstance().sendMag("item_sell/"+name+"/"+price+"/"+NetworkManager.getInstance().getID()+"/"+content);
                    NetworkManager.getInstance().readMsg();
                    String receive_data = NetworkManager.getInstance().getData();
                    while ( receive_data.equalsIgnoreCase("") )
                    {
                        receive_data = NetworkManager.getInstance().getData();
                    }
                    if ( receive_data.equals("OK") ) {
                        NetworkManager.getInstance().sendImg(image_bitmap);
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

    Runnable mRunnable1 = new Runnable() {
        @Override
        public void run() {
            final Handler handler = new Handler () {
                public void handleMessage( Message msg )
                {
                    Bundle bundle = msg.getData();
                    String receive_data = bundle.getString("data");
                    Log.i ("networker", "receive_data in handler: "+receive_data);

                    String[] list = receive_data.split("\n");

                    if ( list[0].equals("DENY") ) {
                        Toast.makeText(getApplicationContext(), "아무런 상품이 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i=0; i<list.length; i++) {
                            String[] sublist = list[i].split("/");
                            mAdapter.add(sublist[0], sublist[1], sublist[2], sublist[3]);
                            Log.i ("networker", "handler: "+sublist[0]+" "+sublist[1]+ " "+sublist[2]);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    itemdialog.dismiss();
                }
            };

            new Thread (){
                public void run()
                {
                    ArrayList<String> list = null;
                    NetworkManager.getInstance().sendMag("item_list");
                    NetworkManager.getInstance().readMsg();
                    String receive_data = NetworkManager.getInstance().getData();
                    while ( receive_data.equalsIgnoreCase("") )
                    {
                        receive_data = NetworkManager.getInstance().getData();
                    }
                    if ( receive_data.equals("Start") )
                        NetworkManager.getInstance().readList();

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    receive_data = NetworkManager.getInstance().getData();

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
