package com.example.test1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.content.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.UUID;

import static com.example.test1.MyApplication.sendcomp;
import static com.example.test1.Debug_Activity.getmessages;
import static com.example.test1.Debug_Activity.hexon;
import static com.example.test1.Debug_Activity.getinfo;
import static com.example.test1.Debug_Activity.hexinfo;
public class MyBluetooth {
    public static BluetoothAdapter mBtAdapter;
    private static ConnectThread mConnectThread;
    public static ConnectedThread nanami;
    private static boolean isBlueToothConnected = false;
    public static final String TAG = "ContentValues";
    public void printLog(String str) {
        Log.e("print", str);
    }
    public void connect(BluetoothDevice device) {
        printLog("connect to: " + device);
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }
    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                printLog("create() failed" + e);
            }
            mmSocket = tmp;
        }
        public void run() {
            if (Thread.interrupted())
                return;
            printLog("BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mBtAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                //isBlueToothConnected = true;
                mmSocket.connect();
                sendcomp.what=1551;
                isBlueToothConnected = true;
            } catch (IOException e) {
                sendcomp.what=773;
                printLog("T unable to connect() socket " + e);
                //handler.sendEmptyMessage(NOT_CONNECT);
                isBlueToothConnected = false;
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    printLog("unable to close() socket during connection failure" + e2);
                }
                return;
            }

            //mConnectThread = null;

            isBlueToothConnected = true;

            // Start the connected thread
            // Start the thread to manage the connection and perform
            // transmissions
            //handler.sendEmptyMessage(CONNECT_SUCCESS);

             nanami = new ConnectedThread(mmSocket);
             nanami.run();
            //blccmp=true;
        }

        public void cancel() {
            try {
                printLog("connect closed");
                mmSocket.close();
            } catch (IOException e) {
                printLog("close() of connect socket failed" + e);
            }
        }
    }
}
class ConnectedThread extends Thread {
    private void delay(long ind){
        try
        {
            Thread.sleep(ind);//单位：毫秒
        } catch (Exception e) {
        }
    }
    //public static ArrayList<Byte> gotinfo= new ArrayList<Byte>();
    public static ByteArrayOutputStream result = new ByteArrayOutputStream();
    public static final String TAG = "ContentValues";

    public void printLog(String str) {
        Log.e("print", str);
    }
    private int allbytes=0;
    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private  String temp="";
    private final OutputStream mmOutStream;
    public ConnectedThread(BluetoothSocket socket) {

        printLog("create ConnectedThread");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            printLog("temp sockets not created" + e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        printLog("stream is built");
        if (Thread.interrupted()) {
            printLog("return");
            return;
        }
        printLog("BEGIN mConnectedThread");
        byte[] buffer = new byte[256];
        int bytes,step=0;

        // Keep listening to the InputStream while connected
        while (true) {
            synchronized (this) {
                try {
                    // Read from the InputStream
                    while((bytes = mmInStream.read(buffer))!=-1) {
                        delay(10);
                        result.write(buffer,0,bytes);
                        printLog(bytes + "bytes");
                        allbytes=allbytes+bytes;
                        while(step!=256&&buffer[step]!=0){
                            temp=temp+(Integer.toHexString(buffer[step])).substring(Integer.toHexString(buffer[step]).length()-2).toUpperCase()+" ";
                            step++;
                        }
                        if(mmInStream.available()==0){printLog("empty end");break;}//输入流中断退出
                        if(bytes==256){
                            run();
                        }//数组溢出循环
                        break;
                    }
                    printLog("total send: "+allbytes+" bytes");
                    String getmessage="";
                    try {
                        getmessage=result.toString("GBK");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(getmessage.length()!=0){
                        Calendar caltools=Calendar.getInstance();
                        String hour= String.valueOf(caltools.get(Calendar.HOUR_OF_DAY));
                        String min= String.valueOf(caltools.get(Calendar.MINUTE));
                        String sed=String.valueOf(caltools.get(Calendar.SECOND));
                        String timedata="<-"+hour+"-"+min+"-"+sed+": "+getmessage+"\n";
                        String hexdata="<-"+hour+"-"+min+"-"+sed+": "+temp+"\n";
                        hexinfo=hexinfo+hexdata;
                        getinfo=getinfo+timedata;
                        if(hexon.isChecked()){
                            getmessages.setText(hexinfo);
                        }
                        else{
                            getmessages.setText(getinfo);
                        }
                        getmessages.setSelection(getmessages.getText().length());
                    }
                    //returnres=(int)buffer[0];
                    //printLog(String.valueOf(returnres));
                    //if(returnres==67){warning="得到数据";}
                    // Message msg = new Message();
                    // msg.what = GET_DATA;
                    // Bundle bundle = new Bundle();
                    //bundle.putInt("data", buffer[0]);
                    //msg.setData(bundle);
                    //handler.sendMessage(msg);
                    sendcomp.what=1551;
                    printLog("handle: "+String.valueOf(sendcomp.what));
                    result=new ByteArrayOutputStream();
                    allbytes=0;
                } catch (IOException e) {
                    printLog("disconnected " + e);
                    sendcomp.what=773;
                    printLog("handle: "+String.valueOf(sendcomp.what));
//                        handler.sendEmptyMessage(OUT_OF_CONNECTED);
                    break;
                }
                printLog("all_end");
            }
            printLog("all2_end");
        }
    }

    /**
     * Write to the connected OutStream.
     *
     * @param buffer The bytes to write
     */
    public void write(byte[] buffer) {
        try {
            sendcomp.what=1551;
            mmOutStream.write(buffer); //java.io.IOException: Broken pipe
        } catch (IOException e) {
            sendcomp.what=773;
            Log.e(TAG, "Exception during write", e);
        }
        //blccmp=false;
    }

    public void cancel() {
        try {
            printLog("connect socket closed");
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }
}
