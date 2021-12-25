package pl.polsl.peoplecounter.datatypes

data class DetectionTime(val hour:String, val minute:String){
    companion object{
        fun setTwoDigitsFormat(element:String):String{
            if(element.length == 1){
                return "0" + element
            }
            return element
        }
    }
}
