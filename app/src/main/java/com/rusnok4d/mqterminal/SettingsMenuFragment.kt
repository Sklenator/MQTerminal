package com.rusnok4d.mqterminal

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rusnok4d.mqterminal.databinding.SettingsMenuBinding
import java.util.Locale

class SettingsMenuFragment : Fragment() {
    private var _binding: SettingsMenuBinding? = null
    private val binding get() = _binding!!

    private var nightMode : Boolean = false
    private var editor : SharedPreferences.Editor? = null
    private var sharedPreferences : SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsMenuBinding.inflate(inflater, container, false)

        checkNightMode()

        binding.btnAbout.setOnClickListener {
            val action = SettingsMenuFragmentDirections.actionSettingsMenuFragmentToAboutFragment()
            findNavController().navigate(action)
        }

        binding.swDarkMode.setOnCheckedChangeListener {
                _,
                _ ->
            if (nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor=sharedPreferences?.edit()
                editor?.putBoolean("night", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor=sharedPreferences?.edit()
                editor?.putBoolean("night", true)
            }
            editor?.apply()
        }

        binding.btnLanguage.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle(R.string.change_language_to)
                .setMessage(R.string.language_dialog_message)
                .setPositiveButton(R.string.czech) { _, _ ->
                setLanguage("cs")
                requireActivity().onBackPressed()

            }
                .setNegativeButton(R.string.english) { _, _ ->
                setLanguage("en")
                requireActivity().onBackPressed()

            }
                .setCancelable(true)
                .setNeutralButton(R.string.cancel) { dialogAlert, _ ->
                dialogAlert.dismiss()
            }
            dialog.show()
        }

        binding.btnBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLanguage(languageCode : String) {
        val resources: Resources = this.resources
        val dm : DisplayMetrics = resources.displayMetrics
        val conf : Configuration = resources.configuration
        conf.setLocale(Locale(languageCode.lowercase(Locale.ROOT)))
        resources.updateConfiguration(conf, dm)
    }

    private fun checkNightMode(){
        sharedPreferences = context?.getSharedPreferences("MODE", Context.MODE_PRIVATE)

        nightMode = sharedPreferences?.getBoolean("night", false)!!

        if(nightMode){
            binding.swDarkMode.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}