package com.acer.example.katalogfilmsub2.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings.System.DATE_FORMAT
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.acer.example.katalogfilmsub2.*
import com.acer.example.katalogfilmsub2.R
import com.acer.example.katalogfilmsub2.search.MovieSearchActivity
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmReceiver : BroadcastReceiver() {



    private lateinit var movieMainViewModel: MovieMainViewModel

    fun listNewRelease(context: Context, notifId: Int){
        val listItems = ArrayList<Movie>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request: PostService = retrofit.create(PostService::class.java)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val today: String = sdf.format(Date())
        val call: Call<JsonObject> = request.getNewRelease("e5ffacd3e2daf66fcfe8be92718237f5", today, today)
        call.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //  val data = response.body()

                    val result = (response.body().toString())
//                    Log.d("body new release", result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        Log.d("judul : ", movie.getString(("title")))
                        val poster_path = "https://image.tmdb.org/t/p/w185"+movie.getString("poster_path")
                        val movieItems = Movie(movie.getInt("id"), movie.getString("title"), movie.getString("overview"), poster_path)
                        listItems.add(movieItems)
                    }
                    sendNotifRelease(context, listItems, notifId)
                }
            }

            override fun onFailure(call: Call<JsonObject>, error: Throwable) {
                Log.e("failure", "errornya ${error.message}")
            }
        })
    }

    companion object {
        const val TYPE_ONE_TIME = "OneTimeAlarm"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val TYPE_DAILY = "DailyReminder"
        const val TYPE_RELEASE = "ReleaseReminder"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        // Siapkan 2 id untuk 2 macam alarm, onetime dan repeating
        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101

        private const val ID_DAILY_REMINDER = 102
        private const val ID_RELEASE_REMINDER = 103

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"

        private const val CHANNEL_NAME = "katalog channel"
        private const val GROUP_KEY_EMAILS = "group_key_emails"
        private const val NOTIFICATION_REQUEST_CODE = 200
        private const val MAX_NOTIFICATION = 2
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = "Katalog Film App"
        val notifId = if (type.equals(TYPE_DAILY, ignoreCase = true)) ID_DAILY_REMINDER else ID_RELEASE_REMINDER
//        showToast(context, title, message)

        if (notifId == ID_DAILY_REMINDER){
            showAlarmNotification(context, title, message, notifId)
        }else {
            listNewRelease(context, notifId)
        }
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }

    fun setDailyReminder(context: Context, type: String, time: String, message: String) {
        Log.d("time", time)
        if (isDateInvalid(time, TIME_FORMAT)) return
        Log.d("r", "SIAP")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        val putExtra = intent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, "Daily reminder set up "+time, Toast.LENGTH_SHORT).show()
    }

    fun setReleaseReminder(context: Context, type: String, time: String, message: String) {
        Log.d("time", time)
        if (isDateInvalid(time, TIME_FORMAT)) return
        Log.d("r", "SIAP")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        val putExtra = intent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, "Release reminder set up "+time, Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = if (type.equals(TYPE_DAILY, ignoreCase = true)) ID_DAILY_REMINDER else ID_RELEASE_REMINDER
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, type+" dibatalkan" , Toast.LENGTH_SHORT).show()
    }


    // Gunakan metode ini untuk mengecek apakah alarm tersebut sudah terdaftar di alarm manager
    fun isAlarmSet(context: Context, type: String): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONETIME else ID_REPEATING

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun sendNotifRelease(context: Context, listItems: ArrayList<Movie>, notifId: Int) {
        val i = listItems.size
        Log.d("list release", listItems.toString())
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, NewReleaseActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder: NotificationCompat.Builder
        //Melakukan pengecekan jika idNotification lebih kecil dari Max Notif
        val CHANNEL_ID = "channel_01"
        if(i == 0){
            mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("New Release Movie")
                .setContentText("Tidak ada movie baru hari ini")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setGroup(GROUP_KEY_EMAILS)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }else if (i < MAX_NOTIFICATION) {
            mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("New Release Movie")
                .setContentText(listItems[i].judul)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setGroup(GROUP_KEY_EMAILS)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine("New Movie " + listItems[i - 1].judul)
                .addLine("New Movie " + listItems[i - 2].judul)
                .setBigContentTitle("$i new movie")
                .setSummaryText("Katalog Movie")
            mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("$i new movie")
                .setContentText("Katalog Movie")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        }
        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(notifId, notification)
    }


}
