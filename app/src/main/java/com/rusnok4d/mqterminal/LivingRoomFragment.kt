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
import com.rusnok4d.mqterminal.databinding.LivingRoomMenuBinding

class LivingRoomFragment : Fragment() {
    private var _binding: LivingRoomMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private val args : LivingRoomFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LivingRoomMenuBinding.inflate(inflater, container, false)

        binding.btnLivingRBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnLivingRLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnLivingRLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnLivingRLights.text = getString(R.string.turn_off_lights)
                        binding.livingRLightsStatus.text = getString(R.string.status_on)
                        binding.livingRLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        Log.d("BathroomFragment", args.broker)
                        publishMessage("smarthome/house/living_room", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnLivingRLights.text = getString(R.string.turn_on_lights)
                        binding.livingRLightsStatus.text = getString(R.string.status_off)
                        binding.livingRLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/house/living_room", "Lights 0")
                    }
                }
            }
        }

        binding.btnLivingRShowTemperature.setOnClickListener {
            if(args.connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                Thread.sleep(1000)
                binding.txtVLivingRTemperature.text = mqttHandler.temperature + " °C"
            }
        }

        if (args.connection) {
            binding.txtVLivingRLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVLivingRLightsStatus.text = getString(R.string.online)
            binding.txtVLivingRTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVLivingRTemperatureStatus.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVLivingRLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVLivingRLightsStatus.text = getString(R.string.offline_status)
            binding.txtVLivingRTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVLivingRTemperatureStatus.text = getString(R.string.offline_status)
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