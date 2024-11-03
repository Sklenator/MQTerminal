package com.rusnok4d.mqterminal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rusnok4d.mqterminal.databinding.GarageMenuBinding

class GarageFragment : Fragment() {
    private var _binding: GarageMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private val args : GarageFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GarageMenuBinding.inflate(inflater, container, false)

        binding.btnGarageBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnGarageLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnGarageLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnGarageLights.text = getString(R.string.turn_off_lights)
                        binding.garageLightsStatus.text = getString(R.string.status_on)
                        binding.garageLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        publishMessage("smarthome/garage", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnGarageLights.text = getString(R.string.turn_on_lights)
                        binding.garageLightsStatus.text = getString(R.string.status_off)
                        binding.garageLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/garage", "Lights 0")
                    }
                }
            }
        }

        binding.btnGarageShowTemperature?.setOnClickListener {
            if(args.connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                Thread.sleep(1000)
                binding.txtVGarageTemperature?.text = mqttHandler.temperature + " °C"
            }
        }

        if (args.connection) {
            binding.txtVGarageLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGarageLightsStatus.text = getString(R.string.online)
            binding.txtVGarageDoorsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGarageDoorsStatus.text = getString(R.string.online)
            binding.txtVGarageTemperatureStatus?.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGarageTemperatureStatus?.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVGarageLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGarageLightsStatus.text = getString(R.string.offline_status)
            binding.txtVGarageDoorsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGarageDoorsStatus.text = getString(R.string.offline_status)
            binding.txtVGarageTemperatureStatus?.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGarageTemperatureStatus?.text = getString(R.string.offline_status)
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