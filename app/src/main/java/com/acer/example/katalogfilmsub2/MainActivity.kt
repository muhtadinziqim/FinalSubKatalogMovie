package com.acer.example.katalogfilmsub2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.alarm.AlarmReceiver
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.favorite_list.MovieFavActivity
import com.acer.example.katalogfilmsub2.favorite_list.TvShowFavActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movies.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "film channel"

        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    private lateinit var dataBaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataBaseHelper = DatabaseHelper(this)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f

    }

}
