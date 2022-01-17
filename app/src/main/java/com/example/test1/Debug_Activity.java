package com.example.test1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class Debug_Activity extends AppCompatActivity {
    public void printLog(String str) {
        Log.e("print", str);
    }
    private MyApplication allres=new MyApplication();
    private MyBluetooth bluetools=new MyBluetooth();
    private ConnectedThread nana7mi;
    private static MyBluetooth.ConnectThread mConnectThread;
    private String getinfo="";
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_);
        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText sendmessage,getmessages;
                sendmessage=findViewById(R.id.sendinfo);
                getmessages=findViewById(R.id.getinfo);
                String temp=allres.Macnow;
                String sendmessagestr=(sendmessage.getText()).toString();
                if((sendmessage.getText()).length()==0){
                    @SuppressLint("WrongConstant") Toast toast = Toast.makeText(Debug_Activity.this, "输入不能为空", 1000);
                    toast.show();
                }
                else{
                    BluetoothAdapter mybluetooth=BluetoothAdapter.getDefaultAdapter();
                    mybluetooth.enable();//打开蓝牙
                    bluetools.mBtAdapter=mybluetooth;
                    BluetoothDevice bluetoothDevice = null;
                    try {
                        bluetoothDevice = mybluetooth.getRemoteDevice(temp);
                        bluetools.connect(bluetoothDevice);
                    } catch (Exception e) {
                        printLog(e.toString());
                        @SuppressLint("WrongConstant") Toast warningempty = Toast.makeText(Debug_Activity.this, "MAC格式错误或为空", 240);
                        warningempty.show();
                        Dialog dialog = new Dialog(Debug_Activity.this);
                        View warning1 = getLayoutInflater().inflate(R.layout.warning1,null);
                        dialog.setContentView(warning1);
                        dialog.show();
                    }
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
                        Calendar caltools=Calendar.getInstance();
                        String hour= String.valueOf(caltools.get(Calendar.HOUR_OF_DAY));
                        String min= String.valueOf(caltools.get(Calendar.MINUTE));
                        String sed=String.valueOf(caltools.get(Calendar.SECOND));
                        String timedata="->"+hour+"-"+min+"-"+sed+": "+sendmessagestr+"\n";
                        getinfo=getinfo+timedata;
                        getmessages.setText(getinfo);
                    }
                }
            }
        });
    }
}