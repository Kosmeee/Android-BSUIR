package com.example.lab1

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import android.content.res.Resources
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast

class MyViewModel : ViewModel() {

    public var number = "1"
    public var number_conv = "1"
    public var adapter = "0"

    fun get_adapter_weight(context: Context): ArrayAdapter<String> {
        val WeightList = context.getResources().getStringArray(R.array.Weight)
        val adapter_weight = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            WeightList
        )
        return adapter_weight
    }

    fun get_adapter_Volume(context: Context): ArrayAdapter<String> {
        val VolumeList = context.getResources().getStringArray(R.array.Volume)
        val adapter_volume = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            VolumeList
        )
        return adapter_volume
    }

    fun get_adapter_distance(context: Context): ArrayAdapter<String> {
        val DistanceList = context.getResources().getStringArray(R.array.Distance)
        val adapter_distance = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            DistanceList
        )
        return adapter_distance
    }

    fun set_text(x: String) : Boolean {
        val sb = StringBuilder()
        if(number.length!=12) {
            sb.append(number).append(x)
            number = sb.toString()
            return true;
        }
        else {
            return false
        }
    }
    fun dot_press() : String{
        var dot_counter = 0
        if(number.length==0){
            return "Dot can't be the first symbol!"
        }
        else{
            for(x in number){
                if(x=='.')
                    dot_counter += 1
                if(dot_counter==1){
                    return "There can not be 2 dots"
                }
            }
        }
        if(!set_text(".")){
            return  "Max length"
        }
        return "1"
    }

    fun x_press() {
        number = number.dropLast(1)
    }

    fun change(){
        var temp = number
        number = number_conv
        number_conv = temp
    }
}