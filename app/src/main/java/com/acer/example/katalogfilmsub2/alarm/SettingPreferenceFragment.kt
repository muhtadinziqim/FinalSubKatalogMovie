package com.acer.example.katalogfilmsub2.alarm

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.acer.example.katalogfilmsub2.R

class SettingPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var RELEASE: String
    private lateinit var DAILY: String

    private lateinit var isReleasePreference: SwitchPreference
    private lateinit var isDailyPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        private const val DEFAULT_VALUE = "Tidak Ada"
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.setting_preference)

        init()
        setSummaries()
    }

    private fun init(){
        RELEASE = resources.getString(R.string.reease_reminder)
        DAILY = resources.getString(R.string.daily_reminder)

        isReleasePreference = findPreference<SwitchPreference>(RELEASE) as SwitchPreference
        isDailyPreference = findPreference<SwitchPreference>(DAILY) as SwitchPreference
    }

    private fun setSummaries(){
        val sh =  preferenceManager.sharedPreferences
        isDailyPreference.isChecked = sh.getBoolean(DAILY, false)
        isReleasePreference.isChecked = sh.getBoolean(RELEASE, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == DAILY){
            isDailyPreference.isChecked = sharedPreferences.getBoolean(DAILY, false)
        }

        if (key == RELEASE){
            isReleasePreference.isChecked = sharedPreferences.getBoolean(RELEASE, false)
        }


        alarmReceiver = AlarmReceiver()

        if (isDailyPreference.isChecked){
            Log.d("daily", " OKE")
            val repeatTime = "07:00"
            val repeatMessage = "Daily Reminder : Seilahkan Buka App Movie Katalog"
            Log.d("rem", "OKE")
            alarmReceiver.setDailyReminder(requireContext(), AlarmReceiver.TYPE_DAILY,
                    repeatTime, repeatMessage)
        }else{
            Log.d("daily", "FALSE")
            alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_DAILY)
        }

        if (isReleasePreference.isChecked){
            Log.d("daily", " OKE")
            val repeatTime = "08:00"
            val repeatMessage = "Ada film apa yang baru nih, silahkan di cek"
            Log.d("rem", "OKE")
            alarmReceiver.setReleaseReminder(requireContext(), AlarmReceiver.TYPE_RELEASE,
                repeatTime, repeatMessage)
        }else{
            Log.d("daily", "FALSE")
            alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_RELEASE)
        }
    }
}
