package com.example.lab1

 class  Converter {
    fun  WeightConverter(type1:String, type2:String, value:Double): Double {
        var res = 0.0
        when(type1)
        {
            "Gram"->
            {
                res = when(type2) {
                    "Kilogram"-> value/1000
                    "Ton"-> value/1000000
                    else -> {
                        value
                    }
                }

            }
            "Kilogram" ->
            {
                res = when(type2) {
                    "Gram"-> value*1000
                    "Ton"-> value/1000
                    else->{
                        value
                    }
                }
            }
            "Ton"->
            {
                res = when(type2) {
                    "Gram"-> value*1000000
                    "Kilogram"-> value*1000
                    else->{
                        value
                    }
                }
            }
        }

        return res
    }

     fun  DistanceConverter(type1:String, type2:String, value:Double): Double {
         var res = 0.0
         when(type1)
         {
             "Centimeter"->
             {
                 res = when(type2) {
                     "Meter"-> value/100
                     "Kilometer"-> value/100000
                     else -> {
                         value
                     }
                 }

             }
             "Meter" ->
             {
                 res = when(type2) {
                     "Centimeter"-> value*100
                     "Kilometer"-> value/1000
                     else->{
                         value
                     }
                 }
             }
             "Kilometer"->
             {
                 res = when(type2) {
                     "Centimeter"-> value*100000
                     "Meter"-> value*1000
                     else->{
                         value
                     }
                 }
             }
         }

         return res

     }
     fun  VolumeConverter(type1:String, type2:String, value:Double): Double {
         var res = 0.0
         when (type1) {
             "Liter" -> {
                 res = when (type2) {
                     "Milliliter" -> value * 1000
                     "M^3" -> value / 1000
                     else -> {
                         value
                     }
                 }

             }
             "Milliliter" -> {
                 res = when (type2) {
                     "Liter" -> value / 1000
                     "M^3" -> value / 1000000
                     else -> {
                         value
                     }
                 }
             }
             "M^3" -> {
                 res = when (type2) {
                     "Milliliter" -> value * 1000000
                     "Liter" -> value * 1000
                     else -> {
                         value
                     }
                 }
             }
         }

         return res
     }


}