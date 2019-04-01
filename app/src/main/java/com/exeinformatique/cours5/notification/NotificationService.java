package com.exeinformatique.cours5.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.exeinformatique.cours5.MainActivity;
import com.exeinformatique.cours5.R;
import com.exeinformatique.cours5.notification.model.MessageModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "NotificationService";
    NotificationManager notificationManager;
    FirebaseFirestore database;
    int idNotification = 2;

    @Override
    public void onCreate(){
        createNotificationChannel();
        database = FirebaseFirestore.getInstance();
        listenForNotificationMessage();
        super.onCreate();
    }

    public void createNotificationChannel(){
        createNotificationChannelService();
        createNotificationChannelMessage();
    }

    public void createNotificationChannelService(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = CHANNEL_ID;
            CharSequence channelName = "NotificationService";
            String channelDescription = "Notification service";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel
                    (channelId, channelName, channelImportance);
            channel.setDescription(channelDescription);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotificationChannelMessage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = CHANNEL_ID;
            CharSequence channelName = "NotificationMessage";
            String channelDescription = "Notification de message";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel
                    (channelId, channelName, channelImportance);
            channel.setDescription(channelDescription);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationServiceIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationServiceIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Notification service")
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1, notification);
        //todo ligne du listen
        return START_STICKY;
    }

    private void listenForNotificationMessage(){
        database.collection("Notification")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot documentMessage:queryDocumentSnapshots){
                    MessageModel messageModel = documentMessage.toObject(MessageModel.class);
                    sendNotificationForMessage(messageModel);
                }
            }
        });
    }

    private void sendNotificationForMessage(MessageModel messageModel){
        Notification notification = NotificationCreator
                .createNotificationForMessage(this, messageModel);
        notificationManager.notify(idNotification, notification);
        idNotification++;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
