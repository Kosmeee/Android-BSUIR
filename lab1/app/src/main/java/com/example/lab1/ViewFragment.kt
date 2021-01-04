package com.example.lab1
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.full.fragment_view.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view.*
import kotlinx.android.synthetic.main.fragment_view.view.*
import kotlinx.android.synthetic.main.fragment_view.view.button_change
import kotlinx.android.synthetic.main.fragment_view.view.button_result
import kotlinx.android.synthetic.main.fragment_view.view.result_Text
import kotlinx.android.synthetic.main.fragment_view.view.spinner_choose
import kotlinx.android.synthetic.main.fragment_view.view.spinner_choose2
import kotlinx.android.synthetic.main.fragment_view.view.spinner_value

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private val model: MyViewModel? by activityViewModels<MyViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MainActivity

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_view, container, false)
        view.spinner_value.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when(view.spinner_value.selectedItem.toString())
                {
                    "Weight"-> {val adapter_weight= context?.let { model?.get_adapter_weight(it) }
                    view.spinner_choose.adapter = adapter_weight
                            view.spinner_choose2.adapter = adapter_weight
                            model?.adapter = "0" }
                    "Distance" -> { val adapter_distance= context?.let { model?.get_adapter_distance(it) }
                        view.spinner_choose.adapter = adapter_distance
                        view.spinner_choose2.adapter = adapter_distance
                        model?.adapter = "1" }
                    else -> {val adapter_volume= context?.let { model?.get_adapter_Volume(it) }
                        view.spinner_choose.adapter = adapter_volume
                        view.spinner_choose2.adapter = adapter_volume
                        model?.adapter = "2"}
                }
            }
        }


        view.button_change.setOnClickListener {
            model?.change()

        }


        view.button_copy1.setOnClickListener{
            var myClipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var myClip: ClipData = ClipData.newPlainText("note_copy", model?.number)
            myClipboard.setPrimaryClip(myClip)
        }
        view.button_copy2.setOnClickListener{
            var myClipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var myClip: ClipData = ClipData.newPlainText("note_copy", model?.number_conv)
            myClipboard.setPrimaryClip(myClip)
        }
        view.button_result.setOnClickListener {
            var text_to_transform = model?.number

            if(text_to_transform==""){
                Toast.makeText(activity, "No data!", Toast.LENGTH_SHORT).show()

                model?.number_conv = ""
            }
            else {
                var from = view.spinner_choose2.selectedItem.toString()
                var to = view.spinner_choose.selectedItem.toString()
                var Converter = Converter()
               var int_value = when(view.spinner_value.selectedItem.toString())
                {
                    "Weight"-> { Converter.WeightConverter(from,to,text_to_transform!!.toDouble())}
                    "Distance" -> { Converter.DistanceConverter(from,to,text_to_transform!!.toDouble()) }
                    else -> {Converter.VolumeConverter(from,to,text_to_transform!!.toDouble()) }
                }

                model?.number_conv = int_value.toString()
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}