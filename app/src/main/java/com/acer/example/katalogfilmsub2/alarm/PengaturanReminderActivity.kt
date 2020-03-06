package com.acer.example.katalogfilmsub2.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.acer.example.katalogfilmsub2.R
import kotlinx.android.synthetic.main.activity_pengaturan_reminder.*

class PengaturanReminderActivity : AppCompatActivity() {


    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan_reminder)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, SettingPreferenceFragment()).commit()

    }
}
