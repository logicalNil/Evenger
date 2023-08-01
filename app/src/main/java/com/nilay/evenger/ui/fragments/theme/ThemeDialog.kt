package com.nilay.evenger.ui.fragments.theme

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nilay.evenger.R
import com.nilay.evenger.databinding.DialogThemeChooseBinding
import com.nilay.evenger.utils.AppTheme
import com.nilay.evenger.utils.SharePrefKeys
import com.nilay.evenger.utils.setAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ThemeDialog : DialogFragment() {
    @Inject
    lateinit var pref: SharedPreferences

    private lateinit var binding: DialogThemeChooseBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogThemeChooseBinding.inflate(layoutInflater)
        binding.apply {
            val appTheme = pref.getString(SharePrefKeys.AppTheme.name, AppTheme.Sys.name)
            setCheckedButton(appTheme)
            handleRadioButton()
        }
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(binding.root)
            .setPositiveButton("OK") { d, _ ->
                d.dismiss()
            }
        return dialog.create()
    }

    private fun DialogThemeChooseBinding.handleRadioButton() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_light -> {
                    setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    updatePref(AppTheme.Light)
                }

                R.id.radio_button_dark -> {
                    setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    updatePref(AppTheme.Dark)
                }

                else -> {
                    setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    updatePref(AppTheme.Sys)
                }
            }
        }
    }

    private fun updatePref(appTheme: AppTheme) {
        pref.edit().putString(SharePrefKeys.AppTheme.name, appTheme.name).apply()
    }


    private fun setCheckedButton(appTheme: String?) {
        appTheme?.let {
            when (it) {
                AppTheme.Sys.name -> binding.radioGroup.check(R.id.radio_button_system)
                AppTheme.Light.name -> binding.radioGroup.check(R.id.radio_button_light)
                AppTheme.Dark.name -> binding.radioGroup.check(R.id.radio_button_dark)
            }
        }
    }
}