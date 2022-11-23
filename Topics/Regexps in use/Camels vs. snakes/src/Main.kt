fun getCamelStyleString(inputString: String): String {
    // put your code here
   return inputString.split("_").joinToString("") { it.capitalize() }

}