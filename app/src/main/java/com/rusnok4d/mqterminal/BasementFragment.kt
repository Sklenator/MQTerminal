package com.rusnok4d.mqterminal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rusnok4d.mqterminal.databinding.BasementMenuBinding
import java.lang.Thread.sleep
import kotlin.math.round

class BasementFragment : Fragment() {
    private var _binding: BasementMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private var connection = false
    private var lights = false

    private val args : BasementFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BasementMenuBinding.inflate(inflater, container, false)
        connection = args.connection

        if (connection) {
            mqttHandler.connect(args.broker, args.client)
            mqttHandler.subscribe("smarthome/basement")
        }

        binding.btnBasementBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnShowDistance?.setOnClickListener {
            if (connection) {
                mqttHandler.subscribe("smarthome/basement/distance")
                publishMessage("smarthome/basement/distance", "Receive distance")
                sleep(1000)
                binding.txtVBasementDistance.text = (round(mqttHandler.distance.toDouble() * 100) / 100).toString() + " CM"
            }
        }

        binding.btnBasementShowTemperature.setOnClickListener {
            if(connection){
                mqttHandler.subscribe("smarthome/temperature")
                publishMessage("smarthome/temperature", "Receive temperature")
                sleep(1000)
                binding.txtVBasementTemperature.text = mqttHandler.temperature + " °C"
            }
        }

        binding.btnBasementLights.setOnClickListener {
            if (connection) {
                when(binding.btnBasementLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnBasementLights.text = getString(R.string.turn_off_lights)
                        binding.basementLightsStatus.text = getString(R.string.status_on)
                        binding.basementLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        publishMessage("smarthome/basement", "Lights 1")
                        lights = true
                        }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnBasementLights.text = getString(R.string.turn_on_lights)
                        binding.basementLightsStatus.text = getString(R.string.status_off)
                        binding.basementLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/basement", "Lights 0")
                        lights = false
                    }
                }
            }
        }

        if (connection) {
            binding.txtVBasementLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVBasementLightsStatus.text = getString(R.string.online)
            binding.txtVBasementTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVBasementTemperatureStatus.text = getString(R.string.online)
            binding.txtVBasementDistanceStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVBasementDistanceStatus.text = getString(R.string.online)
            binding.txtVBasementTemperatureStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVBasementTemperatureStatus.text = getString(R.string.online)
        } else if (!connection) {
            binding.txtVBasementLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVBasementLightsStatus.text = getString(R.string.offline_status)
            binding.txtVBasementTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVBasementTemperatureStatus.text = getString(R.string.offline_status)
            binding.txtVBasementDistanceStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVBasementDistanceStatus.text = getString(R.string.offline_status)
            binding.txtVBasementTemperatureStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVBasementTemperatureStatus.text = getString(R.string.offline_status)
        }

        if(lights){
            binding.btnBasementLights.text = getString(R.string.turn_off_lights)
        } else if(!lights){
            binding.btnBasementLights.text = getString(R.string.turn_on_lights)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun publishMessage(topic: String, message: String){
        mqttHandler.publish(topic, message)
    }
}