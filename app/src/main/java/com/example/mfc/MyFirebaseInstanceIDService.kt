package com.example.mfc

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "notification_channel"
const val channelName = "com.example.mfc"
const val TAG = "NotificationTest"
const val TAGG ="TOKEN"
class MyFirebaseInstanceIDService:FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAGG,"Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage:RemoteMessage){

//        if(remoteMessage.data.isNotEmpty()){
            if(remoteMessage.getNotification() != null){
                Log.d(TAG, "Message data payload:${remoteMessage.messageId}")
                generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
            }else{
                Log.d(TAG, "Error")
            }
//        }else{
//            Log.d(TAG,"data is Empty")
//        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title:String?, message: String?):RemoteViews{
        val remoteViews = RemoteViews("com.example.mfc",R.layout.notification)

        remoteViews.setTextViewText(R.id.title,title)
        remoteViews.setTextViewText(R.id.message,message)
        remoteViews.setImageViewResource(R.id.logo_app,R.drawable.bell)

        return remoteViews
    }

    fun generateNotification(title: String, body: String) {
        val title:String? = null
        val message:String? = null

        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.bell)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }



}

