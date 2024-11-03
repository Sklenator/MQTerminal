package com.rusnok4d.mqterminal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rusnok4d.mqterminal.databinding.GardenMenuBinding


class GardenFragment : Fragment() {
    private var _binding: GardenMenuBinding? = null
    private val binding get() = _binding!!

    private val mqttHandler = MqttHandler()

    private var connection = false

    private val args : GardenFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GardenMenuBinding.inflate(inflater, container, false)
        connection = args.connection

        binding.btnGardenBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnGardenLights.setOnClickListener {
            if (args.connection) {
                when(binding.btnGardenLights.text){
                    getString(R.string.turn_on_lights) -> {
                        binding.btnGardenLights.text = getString(R.string.turn_off_lights)
                        binding.gardenLightsStatus.text = getString(R.string.status_on)
                        binding.gardenLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
                        publishMessage("smarthome/garden", "Lights 1")
                    }
                    getString(R.string.turn_off_lights) -> {
                        binding.btnGardenLights.text = getString(R.string.turn_on_lights)
                        binding.gardenLightsStatus.text = getString(R.string.status_off)
                        binding.gardenLightsStatus.setTextColor(Color.parseColor("#FF0000"))
                        publishMessage("smarthome/garden", "Lights 0")
                    }
                }
            }
        }

        if (args.connection) {
            binding.txtVGardenLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGardenLightsStatus.text = getString(R.string.online)
            binding.txtVGardenBBQStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGardenBBQStatus.text = getString(R.string.online)
            binding.txtVGardenSprinklersStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGardenSprinklersStatus.text = getString(R.string.online)
        } else if (!args.connection) {
            binding.txtVGardenLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGardenLightsStatus.text = getString(R.string.offline_status)
            binding.txtVGardenBBQStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGardenBBQStatus.text = getString(R.string.offline_status)
            binding.txtVGardenSprinklersStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGardenSprinklersStatus.text = getString(R.string.offline_status)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(connection){
            binding.txtVGardenLightsStatus.setTextColor(Color.parseColor("#0F9D58"))
            binding.txtVGardenLightsStatus.text = getString(R.string.online)
            mqttHandler.connect(args.broker, args.client)
        } else if (!connection){
            binding.txtVGardenLightsStatus.setTextColor(Color.parseColor("#FF0000"))
            binding.txtVGardenLightsStatus.text = getString(R.string.offline_status)
        }
    }

    fun publishMessage(topic: String, message: String){
        //Toast.makeText(context, "Publishing message: $message", Toast.LENGTH_SHORT).show()
        mqttHandler.publish(topic, message)
    }
}