package com.example.test1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class nana7mipost extends Service {
    private String URL="https://api.bilibili.com/x/space/acc/info?mid=434334701";
    public nana7mipost() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        request_na nanami=new request_na();
        String getinfo="";
        printLog("request start");
        try {
            getinfo=nanami.run(URL);
        } catch (IOException e) {
            e.printStackTrace();
            printLog("request failed");
        }
        printLog(getinfo);
        int slocaltion=getinfo.indexOf("\"live_room\"");
        int elocaltion=getinfo.indexOf(",\"birthday\"");
        getinfo=getinfo.substring(slocaltion,elocaltion);
        getinfo="{"+getinfo+"}";
        printLog(getinfo);
        Gson gson=new Gson();
        nanamibean beanOne = gson.fromJson(getinfo, nanamibean.class);
        int live= beanOne.live_room.getLiveStatus();
        if(live==0){
            printLog("海子姐还没播");
            printLog(beanOne.live_room.getTitle());
        }
        else{
            printLog("海子姐播了");
        }
        NotificationManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = (NotificationManager) getSystemService(nana7mipost.NOTIFICATION_SERVICE);
        }
        Notification notification = new NotificationCompat.Builder(nana7mipost.this)
                .setContentText("通知内容")
                .setContentTitle("通知标题")
                .setSmallIcon(R.drawable.nijisanji)
                .setWhen(System.currentTimeMillis())
                .build();
        manager.notify(1,notification);
        delay(100);
        return START_STICKY;
    }
    private void printLog(String str) {
        Log.e("print", str);
    }
    private static class request_na extends Thread{
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
    private void delay(long ind){
        try
        {
            Thread.sleep(ind);//单位：毫秒
        } catch (Exception e) {
        }
    }
    @Override
    public void onDestroy() {
        printLog("service stop");
        super.onDestroy();
    }
}