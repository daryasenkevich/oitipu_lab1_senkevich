package com.example.calculator_kotlin.util

import java.lang.StringBuilder

fun Char.isMathOperation(): Boolean {
    return (this == '+' || this == '*' || this == '/' || this == '-')
}

fun Char.isRightBracket(): Boolean {
    return (this == ')')
}

fun Char.isLeftBracket(): Boolean {
    return (this == '(')
}

fun Char.isMinus(): Boolean {
    return (this == '-')
}

fun Char.isMathLetter(): Boolean {
    return (this == 'e' || this == 'π')
}

fun Char.isFactorial(): Boolean {
    return (this == '!')
}

fun Char.isPercent(): Boolean {
    return (this == '%')
}

fun Char.isDot(): Boolean {
    return (this == '.')
}

fun Char.isPow(): Boolean {
    return (this == '^')
}

fun Char.isSqrt(): Boolean {
    return (this == '√')
}