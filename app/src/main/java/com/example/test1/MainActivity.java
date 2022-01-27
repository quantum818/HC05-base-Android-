package com.example.test1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import com.google.gson.Gson;
import okhttp3.*;

//import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import static android.content.ContentValues.TAG;
public class MainActivity extends AppCompatActivity {
    private MyApplication allres;
    private Button zqpnmsl, lky,test,debug;
    private BluetoothAdapter mBtAdapter;
    private ConnectThread mConnectThread;
    public ConnectedThread nanami;
    public String PC="A4:B1:C1:3A:51:69";
    public String Nana7mi="98:D3:31:F4:24:F3";
    public String MACallin;
    public String url="https://devapi.qweather.com/v7/weather/now?location=101210611&key=eb7a5d434b964a06900e32f4aa7c3507";
    public String warning="未得到数据";
    public int returnres=0;
    private boolean isBlueToothConnected = false;
    private boolean blccmp = false;
    private boolean readon = true;
    final private dialog dialogbt=new dialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        startService();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            @SuppressLint("WrongConstant") Toast toast = Toast.makeText(MainActivity.this, "本机无蓝牙设备", 60);
            toast.show();
        } else {
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            //mBtAdapter.enable();//打开蓝牙
            String bluetoothad = mBtAdapter.getAddress();
            //String macAddress = android.provider.Settings.Secure.getString(App.getApp().getContentResolver(), "bluetooth_address");
            bluetoothad = "本机蓝牙地址: " + bluetoothad;
            @SuppressLint("WrongConstant") Toast toast = Toast.makeText(MainActivity.this, bluetoothad, 60);
            toast.show();
        }
        zqpnmsl = (Button) findViewById(R.id.zqpnmsl);
        lky = (Button) findViewById(R.id.lky);
        test = (Button) findViewById(R.id.test);
        debug = (Button) findViewById(R.id.debug);
        test.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               textdialog dialog=new textdialog();
               dialog.showdialog(R.layout.dialog);
            }
        });
        lky.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        AboutActivity2.class);
                startActivity(intent);
            }
        });
        debug.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        Debug_Activity.class);
                startActivity(intent);
            }
        });
        zqpnmsl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                boolean bluetoothdeal=false;
                String get="";
                ArrayList<String> dealfinal=new ArrayList<String>();
               while(get==""){
                   weatherget getnow=new weatherget();
                   try {
                       get=getnow.run(url);
                   } catch (IOException e) {
                       e.printStackTrace();
                       printLog("请求失败");
                   }
                   printLog(String.valueOf(get));
                   getdeal get1=new getdeal();
                   dealfinal=get1.stringdeal(get);
                   printLog(String.valueOf(dealfinal));
               }
                mBtAdapter = BluetoothAdapter.getDefaultAdapter();//本地蓝牙设备
                mBtAdapter.enable();//打开蓝牙
                BluetoothDevice bluetoothDevice = null;
                try {
                    bluetoothDevice = mBtAdapter.getRemoteDevice(MACallin);
                    connect(bluetoothDevice);
                } catch (Exception e) {
                    Toast warningempty = Toast.makeText(MainActivity.this, "MAC格式错误或为空", 240);
                    warningempty.show();
                    Dialog dialog = new Dialog(MainActivity.this);
                    View warning1 = getLayoutInflater().inflate(R.layout.warning1,null);
                    dialog.setContentView(warning1);
                    dialog.show();
                }
                if(!isBlueToothConnected){
                    Toast warningempty = Toast.makeText(MainActivity.this, "链接失败", 240);
                    warningempty.show();
                }
                else{
                    byte[] test=new byte[7];
                    //编码天气数据
                    int i=0;
                    for(String str:dealfinal){
                        boolean getpress=true;
                        if(i==0){
                            test[0]=(byte) (str.charAt(0));
                            // printLog(str+"\b");
                            printLog(test[i]+"\n");
                            i++;
                        }
                        else if(i>0&&i<=3){
                            test[i]=(byte) (Integer.parseInt(str));

                            // printLog(str+"\b");
                            printLog(test[i]+"\n");
                       /* if(Integer.parseInt(str)==0){
                            test[i]=(byte) '0';
                        }*/
                            i++;
                        }
                        else if(i==4){

                            int tempF=Integer.parseInt(str)-(Integer.parseInt(str)/10)*10;
                            test[i]=(byte) (Integer.parseInt(str)/10);
                            printLog(test[i]+"\n");
                            test[i+1]=(byte) (tempF);
                            printLog(test[i+1]+"\n");

                        }
                    }
                    test[6]=(byte)'X';
                    printLog(test[i]+"\n");
                    delay(1000);
                    if(isBlueToothConnected){
                        Toast toast2 = Toast.makeText(MainActivity.this,"连接成功", 120);
                        toast2.show();
                    }
                    delay(1000);
                    while(returnres!=67) {
                        if(nanami!=null){
                            Toast toast2 = Toast.makeText(MainActivity.this,"未收到单片机通信请重试", 120);
                            toast2.show();
                            nanami.start();
                            delay(1000);
                        }
                        if(returnres==67) {
                            Toast toast2 = Toast.makeText(MainActivity.this, "收到信息0x43", 120);
                            toast2.show();
                        }
                    }
                    if (nanami != null) {
                        Toast toast2 = Toast.makeText(MainActivity.this, "开始传输天气信息", 120);
                        toast2.show();
                        nanami.write(test);
                    }
                }
/*                while(returnres!=67){
                        nanami.start();
                        delay(1000);
                        Toast toast2 = Toast.makeText(MainActivity.this,"线程开启", 120);
                        toast2.show();
                        readon=false;
                }
                Toast toast = Toast.makeText(MainActivity.this, "接收到数据"+returnres, 120);
                toast.show();
                delay(1000);
                nanami.write(test);*/
/*
                    while (returnres!=67) {
                        if (nanami != null) {
                            nanami.start();
                            Toast toast2 = Toast.makeText(MainActivity.this,"线程开启", 120);
                            toast2.show();
                            readon=false;
                            break;
                        }
                        else{
                            connect(bluetoothDevice);
                        }
                    }
                    if(returnres==67){
                        try
                        {
                            Thread.sleep(1000);//单位：毫秒
                        } catch (Exception e) {
                        }
                        nanami.write(test);
                    }
                    Toast toast = Toast.makeText(MainActivity.this, "接收到数据"+returnres, 120);
                    toast.show();
                    Toast toast2 = Toast.makeText(MainActivity.this, warning, 120);
                    toast2.show();
*/

                //try {
                //   BluetoothSocket tmp =bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                //} catch (IOException e) {
                //   Toast toast = Toast.makeText(MainActivity.this, "create() failed" + e, 120);
                // }
            }
        });
    }
/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.getMac, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }*/
    public class textdialog extends Thread{
        private void showdialog(int id){
            Dialog dialog = new Dialog(MainActivity.this);
            View view = getLayoutInflater().inflate(id,null);
            dialog.setContentView(view);
            Button getMac,cancel;
            dialog.show();
            getMac=(Button) view.findViewById(R.id.getMAC);
            cancel=(Button) view.findViewById(R.id.cancel);
            EditText mactext;
            mactext=(EditText) view.findViewById(R.id.Mactext);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            getMac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((mactext.getText().length())==0){
                        @SuppressLint("WrongConstant") Toast toast = Toast.makeText(MainActivity.this, "输入不能为空", 240);
                        toast.show();
                    }
                    else{
                        MACallin=((mactext.getText()).toString()).toUpperCase(Locale.ROOT);
                        if(MACallin.equals("西园千草")){
                            MACallin=Nana7mi;
                        }
                        else if((MACallin.toUpperCase()).equals("PC")){
                            MACallin=PC;
                        }
                        allres.Macnow=MACallin;
                        printLog("当前Mac"+MACallin);
                        dialog.cancel();
                    }
                }
            });
        }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void printLog(String str) {
        Log.e("print", str);
    }//新增post方法
    private static class weatherget extends Thread{
        public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
         //新建一个OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        String run(String url) throws IOException {
           //创建一个request对象
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            //获取响应并把响应体返回
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }
   private  class getdeal extends Thread{//对响应体简单编发发送五组数据给HC05对单个超过1024的数据将溢出部分分次传输

        public ArrayList<String> stringdeal(String ind){
            /*StringBuffer weathertu=new StringBuffer(ind.substring(ind.indexOf("text")+7,ind.indexOf("\"",ind.indexOf("text")+7)));
            printLog(weathertu.toString());
            StringBuffer feelsLike=new StringBuffer(ind.substring(ind.indexOf("feelsLike")+12,ind.indexOf("\"",ind.indexOf("feelsLike")+12)));
            printLog(feelsLike.toString());
            StringBuffer windSpeed=new StringBuffer(ind.substring(ind.indexOf("windSpeed")+12,ind.indexOf("\"",ind.indexOf("windSpeed")+12)));
            printLog(windSpeed.toString());
            StringBuffer pressure=new StringBuffer(ind.substring(ind.indexOf("pressure")+11,ind.indexOf("\"",ind.indexOf("pressure")+11)));
            printLog(windSpeed.toString());*/
            Gson gson = new Gson();
            Bean beanOne = gson.fromJson(ind, Bean.class);
            char getcode=codeinfo(beanOne.now.getText());
            ArrayList<String> finalcode=new ArrayList<String>();
            finalcode.add(String.valueOf(getcode));
            finalcode.add(beanOne.now.getFeelsLike());
            finalcode.add(beanOne.now.getWindSpeed());
            finalcode.add(beanOne.now.getCloud());
            finalcode.add(beanOne.now.getPressure());
            return finalcode;
        }
        private char codeinfo(String ind){
            char fin='\n';
            switch (ind){
                case "晴" : fin='A';break;
                case "多云" : fin='B';break;
                case "少云" : fin='C';break;
                case "晴间多云" : fin='D';break;
                case "阴" : fin='E';break;
                case "阵雨" : fin='F';break;
                case "小雨" : fin='G';break;
                case "中雨" : fin='H';break;
                case "大雨" : fin='I';break;
                case "暴雨" : fin='J';break;
            }
            if(ind.indexOf("雪")!=-1){
                fin='K';
            }
            else if(ind.indexOf("雾")!=-1){
                fin='L';
            }
            else if(ind.indexOf("霾")!=-1){
                fin='M';
            }
            return fin;
        }
   }
   private void delay(long ind){
       try
       {
           Thread.sleep(ind);//单位：毫秒
       } catch (Exception e) {
       }
   }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
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
            if (Thread.interrupted()) {
                printLog("return");
                return;
            }
            printLog("BEGIN mConnectedThread");
            byte[] buffer = new byte[256];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                synchronized (this) {
                    try {
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);
                        printLog(bytes + "bytes");
                        returnres=(int)buffer[0];
                        printLog(String.valueOf(returnres));
                        if(returnres==67){warning="得到数据";}
                       // Message msg = new Message();
                       // msg.what = GET_DATA;
                        // Bundle bundle = new Bundle();
                        //bundle.putInt("data", buffer[0]);
                        //msg.setData(bundle);

                        //handler.sendMessage(msg);
                    } catch (IOException e) {
                        printLog("disconnected " + e);
//                        handler.sendEmptyMessage(OUT_OF_CONNECTED);
                        break;
                    }
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
            blccmp=false;
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
    public void connect(BluetoothDevice device) {
        printLog("connect to: " + device);
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }
    private class ConnectThread extends Thread {
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
                @SuppressLint("WrongConstant") Toast toast = Toast.makeText(MainActivity.this, "链接失败", 60);
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
            } catch (IOException e) {

                printLog("unable to connect() socket " + e);
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

            mConnectThread = null;

            isBlueToothConnected = true;

            // Start the connected thread
            // Start the thread to manage the connection and perform
            // transmissions
            //handler.sendEmptyMessage(CONNECT_SUCCESS);

            nanami = new ConnectedThread(mmSocket);
            blccmp=true;
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                printLog("close() of connect socket failed" + e);
            }
        }
    }
    public void startService() {
        startService(new Intent(getBaseContext(), nana7mipost.class));
    }
   public void stopService() {
        printLog("service stop");
        stopService(new Intent(getBaseContext(), nana7mipost.class));
    }
}