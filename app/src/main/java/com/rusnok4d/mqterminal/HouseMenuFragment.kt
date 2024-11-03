package com.rusnok4d.mqterminal

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rusnok4d.mqterminal.databinding.HouseMenuBinding


class HouseMenuFragment : Fragment() {
    private var _binding: HouseMenuBinding? = null
    private val binding get() = _binding!!
    var newRoom = ""

    private var connection = false
    private var brokerURL = "tcp://192.168.0.180:1883"
    private var clientID = "Android"

    private val args : HouseMenuFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HouseMenuBinding.inflate(inflater, container, false)
        connection = args.connection
        brokerURL = args.broker
        clientID = args.client
        binding.btnHouseBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.imgVMainHallway?.setOnClickListener {
            val conn = connection
            val broker = brokerURL
            val client = clientID
            val action = HouseMenuFragmentDirections.actionHouseMenuFragmentToMainHallwayFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVMasterBedroom?.setOnClickListener {
            val conn = connection
            val broker = brokerURL
            val client = clientID
            val action = HouseMenuFragmentDirections.actionHouseMenuFragmentToMasterBedroomFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVKitchen?.setOnClickListener {
            val conn = connection
            val broker = brokerURL
            val client = clientID
            val action = HouseMenuFragmentDirections.actionHouseMenuFragmentToKitchenFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVDiningRoom?.setOnClickListener {
            val conn = connection
            val broker = brokerURL
            val client = clientID
            val action = HouseMenuFragmentDirections.actionHouseMenuFragmentToDiningRoomFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVLivingRoom?.setOnClickListener {
            val conn = connection
            val broker = brokerURL
            val client = clientID
            val action = HouseMenuFragmentDirections.actionHouseMenuFragmentToLivingRoomFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.imgVBathroom?.setOnClickListener {
            val conn = connection
            val broker = brokerURL
            val client = clientID
            val action = HouseMenuFragmentDirections.actionHouseMenuFragmentToBathroomFragment(conn, broker, client)
            findNavController().navigate(action)
        }

        binding.btnHouseAdd.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Name")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.add_room_test, null)
            builder.setView(customLayout)

            // add a button
            builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                /*    // send data from the AlertDialog to the Activity
                    val editText = customLayout.findViewById<EditText>(R.id.editText)
                    if(editText.text != null){
                        val btn = Button(context)
                        btn.text = editText.text.toString()
                        btn.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        binding.houseConstraint?.addView(btn)
                    }
    */
            }
            val dialog = builder.create()
            dialog.show()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
