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
import com.rusnok4d.mqterminal.databinding.KitchenMenuBinding

class KitchenFragment : Fragment() {
    private var _binding: KitchenMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private val args : KitchenFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = KitchenMenuBinding.inflate(inflater, container, false)

        binding.btnKitchenBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnKitchenLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnKitchenLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnKitchenLights.text = getString(R.string.turn_off_lights)
                        binding.kitchenLightsStatus.text = getString(R.string.status_on)
                        binding.kitchenLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        Log.d("BathroomFragment", args.broker)
                        publishMessage("smarthome/house/kitchen", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnKitchenLights.text = getString(R.string.turn_on_lights)
                        binding.kitchenLightsStatus.text = getString(R.string.status_off)
                        binding.kitchenLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/house/kitchen", "Lights 0")
                    }
                }
            }
        }

        binding.btnKitchenShowTemperature.setOnClickListener {
            if(args.connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                Thread.sleep(1000)
                binding.txtVKitchenTemperature.text = mqttHandler.temperature + " °C"
            }
        }

        if (args.connection) {
            binding.txtVKitchenLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVKitchenLightsStatus.text = getString(R.string.online)
            binding.txtVKitchenTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVKitchenTemperatureStatus.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVKitchenLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVKitchenLightsStatus.text = getString(R.string.offline_status)
            binding.txtVKitchenTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVKitchenTemperatureStatus.text = getString(R.string.offline_status)
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