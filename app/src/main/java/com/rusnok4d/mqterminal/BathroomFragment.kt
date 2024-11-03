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
import com.rusnok4d.mqterminal.databinding.BathroomMenuBinding

class BathroomFragment : Fragment() {
    private var _binding: BathroomMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private val args : BathroomFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BathroomMenuBinding.inflate(inflater, container, false)

        binding.btnBathroomBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnBathroomLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnBathroomLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnBathroomLights.text = getString(R.string.turn_off_lights)
                        binding.bathroomLightsStatus.text = getString(R.string.status_on)
                        binding.bathroomLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        Log.d("BathroomFragment", args.broker)
                        publishMessage("smarthome/house/bathroom", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnBathroomLights.text = getString(R.string.turn_on_lights)
                        binding.bathroomLightsStatus.text = getString(R.string.status_off)
                        binding.bathroomLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/house/bathroom", "Lights 0")
                    }
                }
            }
        }

        binding.btnBathroomShowTemperature.setOnClickListener {
            if(args.connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                Thread.sleep(1000)
                binding.txtVBathroomTemperature.text = mqttHandler.temperature + " °C"
            }
        }

        if (args.connection) {
            binding.txtVBathroomLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVBathroomLightsStatus.text = getString(R.string.online)
            binding.txtVBathroomTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVBathroomTemperatureStatus.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVBathroomLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVBathroomLightsStatus.text = getString(R.string.offline_status)
            binding.txtVBathroomTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVBathroomTemperatureStatus.text = getString(R.string.offline_status)

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