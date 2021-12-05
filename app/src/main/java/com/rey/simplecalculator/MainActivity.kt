package com.rey.simplecalculator

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    // create nullables variable
    private var tvInput: TextView? = null

    // if dot already there, it will not add another dot
    var lastNumeric : Boolean = false
    var lastDot : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
    }

    fun vibratePhone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(30)
        }
    }

    // whenever onDigit called, view will be the number
    fun onDigit(view: View){
        // tvInput? : if null, it will not append
        //              converts view Button into text
        tvInput?.append((view as Button).text)
        // flags for decimal dot
        lastNumeric = true
        lastDot = false
        vibratePhone()
    }

    // clear function
    fun onClear(view: View){
        tvInput?.text = ""
        vibratePhone()
    }

    fun onBack(view: View){
        val back = tvInput?.text.toString()
        if(back.isNotEmpty())
            tvInput?.text = back.substring(0, back.length - 1)
        vibratePhone()
    }

    // if dot already there, it will not add another dot
    fun onDecimalPoint(view: View){
        // if lastNumeric = true and lastDot = false
        if(lastNumeric && !lastDot){
            tvInput?.append(".")
            // flags to know which one active
            lastNumeric = false
            lastDot = true
        }
        vibratePhone()
    }

    fun onOperator(view: View){
        // if tvInput and text is not empty (null safety)...
        tvInput?.text?.let{
            // ...do this
            if(lastNumeric && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)

                lastNumeric = false
                lastDot = false
            }
        }
        vibratePhone()
    }

    fun onEqual(view: View){
        // if lastNumeric = true
        if(lastNumeric){
            //                     getText() converted to String
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try{
                // makes sure that the value not having a minus
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    // get rid of the first entry in the string (-99 to 99 {index0 = -, index1 = 9})
                    tvValue = tvValue.substring(1)
                }

                // SUBTRACT OPERATION

                if(tvValue.contains("-")) {
                    // will use dash to split value (99-1 to 99 and 1)
                    val splitValue = tvValue.split("-")

                    var one = splitValue[0] // 99
                    val two = splitValue[1] // 1

                    // conmbines prefix with number
                    if (prefix.isNotEmpty())
                        one = prefix + one

                    // fun -> string to double -> substract -> double to string -> put string into tvInput
                    tvInput?.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())

                // ADDITION OPERATION

                }else if(tvValue.contains("+")) {
                    // will use dash to split value (99+1 to 99 and 1)
                    val splitValue = tvValue.split("+")

                    var one = splitValue[0] // 99
                    val two = splitValue[1] // 1

                    // conmbines prefix with number
                    if (prefix.isNotEmpty())
                        one = prefix + one

                    // fun -> string to double -> addition -> double to string -> put string into tvInput
                    tvInput?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())

                // MULTIPLICATION OPERATION

                }else if(tvValue.contains("*")) {
                    // will use dash to split value (99*1 to 99 and 1)
                    val splitValue = tvValue.split("*")

                    var one = splitValue[0] // 99
                    val two = splitValue[1] // 1

                    // conmbines prefix with number
                    if (prefix.isNotEmpty())
                        one = prefix + one

                    // fun -> string to double -> multiplication -> double to string -> put string into tvInput
                    tvInput?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())

                // DIVIDE OPERATION

                }else if(tvValue.contains("/")) {
                    // will use dash to split value (99-1 to 99 and 1)
                    val splitValue = tvValue.split("/")

                    var one = splitValue[0] // 99
                    val two = splitValue[1] // 1

                    // conmbines prefix with number
                    if (prefix.isNotEmpty())
                        one = prefix + one

                    // fun -> string to double -> divide -> double to string -> put string into tvInput
                    tvInput?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                }
            // catch divide by zero and other stuff that can't be solved mathematicaly
            }catch (e: ArithmeticException){
                e.printStackTrace()
                }
            }
        vibratePhone()
        }

    private fun removeZeroAfterDot(result: String) : String{
        var value = result
        if(result.contains(".0"))
            // 90.0 to 90               removes ".0" (-2)
            value = result.substring(0, result.length -2)
        return value
    }

    private fun isOperatorAdded(value: String) : Boolean{
        // if "-" at the beginnig tvResult, it will ignore
        // purpose: so calc result can display negative value
        return if(value.startsWith("-")){
            false
        }else{
            // if there's /,*,+,-  it will be true statement
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }
}