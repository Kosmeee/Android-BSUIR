package com.example.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_keyboard.view.*
import kotlinx.android.synthetic.main.fragment_view.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class KeyboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val model: MyViewModel by activityViewModels<MyViewModel>()

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
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_keyboard, container, false)



        fun toast_maxlen(str : String) {
            val text = str
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
        }

        view.button__1.setOnClickListener {
            if (!model.set_text("1")) {
                toast_maxlen("Max length")
            }
        }

        view.button__2.setOnClickListener {
            if (!model.set_text("2")) {
                toast_maxlen("Max length")
            }
        }

        view.button__3.setOnClickListener {
            if (!model.set_text("3")) {
                toast_maxlen("Max length")
            }
        }

        view.button__4.setOnClickListener {
            if (!model.set_text("4")) {
                toast_maxlen("Max length")
            }
        }

        view.button__5.setOnClickListener {
            if (!model.set_text("5")) {
                toast_maxlen("Max length")
            }
        }

        view.button__6.setOnClickListener {
            if (!model.set_text("6")) {
                toast_maxlen("Max length")
            }
        }

        view.button__7.setOnClickListener {
            if (!model.set_text("7")) {
                toast_maxlen("Max length")
            }
        }

        view.button__8.setOnClickListener {
            if (!model.set_text("8")) {
                toast_maxlen("Max length")
            }
        }

        view.button__9.setOnClickListener {
            if (!model.set_text("9")) {
                toast_maxlen("Max length")
            }
        }

        view.button__0.setOnClickListener {
            if (!model.set_text("0")) {
                toast_maxlen("Max length")
            }
        }

        view.button__x.setOnClickListener {
            model.x_press()
        }

        view.button__dot.setOnClickListener {
            var return_value = model.dot_press()
            if(return_value != "1"){
                toast_maxlen(return_value)
            }

        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}