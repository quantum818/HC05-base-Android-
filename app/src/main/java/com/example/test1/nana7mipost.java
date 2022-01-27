package com.example.test1;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class nana7mipost extends Service {
    private String URL="https://api.bilibili.com/x/space/acc/info?mid=434334701";
    public nana7mipost() {
        printLog("service start");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "live";
                    String channelName = "直播提醒";
                    int importance = NotificationManager.IMPORTANCE_MAX;
                    createNotificationChannel(channelId, channelName, importance);
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                if(live==1){
                    printLog("七海nana7mi正在直播");
                    printLog(beanOne.live_room.getTitle());
                    printLog("notice start");
                    Notification notification = new NotificationCompat.Builder(nana7mipost.this, "live")
                            .setAutoCancel(true)
                            .setContentTitle("七海nana7mi正在直播")
                            .setContentText("直播标题:"+beanOne.live_room.getTitle())
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.nana7mi)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.nana7mi))
                            .build();
                    manager.notify(1, notification);
                }
                else{
                    printLog("海子姐没播");
                }
                //创建Notification，传入Context和channelId
                try {
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                run();
            }
        }).start();
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
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, channelName, importance);
            channel.setBypassDnd(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 500, 2000});
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
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