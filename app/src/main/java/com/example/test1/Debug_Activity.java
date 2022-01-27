package com.example.test1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static com.example.test1.MyApplication.sendcomp;
import static java.lang.Math.abs;

public class Debug_Activity extends AppCompatActivity {
    public void printLog(String str) {
        Log.e("print", str);
    }
    private MyApplication allres=new MyApplication();
    private MyBluetooth bluetools=new MyBluetooth();
    private ConnectedThread nana7mi;
    private MyBluetooth.ConnectThread mConnectThread;
    public static String getinfo="";
    public static String hexinfo="";
    static EditText sendmessage,getmessages;
    static Switch hexon;
    Button send,empty,link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_);
        sendmessage=findViewById(R.id.sendinfo);
        getmessages=findViewById(R.id.getinfo);
        send=(Button) findViewById(R.id.send);
        empty=(Button) findViewById(R.id.empty);
        hexon=(Switch) findViewById(R.id.hex);
        send.setEnabled(false);
        send.setText("寸止!");
        String temp=allres.Macnow;
        BluetoothAdapter mybluetooth=BluetoothAdapter.getDefaultAdapter();
        mybluetooth.enable();//打开蓝牙
        bluetools.mBtAdapter=mybluetooth;
        BluetoothDevice bluetoothDevice = null;
        try {
            bluetoothDevice = mybluetooth.getRemoteDevice(temp);
            bluetools.connect(bluetoothDevice);
            printLog("activity_handle: "+String.valueOf(sendcomp.what));
            send.setEnabled(true);
            send.setText("发射!");
        } catch (Exception e) {
            printLog(e.toString());
            @SuppressLint("WrongConstant") Toast warningempty = Toast.makeText(Debug_Activity.this, "MAC格式错误或为空", 240);
            warningempty.show();
            Dialog dialog = new Dialog(Debug_Activity.this);
            View warning1 = getLayoutInflater().inflate(R.layout.warning1,null);
            dialog.setContentView(warning1);
            dialog.show();
        }
        hexon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    hexon.setTextColor(0xffff0000);
                    getmessages.setTextColor(0xffff0000);
                    getmessages.setText(hexinfo);
                    getmessages.setSelection(getmessages.getText().length());
                }else {
                    hexon.setTextColor(0xFFBABABA);
                    getmessages.setTextColor(0xFF000000);
                    getmessages.setText(getinfo);
                    getmessages.setSelection(getmessages.getText().length());
                    printLog("hexo off");
                }
            }
        });
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getinfo="";
                getmessages.setText("");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendmessagestr=(sendmessage.getText()).toString();
                if((sendmessage.getText()).length()==0){
                    @SuppressLint("WrongConstant") Toast toast = Toast.makeText(Debug_Activity.this, "输入不能为空", 1000);
                    toast.show();
                }
                else{
                    if(bluetools.nanami==null){
                        printLog("nanami is empty");
                    }
                    else{
                        byte[] sendbytes= new byte[0];
                        try {
                            sendbytes = sendmessagestr.getBytes("GBK");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        bluetools.nanami.write(sendbytes);
                        //handler.sendMessage(sendcomp);
                        printLog("handle: "+String.valueOf(sendcomp.what));
                        String hexoString="";
                        for(int i=0;i<sendbytes.length;i++){
                            hexoString=hexoString+(Integer.toHexString(sendbytes[i])).substring(Integer.toHexString(sendbytes[i]).length()-2).toUpperCase()+" ";//截取编码后最后两位得到GBK原码
                        }
                        printLog(hexoString);
                        Calendar caltools=Calendar.getInstance();
                        String hour= String.valueOf(caltools.get(Calendar.HOUR_OF_DAY));
                        String min= String.valueOf(caltools.get(Calendar.MINUTE));
                        String sed=String.valueOf(caltools.get(Calendar.SECOND));
                        String hexodata="->"+hour+":"+min+":"+sed+": "+hexoString+"\n";
                        String timedata="->"+hour+":"+min+":"+sed+": "+sendmessagestr+"\n";
                        getinfo=getinfo+timedata;
                        hexinfo=hexinfo+hexodata;
                        if(hexon.isChecked()){
                            getmessages.setText(hexinfo);
                        }
                        else{
                            getmessages.setText(getinfo);
                        }
                        getmessages.setSelection(getmessages.getText().length());
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        getinfo="";//清空收发字符串
        hexinfo="";
        super.onDestroy();
        if(bluetools.nanami!=null){
            bluetools.nanami.cancel();
        }
        //bluetools.mConnectThread.cancel();
        printLog("tread exit");
    }
}