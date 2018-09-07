package com.io.sdchain.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.io.sdchain.R;
import com.io.sdchain.ui.activity.MainActivity;
import com.io.sdchain.utils.AppUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by huangkangke on 2016/5/19.
 */
public final class APKTOupdateDownService extends Service {
    private static final int TIMEOUT = 10 * 1000;
    private String address;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;
    private String app_name = "";
    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;

    private int notification_id = 0;
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("mark", "net status has changed");
                AppUtils.stopRunningService(getApplicationContext(), "com.io.sdchain.service.APKTOupdateDownService");
                connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        Toast.makeText(context, context.getString(R.string.info192), Toast.LENGTH_LONG).show();
                    } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        Toast.makeText(context, context.getString(R.string.info193), Toast.LENGTH_LONG).show();
                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Toast.makeText(context, context.getString(R.string.info194), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.info195), Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //  IntentFilter mFilter = new IntentFilter();
        // mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //  registerReceiver(mReceiver, mFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            app_name = intent.getStringExtra("app_name");
            address = intent.getStringExtra("address");
            // 创建文件
            createFile(app_name);

            createNotification();

            createThread();

        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
    }

    @SuppressLint("HandlerLeak")
    public void createThread() {
        final Handler handler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWN_OK:
                        notificationManager.cancel(notification_id);
                        stopService(updateIntent);
                        // AppUtils.stopRunningService(getApplicationContext(),"com.example.administrator.myapplication.Servicer.APKTOupdateDownService");
                        AppUtils.installApk(APKTOupdateDownService.this, updateFile);
                        AppUtils.stopRunningService(getApplicationContext(), "com.io.sdchain.service.APKTOupdateDownService");
                        break;
                    case DOWN_ERROR:
                        contentView.setTextViewText(R.id.notificationTitle, getString(R.string.info196));
                        notification.contentView = contentView;
                        notificationManager.cancel(notification_id);
                        // AppUtils.stopRunningService(getApplicationContext(), "com.example.administrator.myapplication.Servicer.APKTOupdateDownService");
                        // stopService(updateIntent);
                        break;


                    default:
                        //  AppUtils.stopRunningService(getApplicationContext(),"com.example.administrator.myapplication.Servicer.APKTOupdateDownService");
                        stopService(updateIntent);
                        break;

                }

            }

        };

        final Message message = new Message();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long downloadSize = downloadUpdateFile(address,
                            updateFile.toString());
                    if (downloadSize > 0) {
                        // 下载成功
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = DOWN_ERROR;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }

    RemoteViews contentView;

    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notification = new Notification();
        // notification.defaults = Notification.DEFAULT_SOUND;

        notification.icon = R.mipmap.ic_launcher;
        notification.tickerText = getString(R.string.info197);

        contentView = new RemoteViews(getPackageName(), R.layout.item_notification);
        contentView.setImageViewResource(R.id.notificationImage, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.notificationTitle, getString(R.string.info198));
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;
        updateIntent = new Intent(this, MainActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        notification.contentIntent = pendingIntent;

        notificationManager.notify(notification_id, notification);
    }

    /***
     *
     * @return
     * @throws MalformedURLException
     */
    public long downloadUpdateFile(String address, String file)
            throws Exception {

        int down_step = 1;
        int totalSize;
        int downloadCount = 0;
        int updateCount = 0;
        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(address);
        Logger.e("url:" + url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// if file exist,then cover
        byte buffer[] = new byte[1024];
        int readsize = 0;
        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// get download size by time
            if (updateCount == 0
                    || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                // change notice
                contentView.setTextViewText(R.id.notificationPercent,
                        updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100,
                        updateCount, false);
                // show_view
                notificationManager.notify(notification_id, notification);

            }

        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();
        return downloadCount;

    }

    public static File updateDir = null;
    public static File updateFile = null;

    public void createFile(String name) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            updateDir = new File(Environment
                    .getExternalStorageDirectory() + "/" + "instalment" + "/");
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            updateFile = new File(updateDir + name + ".apk");
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}

