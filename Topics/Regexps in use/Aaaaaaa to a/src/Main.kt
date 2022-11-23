fun main() {
    val text = readln()
    // write your code here
    println(text.replace("[a|A]+".toRegex(),"a"))
}