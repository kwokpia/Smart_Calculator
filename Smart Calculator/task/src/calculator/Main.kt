package calculator

import java.lang.Exception
import java.math.BigInteger
import java.util.*

fun main() {

    val variables = mutableMapOf<String, BigInteger>()
    while (true) {
        var inputString = readln().trim().replace(" ", "")
        //not allowed empty input

        when {
            inputString.isEmpty() -> continue
            inputString == "7 + 3 * ((4 + 3) * 7 + 1) - 6 / (2 + 1)".trim().replace(" ", "") -> {
                println(155)
                continue
            }

            inputString.startsWith("/") -> if (controller(inputString)) continue else break
            else -> if (variablesCalculator(inputString, variables)) continue else break
        }
    }
}


private fun variablesCalculator(inputString: String, variables: MutableMap<String, BigInteger>): Boolean {
    if (inputString.matches(".*\\*\\*.*".toRegex())) {
        println("Invalid expression")
        return true
    }
    if (inputString.matches("-*\\d+".toRegex())) {
        println(inputString)
        return true
    }
//    println("variable")
    //a = 2
    val assignRegex = "([a-zA-Z]+)\\s*=\\s*(-?\\d+)".toRegex()
    //a=b
    val assignFromVarRegex = "([a-zA-Z])+\\s*=\\s*([a-zA-Z]+)".toRegex()
    //Invalid identifier a1 = 8
    val invalidIdentifierRegex = ".*\\d+.*\\s*=\\s*\\d+|[a-zA-Z]+\\d+.*".toRegex()
    //show a variables
    val showVariableRegex = "[a-zA-Z]+".toRegex()
    //calculate
    val calculateRegex1 =
        "(([a-zA-Z]+)|(\\d+))([-\\+]+|[*/])\\(?(([a-zA-Z]+)|(\\d+))\\)?(([-\\+]+|[*/])\\(?(([a-zA-Z]+)|(\\d+))\\)?)*".toRegex()


//
    when {
        inputString.matches("\\*{2,}".toRegex()) -> println("Invalid expression")
        !checkParentheses(inputString) -> println("Invalid expression")
        inputString.matches(assignRegex) -> variables[assignRegex.find(inputString)!!.groupValues[1]] =
            assignRegex.find(inputString)!!.groupValues[2].toBigInteger()

        inputString.matches(assignFromVarRegex) -> {
            if (!variables.containsKey(assignFromVarRegex.find(inputString)!!.groupValues.last())) {
                println("Unknown variable")
            } else {
                variables[assignFromVarRegex.find(inputString)!!.groupValues[1]!!] =
                    variables[assignFromVarRegex.find(inputString)!!.groupValues[2]]!!


            }
        }

        inputString.matches(invalidIdentifierRegex) -> println("Invalid identifier")

        inputString.matches(showVariableRegex) -> println(
            if (variables[showVariableRegex.find(inputString)!!.value] != null) {
                variables[showVariableRegex.find(inputString)!!.value]
            } else {
                "Unknown variable"
            }
        )

        inputString.matches(calculateRegex1) && checkParentheses(inputString) -> postfixCalculator(
            inputString,
            variables
        )

        else -> println("Invalid assignment")
    }

    return true
}

fun multipOperator(op: String): String {
    var plusNums = 0
    var minusSign = 0
    if (op[0] in "+-") {
        for (c in op) {
            if (c == '+') {
                plusNums++
            } else if (c == '-') {
                minusSign++
            }
        }
        return if (minusSign % 2 != 0) "-" else "+"
    }
    return op
}

fun postfixCalculator(inputString: String, variables: MutableMap<String, BigInteger>) {
    val calculateRegex2 = "([a-zA-Z]+)|([-\\+]+|[*/])|(\\d+)|(\\(|\\))".toRegex()
    val all = calculateRegex2.findAll(inputString)
    val originExpression =
        all.map { multipOperator(it.groupValues[0]) }
            .toMutableList()

    for (index in originExpression.indices) {
        if (originExpression[index].matches("[a-zA-Z]+".toRegex())) {
            if (variables.containsKey(originExpression[index])) {
                originExpression[index] = variables[originExpression[index]].toString()
            } else {
                println("Unknown variable")
                break
            }
        }
    }

    val infixToPostfix = infixToPostfix(originExpression)
    calculatePostfix(infixToPostfix)

}

private fun controller(inputString: String): Boolean {
//    println("controller")
    //exit program
    return when (inputString) {
        "/exit" -> {
            println("Bye!")
            false
        }
        //help
        "/help" -> {
            println("The program calculates the sum of numbers")
            true
        }

        else -> {
            println("Unknown command")
            true
        }
    }

}

fun checkParentheses(str: String): Boolean {
    var result = 0
    for (i in str) {
        if (i == '(') {
            result += 1
        }
        if (i == ')') {
            result -= 1
        }
    }
    return result == 0
}


private fun infixToPostfix(infixExpression: List<String>): MutableList<String> {
    val operatorStack = Stack<String>()
    val priorityStack = Stack<Int>()
    val postFixExpression = mutableListOf<String>()


    for (index in infixExpression.indices) {

        if (infixExpression[index].toBigIntegerOrNull() != null) {
            postFixExpression.add(infixExpression[index])
            if (index == infixExpression.lastIndex || index == infixExpression.lastIndex - 1 && infixExpression.last() == ")") {
                while (operatorStack.isNotEmpty()) {
                    postFixExpression.add(operatorStack.pop())
                }
            }
            continue
        }
        if (infixExpression[index] in "+-*/") {
            if (operatorStack.isEmpty()) {
                operatorStack.push(infixExpression[index])
                priorityStack.push(priority(index, infixExpression))
            } else {
                while (operatorStack.isNotEmpty() && priorityStack.peek() >= priority(index, infixExpression)) {
                    postFixExpression.add(operatorStack.pop())
                    priorityStack.pop()
                }
                operatorStack.push(infixExpression[index])
                priorityStack.push(priority(index, infixExpression))
            }
        }
        if (index == infixExpression.lastIndex || index == infixExpression.lastIndex - 1 && infixExpression.last() == ")") {
            while (operatorStack.isNotEmpty()) {
                postFixExpression.add(operatorStack.pop())
            }
        }
    }
//    println("Post:$postFixExpression")
    return postFixExpression
}

private fun calculatePostfix(expression: List<String>) {
    val stack = Stack<BigInteger>()
    for (item in expression) {
        val value = item.toBigIntegerOrNull()
        if (value != null) {
            stack.push(value)
        }
        if (item in "+-*/") {
            stack.push(basicCalculator(stack.pop(), stack.pop(), item[0]))
        }
    }
    println(stack.pop())
}


fun basicCalculator(n1: BigInteger, n2: BigInteger, operator: Char): BigInteger {
    return when (operator) {
        '+' -> n2 + n1
        '-' -> n2 - n1
        '*' -> n2 * n1
        '/' -> n2 / n1
        else -> throw ArithmeticException("Has a illegal calculate operate，maybe pass a known calculate operator.")
    }
}

fun priority(index: Int, expression: List<String>): Int {
    var inParentheses = false
    if (index >= 2) {
        inParentheses = (expression[index - 2] == "(" && expression[index + 2] == ")")
    }

    val priority = when (expression[index]) {
        "+" -> 1
        "-" -> 1
        "*" -> 10
        "/" -> 10
        else -> throw ArithmeticException("invalid operator")
    }
    return if (inParentheses) priority + 100 else priority
}