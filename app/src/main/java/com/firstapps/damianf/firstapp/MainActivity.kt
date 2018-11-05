package com.firstapps.damianf.firstapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var timerState: TimerState = TimerState.STOPPED
    private lateinit var timer: CountDownTimer


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonStart -> startTimer()
            R.id.buttonStop -> stopTimer()
            R.id.buttonPause -> pauseTimer()
            R.id.buttonPlus1 -> addValue(textViewMinutesDec)
            R.id.buttonPlus2 -> addValue(textViewMinutes)
            R.id.buttonPlus3 -> addValue(textViewSecondsDec)
            R.id.buttonPlus4 -> addValue(textViewSeconds)
            R.id.buttonMinus1 -> minValue(textViewMinutesDec)
            R.id.buttonMinus2 -> minValue(textViewMinutes)
            R.id.buttonMinus3 -> minValue(textViewSecondsDec)
            R.id.buttonMinus4 -> minValue(textViewSeconds)
        }
    }

    private fun pauseTimer() {
        if (timerState != TimerState.PAUSED) {
            timerState = TimerState.PAUSED
            timer.cancel()
        }
    }

    private fun stopTimer() {
        if (timerState == TimerState.STOPPED || timerState == TimerState.PAUSED) {
            resetDisplay()
        }
         if (timerState == TimerState.RUNNING) {
            resetDisplay()
            timerState = TimerState.STOPPED
            Toast.makeText(this@MainActivity,"Czas minął!",Toast.LENGTH_SHORT).show()
            timer.cancel()
        }
    }

    private fun resetDisplay() {
        textViewMinutes.text = "0"
        textViewMinutesDec.text = "0"
        textViewSecondsDec.text = "0"
        textViewSeconds.text = "0"
    }

    private fun startTimer() {
        if (timerState != TimerState.RUNNING) {
            val minutes = (textViewMinutesDec.text.toString() + textViewMinutes.text.toString()).toLong()
            val seconds = (textViewSecondsDec.text.toString() + textViewSeconds.text.toString()).toLong()
            timerState = TimerState.RUNNING
            val milis = minutes * 60 * 1000 + seconds * 1000 + 1000L
            timer = object : CountDownTimer(milis, 1000) {
                override fun onFinish() {
                    timerState = TimerState.STOPPED

                }

                override fun onTick(millisUntilFinished: Long) {
                    println(millisUntilFinished)
                    if(millisUntilFinished > 2000L){
                        updateDisplay(millisUntilFinished - 1000L)

                    }else {
                        stopTimer()
                    }
                }
            }.start()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {

        }
        super.onSaveInstanceState(outState)
    }

    private fun addValue(textView: TextView) {
        if (timerState == TimerState.STOPPED) {
            val temp = textView.text.toString().toInt()

            if (temp in 0..8) {
                textView.text = (temp + 1).toString()
            } else if (temp == 9) {
                textView.text = "0"
            }
        }
    }

    private fun minValue(textView: TextView) {
        if (timerState == TimerState.STOPPED) {
            val temp = textView.text.toString().toInt()

            if (temp != 0) {
                if (temp in 1..9) {
                    textView.text = (temp - 1).toString()

                }
            }
        }
    }

    private fun setDisplay(mins: String, sec: String) {

    }

    private fun updateDisplay(value: Long) {
        val seconds = (value.toInt() / 1000) % 60
        val minutes = (value.toInt() / 1000) / 60

        textViewSeconds.text = (seconds % 10).toString()
        textViewSecondsDec.text = (seconds / 10).toString()
        textViewMinutes.text = (minutes % 10).toString()
        textViewMinutesDec.text = (minutes / 10).toString()
    }

    private enum class TimerState {
        RUNNING, PAUSED, STOPPED
    }
}
