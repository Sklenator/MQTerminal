package com.rusnok4d.mqterminal

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rusnok4d.mqterminal.databinding.MainMenuBinding

class MainMenuFragment : Fragment(R.layout.main_menu) {

    private var _binding: MainMenuBinding? = null
    val binding get() = _binding!!

    private val mqttHandler = MqttHandler()
    private var brokerURL: String = "tcp://192.168.0.180:1883"
    private var clientId: String = "Android"

    private var isConnected: Boolean = false

    private var nightMode : Boolean = false
    private var sharedPreferences : SharedPreferences?=null

    //private val keyConnection = "connection"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
/*        if (savedInstanceState != null) {
            isConnected = savedInstanceState.getBoolean(keyConnection)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainMenuBinding.inflate(inflater, container, false)

        binding.imgVHouse?.setOnClickListener {
            val conn = isConnected
            val broker = brokerURL
            val client = clientId
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToHouseMenuFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgSettings.setOnClickListener {
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToSettingsMenuFragment()
            findNavController().navigate(action)
        }

        binding.imgVGarden?.setOnClickListener {
            val conn = isConnected
            val broker = brokerURL
            val client = clientId
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToGardenFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVBasement?.setOnClickListener {
            val conn = isConnected
            val broker = brokerURL
            val client = clientId
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToBasementFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVGarage?.setOnClickListener {
            val conn = isConnected
            val broker = brokerURL
            val client = clientId
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToGarageFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.btnConnDisconn.setOnClickListener {
            if(binding.btnConnDisconn.text.equals(getString(R.string.conn))){
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.connection)
                val customLayout: View = layoutInflater.inflate(R.layout.connect_dialog, null)
                builder.setView(customLayout)
                    .setMessage(R.string.connect_to_mqtt_broker)
                val brokerIP = customLayout.findViewById<EditText>(R.id.broker_ip)
                val clientName = customLayout.findViewById<EditText>(R.id.client_id)

                builder.setPositiveButton(R.string.conn) { _: DialogInterface?, _: Int ->
                    if(brokerIP.text.toString() != "" && clientName.text.toString() != ""){
                        brokerURL = "tcp://" + (brokerIP.text.toString()) + ":1883"
                        clientId = clientName.text.toString()
                    }
                    Toast.makeText(context, "Connecting to broker $brokerURL as $clientId", Toast.LENGTH_SHORT).show()
                    mqttHandler.connect(brokerURL, clientId)
                    binding.txtVConnDisconn.text = getString(R.string.press_disconn) + "\n" + brokerURL
                    binding.btnConnDisconn.text = getString(R.string.disconn)
                    isConnected = true
                }
                val dialog = builder.create()
                dialog.show()
            }else{
             mqttHandler.disconnect()
             binding.txtVConnDisconn.text = getString(R.string.press_conn)
             binding.btnConnDisconn.text = getString(R.string.conn)
             isConnected = false
            }
        }

        if(isConnected){
            binding.txtVConnDisconn.text = getString(R.string.press_disconn) + "\n" + brokerURL
            binding.btnConnDisconn.text = getString(R.string.disconn)
        }else{
            binding.txtVConnDisconn.text = getString(R.string.press_conn)
            binding.btnConnDisconn.text = getString(R.string.conn)
        }

        sharedPreferences = context?.getSharedPreferences("MODE", Context.MODE_PRIVATE)

        nightMode = sharedPreferences?.getBoolean("night", false)!!

        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        return binding.root
    }
/*
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(keyConnection, isConnected)
    }
*/
    override fun onResume() {
        super.onResume()
        if(isConnected){
        mqttHandler.connect(brokerURL, clientId)
        }
    }
}