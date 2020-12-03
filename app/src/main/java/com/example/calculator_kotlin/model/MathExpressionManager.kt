package com.example.calculator_kotlin.model

import android.util.Log
import com.example.calculator_kotlin.util.*
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator
import kotlin.math.sqrt
import kotlin.text.isLetter

class MathExpressionManager {

    fun configExpressionEvaluator(expression: String): Expression {
        //через этот объект мы сможем вычислять мат. действия из строки
        return ExpressionBuilder(expression.replace('√', '#').replace("ln", "log"))
            .operator(getFactorialOperator())
            .operator(getPercentOperator())
            .operator(getSqrtOperator())
            .build()
    }

    fun validateExpression(currentStr: String, newStr: String): Pair<Boolean, String> {
        val resultStrBuilder = StringBuilder(currentStr).append(newStr)
        val currentStrLastChar = currentStr[currentStr.length - 1]
        val newStrFirstChar = newStr[newStr.length - 1]
        if (!currentStrLastChar.isDigit() && !newStrFirstChar.isDigit()) {
            if ((currentStrLastChar.isMathOperation() && newStrFirstChar.isLeftBracket())
                || (currentStrLastChar.isRightBracket() && newStrFirstChar.isMathOperation())
                || (currentStrLastChar.isLeftBracket() && newStrFirstChar.isMinus())
                || (currentStrLastChar.isLetter() && newStrFirstChar.isMathOperation())
                || (currentStrLastChar.isFactorial() && newStrFirstChar.isMathOperation())
                || (currentStrLastChar.isDigit() && newStrFirstChar.isFactorial())
                || (currentStrLastChar.isMathOperation() && newStrFirstChar.isDot())
                || (currentStrLastChar.isPercent() && newStrFirstChar.isMathOperation())
                || (currentStrLastChar.isSqrt() && newStrFirstChar.isLeftBracket())
                || (currentStrLastChar.isLetter() && newStrFirstChar.isPercent())
                || (currentStrLastChar.isLetter() && newStrFirstChar.isPow())
                || (currentStrLastChar.isLetter() && newStrFirstChar.isFactorial())
                || (currentStrLastChar.isLeftBracket() && newStrFirstChar.isLetter())
                || (currentStrLastChar.isSqrt() && newStrFirstChar.isLetter())
                || (currentStrLastChar.isMathOperation() && newStrFirstChar.isSqrt())
                || (currentStrLastChar.isRightBracket() && newStrFirstChar.isPercent())
                || (currentStrLastChar.isRightBracket() && newStrFirstChar.isFactorial())
                || (currentStrLastChar.isRightBracket() && newStrFirstChar.isRightBracket())
                || (currentStrLastChar.isFactorial() && newStrFirstChar.isFactorial())
                || (currentStrLastChar.isLeftBracket() && newStrFirstChar.isLeftBracket())
                || (currentStrLastChar.isLetter() && newStrFirstChar.isRightBracket())
                || (currentStrLastChar.isPow() && newStrFirstChar.isRightBracket())
                || (currentStrLastChar.isPow() && newStrFirstChar.isLetter())
                || (currentStrLastChar.isMathOperation() && newStrFirstChar.isMathLetter())
            ) {
                return Pair(true, resultStrBuilder.toString())
            } else {
                return Pair(false, "")
            }
        }
        return Pair(true, resultStrBuilder.toString())
    }

    fun getNumberBeforeLastMathOperation(expression: String): String {
        with(expression) {
            val numberBuilder = java.lang.StringBuilder("")
            if (this.isNotEmpty() && length >= 2) {
                var lastChar = this[length - 1]
                if ((lastChar.isMathOperation() || lastChar.isFactorial() || lastChar.isPercent()) && this[length - 2].isDigit()) {
                    var i = length - 2
                    lastChar = this[i]

                    while (lastChar.isDigit() || lastChar == '.') {
                        if (i - 1 >= 0) {
                            numberBuilder.append(lastChar)
                            lastChar = this[--i]
                        } else {
                            numberBuilder.append(lastChar)
                            break
                        }
                    }
                }
            }
            return numberBuilder.toString().reversed()
        }
    }

    private fun getPercentOperator(): Operator {
        return object : Operator(
            "%",
            1,
            true,
            PRECEDENCE_POWER + 1
        ) {
            override fun apply(vararg args: Double): Double {
                return args[0] / 100
            }
        }
    }

    private fun getFactorialOperator(): Operator {
        return object : Operator(
            "!",
            1,
            true,
            PRECEDENCE_POWER + 1
        ) {
            override fun apply(vararg args: Double): Double {
                val arg = args[0].toInt()
                require(arg.toDouble() == args[0]) { "Operand for factorial has to be an integer" }
                require(arg >= 0) { "The operand of the factorial can not be less than zero" }
                var result = 1.0
                for (i in 1..arg) {
                    result *= i.toDouble()
                }
                return result
            }
        }
    }

    private fun getSqrtOperator(): Operator {
        return object : Operator(
            "#",
            1,
            true,
            PRECEDENCE_POWER + 1
        ) {
            override fun apply(vararg args: Double): Double {
                val arg = args[0]
                require(arg >= 0) { "The operand of the factorial can not be less than zero" }
                return sqrt(arg)
            }
        }
    }
}