fun main() {
    val string = readLine()!!
    val n = readLine()!!.toInt()

    string.split("\\s+".toRegex(),n).forEach { println(it) }

}