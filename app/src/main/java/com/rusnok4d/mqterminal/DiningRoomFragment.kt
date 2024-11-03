package com.rusnok4d.mqterminal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rusnok4d.mqterminal.databinding.DiningRoomMenuBinding

class DiningRoomFragment : Fragment() {
    private var _binding: DiningRoomMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private val args : DiningRoomFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DiningRoomMenuBinding.inflate(inflater, container, false)

        binding.btnDiningRBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        binding.btnDiningRLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnDiningRLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnDiningRLights.text = getString(R.string.turn_off_lights)
                        binding.diningRStatusLights.text = getString(R.string.status_on)
                        binding.diningRStatusLights.setTextColor(Color.parseColor("#0F9D58"))
                        publishMessage("smarthome/house/dining_room", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnDiningRLights.text = getString(R.string.turn_on_lights)
                        binding.diningRStatusLights.text = getString(R.string.status_off)
                        binding.diningRStatusLights.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/house/dining_room", "Lights 0")
                    }
                }
            }
        }

        binding.btnDiningRShowTemperature.setOnClickListener {
            if(args.connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                Thread.sleep(1000)
                binding.txtVDiningRTemperature.text = mqttHandler.temperature + " °C"
            }
        }

        if (args.connection) {
            binding.txtVDiningRLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVDiningRLightsStatus.text = getString(R.string.online)
            binding.txtVDiningRTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVDiningRTemperatureStatus.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVDiningRLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVDiningRLightsStatus.text = getString(R.string.offline_status)
            binding.txtVDiningRTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVDiningRTemperatureStatus.text = getString(R.string.offline_status)
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