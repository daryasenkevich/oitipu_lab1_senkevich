package com.example.calculator_kotlin.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator_kotlin.R
import com.example.calculator_kotlin.model.MathExpressionManager
import com.example.calculator_kotlin.util.isMathOperation
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.round


@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class MainActivity : AppCompatActivity() {

    private val mathExpressionManager = MathExpressionManager()

    companion object {
        private const val EXPRESSION_TEXT_KEY = "EXPRESSION_TEXT_KEY"
        private const val RESULT_TEXT_KEY = "RESULT_TEXT_KEY"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            val expressionText = savedInstanceState.getString(EXPRESSION_TEXT_KEY) ?: ""
            val resultText = savedInstanceState.getString(RESULT_TEXT_KEY) ?: ""
            setExpressionData(expressionText, resultText)
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setEngineerMathOperationListeners()
        }

        setDefaultOperationListeners()
    }

    private fun setEngineerMathOperationListeners() {
        sin_btn.setOnClickListener { setTextFields("sin(") }
        cos_btn.setOnClickListener { setTextFields("cos(") }
        tan_btn.setOnClickListener { setTextFields("tan(") }
        log_btn.setOnClickListener { setTextFields("log10(") }
        ln_btn.setOnClickListener { setTextFields("ln(") }
        ten_power_x_btn.setOnClickListener { setTextFields("10^(") }
        one_dev_x_btn.setOnClickListener { setTextFields("^(-1)") }
        x_power_y_btn.setOnClickListener { setTextFields("^") }
        x_power_3_btn.setOnClickListener { setTextFields("^3") }
        x_power_2_btn.setOnClickListener { setTextFields("^2") }
        pi_btn.setOnClickListener { setTextFields("π") }
        exp_btn.setOnClickListener { setTextFields("e") }
        percent_btn.setOnClickListener { setTextFields("%") }
        factorial_btn.setOnClickListener { setTextFields("!") }
        radical_btn.setOnClickListener { setTextFields("√") }
    }

    private fun setDefaultOperationListeners() {
        btn_0.setOnClickListener { setTextFields("0") }
        btn_1.setOnClickListener { setTextFields("1") }
        btn_2.setOnClickListener { setTextFields("2") }
        btn_3.setOnClickListener { setTextFields("3") }
        btn_4.setOnClickListener { setTextFields("4") }
        btn_5.setOnClickListener { setTextFields("5") }
        btn_6.setOnClickListener { setTextFields("6") }
        btn_7.setOnClickListener { setTextFields("7") }
        btn_8.setOnClickListener { setTextFields("8") }
        btn_9.setOnClickListener { setTextFields("9") }
        minus_btn.setOnClickListener { setTextFields("-") }
        plus_btn.setOnClickListener { setTextFields("+") }
        dev_btn.setOnClickListener { setTextFields("/") }
        mult_btn.setOnClickListener { setTextFields("*") }
        close_bracket.setOnClickListener { setTextFields(")") }
        open_bracket.setOnClickListener { setTextFields("(") }

        all_clean_btn.setOnClickListener {
            math_operation.text = ""
            result_text.text = ""
        }

        back_btn.setOnClickListener {
            val str = math_operation.text.toString()
            if (str.isNotEmpty())
                math_operation.text = str.substring(0, str.length - 1)

            result_text.text = ""
        }

        dot_btn.setOnClickListener {
            val str = math_operation.text.toString()
            if (str.isNotEmpty()) {
                if (str[str.length - 1].isMathOperation()) {
                    setTextFields("0.")
                } else {
                    setTextFields(".")
                }
            } else {
                setTextFields("0.")
            }
        }

        equal_btn.setOnClickListener {
            try {

                val expressionEvaluator =
                    mathExpressionManager.configExpressionEvaluator(math_operation.text.toString())

                val result = expressionEvaluator.evaluate()

                val longRes = result.toLong()
                if (longRes.toDouble() == result) {
                    //пишем целое число
                    result_text.text = longRes.toString()
                } else {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        result_text.text = (round(result * 1000000) / 1000000).toString()
                    }
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        result_text.text = result.toString()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Ошибка", "сообщение: ${e.message}")
                Toast.makeText(this, "Неверное выражение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setExpressionData(expressionText: String, resultText: String) {
        if (expressionText.isNotEmpty()) {
            math_operation.text = expressionText
        }

        if (resultText.isNotEmpty()) {
            result_text.text = resultText
        }
    }

    private fun setTextFields(str: String) {
        if (result_text.text.isNotEmpty()) {
            math_operation.text = result_text.text
            result_text.text = ""
        }

        if (math_operation.text.isNotEmpty()) {
            val validationResultPair =
                mathExpressionManager.validateExpression(math_operation.text.toString(), str)
            if (validationResultPair.first) {
                val newExpression = validationResultPair.second
                math_operation.text = newExpression
            }
        } else {
            math_operation.append(str)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXPRESSION_TEXT_KEY, math_operation.text.toString())
        outState.putString(RESULT_TEXT_KEY, result_text.text.toString())
    }
}
