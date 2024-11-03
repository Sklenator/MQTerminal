package com.rusnok4d.mqterminal

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rusnok4d.mqterminal.databinding.MasterBedroomMenuBinding

class MasterBedroomFragment : Fragment() {
    private var _binding: MasterBedroomMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private val args : MasterBedroomFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MasterBedroomMenuBinding.inflate(inflater, container, false)

        binding.btnMasterBedroomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnMasterBedroomLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnMasterBedroomLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnMasterBedroomLights.text = getString(R.string.turn_off_lights)
                        binding.masterBedroomLightsStatus.text = getString(R.string.status_on)
                        binding.masterBedroomLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        Log.d("BathroomFragment", args.broker)
                        publishMessage("smarthome/house/master_bedroom", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnMasterBedroomLights.text = getString(R.string.turn_on_lights)
                        binding.masterBedroomLightsStatus.text = getString(R.string.status_off)
                        binding.masterBedroomLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/house/master_bedroom", "Lights 0")
                    }
                }
            }
        }

        binding.btnMasterBedroomShowTemperature.setOnClickListener {
            if(args.connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                Thread.sleep(1000)
                binding.txtVMasterBedroomTemperature.text = mqttHandler.temperature + " °C"
            }
        }

        if (args.connection) {
            binding.txtVMasterBedroomLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVMasterBedroomLightsStatus.text = getString(R.string.online)
            binding.txtVMasterBedroomTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVMasterBedroomTemperatureStatus.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVMasterBedroomLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVMasterBedroomLightsStatus.text = getString(R.string.offline_status)
            binding.txtVMasterBedroomTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVMasterBedroomTemperatureStatus.text = getString(R.string.offline_status)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(args.connection) {
            mqttHandler.connect(args.broker, args.client)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun publishMessage(topic: String, message: String){
        //Toast.makeText(context, "Publishing message: $message", Toast.LENGTH_SHORT).show()
        mqttHandler.publish(topic, message)
    }
}