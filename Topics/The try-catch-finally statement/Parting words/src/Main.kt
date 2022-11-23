import java.lang.Exception
import java.lang.IndexOutOfBoundsException

fun pepTalk(name: String): String {

    return try {
        val (firstName, secondName) =
            name.split(" ")
        "Good luck!\nDon't lose the towel, traveler $firstName $secondName!"
    } catch (e: Exception) {
        "Good luck!\nDon't lose the towel, nameless one."
    }


}
// do not change the function above        

fun main() {
    val name = readLine()!!
    val advice = pepTalk(name) // fix this function call
    print(advice)
}