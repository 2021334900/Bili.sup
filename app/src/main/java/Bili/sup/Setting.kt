package Bili.sup


import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import Bili.sup.R


class Setting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//        )
        super.onCreate(savedInstanceState)
        getsetting(this, window)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
//            val ks = findPreference("keepscreen") as SwitchPreferenceCompat?
//            when (ks?.isChecked) {
//                true -> activity?.window?.setFlags(
//                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                )
//                else -> activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//            }
//
//            val ah = findPreference("night") as SwitchPreferenceCompat?
//            when (ah?.isChecked) {
//                true -> activity?.setTheme(R.style.follow)
//                else -> activity?.setTheme(R.style.keep)
//            }
//            else {
//                activity?.setTheme(R.style.keep)
//            }

        }
    }
}

