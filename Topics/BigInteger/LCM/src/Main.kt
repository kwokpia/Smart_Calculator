import java.math.BigInteger

fun main() {
    // write your code here
    val a = readln().toBigInteger()
    val b = readln().toBigInteger()
    val gcm = a*b / a.gcd(b)
    println(gcm)
}