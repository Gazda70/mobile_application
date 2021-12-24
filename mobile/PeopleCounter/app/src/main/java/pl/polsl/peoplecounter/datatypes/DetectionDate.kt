package pl.polsl.peoplecounter.datatypes

data class DetectionDate( val year:String,
                           val month:String,
                           val day:String,){

    companion object {
        fun formatMonthNumberToLiteralShortcut(numberMonth:Int):String{
            return when(numberMonth){
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                12 -> "Dec"
                else -> "Err"
            }
        }
    }
}
