package com.huydd2908.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var numbers = mutableListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private var operators = mutableListOf("+", "-", "x", "/")
    private var expression = ""
    private var temp1 = 0
    private var temp2 = 0
    private var operator = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textResult.visibility = View.GONE
        textExpression.text = getString(R.string.text_0)
    }

    override fun onClick(v: View?) {
        val clicked = if (v is Button) v else return
        checkError()
        when (val value = clicked.text.toString()) {
            in numbers -> {
                if (textResult.visibility == View.VISIBLE) {
                    reset()
                }
                expression += value
                textExpression.text = expression
            }
            in operators -> {
                textResult.visibility = View.GONE
                if (expression.takeLast(1) in operators) {
                    expression = expression.dropLast(1)
                    operator = ""
                }
                if (operator != "") {
                    expression = calculate()
                    operator = ""
                    checkError()
                }
                expression += value
                operator = value
                textExpression.text = expression
            }
            getString(R.string.button_show_result) -> {
                if (operator != "") {
                    expression = calculate()
                    operator = ""
                    textResult.visibility = View.VISIBLE
                    textResult.text = expression
                }
            }
            getString(R.string.button_clear) -> reset()
        }
    }

    private fun reset() {
        textExpression.text = getString(R.string.text_0)
        textResult.visibility = View.GONE
        expression = ""
        temp1 = 0
        temp2 = 0
        operator = ""
    }

    private fun calculate(): String {
        val i = expression.lastIndexOf(operator)
        try {
            temp1 = expression.substring(0, i).toInt()
            temp2 = expression.substring(i + 1, expression.length).toInt()
        } catch (e: NumberFormatException) {
            reset()
        }
        return when (operator) {
            getString(R.string.button_minus) -> (temp1 - temp2).toString()
            getString(R.string.button_multiply) -> (temp1 * temp2).toString()
            getString(R.string.button_divide) -> {
                if (temp2 != 0) (temp1 / temp2).toString()
                else getString(R.string.divide_0)
            }
            else -> (temp1 + temp2).toString()
        }
    }

    private fun checkError() {
        if (expression == getString(R.string.divide_0)) {
            reset()
        }
    }
}
