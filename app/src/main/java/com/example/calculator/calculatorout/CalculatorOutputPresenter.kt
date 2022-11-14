package com.example.calculator.calculatorout

import android.util.Log
import bsh.Interpreter
import java.text.DecimalFormat

object CalculatorOutputPresenter {
    //Current attached view
    private var mView : CalculatorOutputInterfaceView? = null

    //Current equation
    private var mCurrentEquation: String = ""

    //Current outcome
    private var mCurrentOutcome: String = ""

    //Interpreter
    private val mInterpreter = Interpreter()

    //operators
    private val operators = setOf("+", "-", "*", "/", "%")

    //Decimal format
    private var decimalFormat = DecimalFormat()

    fun attach(view : CalculatorOutputInterfaceView){
        decimalFormat.isDecimalSeparatorAlwaysShown = false
        decimalFormat.isGroupingUsed = false

        mView = view
        updateEquation()
        updateOutcome()
    }

    fun detach(){
        mView = null
    }

    fun add(item : String){
        //If item is an operator, don't add to equation if last item is also an operator
        if(mCurrentEquation.isNotEmpty() && operators.contains(item) && operators.contains(
                mCurrentEquation[mCurrentEquation.length - 1].toString())){
            return
        }

        mCurrentEquation = mCurrentEquation.plus(item)
        updateEquation()
        calculateOutcome()
        updateOutcome()
    }

    fun remove(){
        mCurrentEquation = if(mCurrentEquation.length > 1){
            mCurrentEquation.substring(0, mCurrentEquation.length - 1)
        } else{
            ""
        }
        updateEquation()
        calculateOutcome()
        updateOutcome()
    }

    fun solve(){
        if(mCurrentEquation.isNotEmpty()){
            mCurrentEquation = mCurrentOutcome
            mCurrentOutcome = ""
        }
        updateEquation()
        updateOutcome()
    }

    fun clear(){
        mCurrentEquation = ""
        mCurrentOutcome = ""
        updateEquation()
        updateOutcome()
    }

    private fun calculateOutcome(){
        if(mCurrentEquation.isNotEmpty()){
            try {
                mInterpreter.eval("result = ${mCurrentEquation}D")
                val result = mInterpreter.get("result")

                if(result != null){
                    mCurrentOutcome = decimalFormat.format(result).toString()

                }
            } catch (e : Exception){
                Log.d("Presenter", "calculateOutcome: exception $e")
                mCurrentOutcome = ""
            }
        } else{
            mCurrentOutcome = ""
        }
    }

    private fun updateEquation(){
        mView?.setEquation(mCurrentEquation)
    }

    private fun updateOutcome(){
        mView?.setOutcome(mCurrentOutcome)
    }
}