package com.example.leejs4937.networkproject;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Created by leejs4937 on 2016. 5. 21..
 */
public class NetworkManager {
    private final String serverIP = "52.79.154.4";
    private final int portNum = 5001;

    private Socket socket = null;

    private String ID = null;
    private String PW = null;

    private String Data = null;
    private byte[] ByteData = null;

    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private NetworkManager() {

    }

    public String getID () {
        return ID;
    }

    public void setID (String id) {
        ID = id;
    }

    public void setPW (String pw) {
        PW = pw;
    }

    public void clearData () {
        Data = "";
    }

    public String getData () {
        return Data;
    }

    public byte[] getByteData () { return ByteData; }

    public void sendMag (String msg) {
        try {
            SocketAddress serverAddr = new InetSocketAddress(serverIP, portNum);
            socket = new Socket ();
            socket.connect(serverAddr, 5000);
            socket.setSoTimeout(100);
            Log.i("networking","connect");
            byte[] sendmsg = msg.getBytes("UTF-8");
            Log.i("networking","msg: "+msg);
            socket.getOutputStream().write(sendmsg);
        } catch (Exception e) {
            Log.i("networking","connect error: "+e.toString());
        }
    }

    public void sendImg (Bitmap image) {
        try {
            SocketAddress serverAddr = new InetSocketAddress(serverIP, portNum);
            socket = new Socket ();
            socket.connect(serverAddr, 5000);
            socket.setSoTimeout(100);
            int height= image.getHeight();
            int width= image.getWidth();
            Bitmap uploadBitmap = Bitmap.createScaledBitmap( image, 200, height*200/width, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            uploadBitmap.compress( Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();

            socket.getOutputStream().write(byteArray);
            Log.i("networking","sendimage"+byteArray +"/length: "+byteArray.length);
        } catch (Exception e) {
            Log.i("networking","connect error: "+e.toString());
        }
    }

    public void readMsg () {
        try {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream input = socket.getInputStream();
            int numofdata = input.available();
            while (numofdata < 1) {
                numofdata = input.available();
            }
            Log.i("networker","connect");
            byte[] readmsg = new byte[numofdata];
            input.read(readmsg);
            /*
            BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String temp = "";
            while (temp == null) {
                temp = br.readLine();
                Data = Data + temp;
            }
            */
            ByteData = readmsg;
            Data = new String (readmsg, "UTF-8");
            Log.i("networker","readData: "+Data+"ddd");
        } catch (Exception e) {
            Log.i("networking","connect error: "+e.toString());
            Data = "fail";
        }
    }


    public void readList () {
        ArrayList<String> list = new ArrayList<String>();
        try {
            InputStream input = socket.getInputStream();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            int numofdata = input.available();
            while (numofdata < 1) {
                numofdata = input.available();
            }
            byte[] readmsg = new byte[numofdata];
            input.read(readmsg);
            Data = new String (readmsg, "UTF-8");
            Log.i("networker", "readData: " + Data + "ddd");
        } catch (Exception e) {
            Log.i("networking","connect error: "+e.toString());
            Data = "fail";
        }
    }

    public void setInfo (String id, String pw){
        ID = id;
        PW = pw;
    }

}
