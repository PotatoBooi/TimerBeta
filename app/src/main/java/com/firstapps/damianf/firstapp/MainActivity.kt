package com.firstapps.damianf.firstapp

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var timerState: TimerState = TimerState.STOPPED
    private var timer: CountDownTimer? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonStart -> startTimer()
            R.id.buttonStop -> stopTimer()
            R.id.buttonPause -> pauseTimer()
            R.id.buttonPlus1 -> changeDisplay(textViewMinutesDec)
            R.id.buttonPlus2 -> changeDisplay(textViewMinutes)
            R.id.buttonPlus3 -> changeDisplay(textViewSecondsDec)
            R.id.buttonPlus4 -> changeDisplay(textViewSeconds)
            R.id.buttonMinus1 -> changeDisplay(textViewMinutesDec, true)
            R.id.buttonMinus2 -> changeDisplay(textViewMinutes, true)
            R.id.buttonMinus3 -> changeDisplay(textViewSecondsDec, true)
            R.id.buttonMinus4 -> changeDisplay(textViewSeconds, true)
        }
    }

    private fun pauseTimer() {
        if (timerState != TimerState.PAUSED) {
            timerState = TimerState.PAUSED
            timer?.cancel()
        }
    }

    private fun stopTimer() {
        if (timerState == TimerState.STOPPED || timerState == TimerState.PAUSED) {
            resetDisplay()
        }
        if (timerState == TimerState.RUNNING) {
            resetDisplay()
            timerState = TimerState.STOPPED
            timer?.cancel()
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
            val milis = minutes * 60 * 1000 + seconds * 1000 + 1200L
            timer = object : CountDownTimer(milis, 1000) {
                override fun onFinish() {
                    timerState = TimerState.STOPPED

                }

                override fun onTick(millisUntilFinished: Long) {
                    println(millisUntilFinished)
                    if (millisUntilFinished > 2000L) {
                        updateDisplay(millisUntilFinished - 1000L)

                    } else {
                        Toast.makeText(this@MainActivity, "Czas minął!", Toast.LENGTH_SHORT).show()
                        stopTimer()
                    }
                }
            }.start()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            val display = savedInstanceState.getStringArrayList("DISPLAY_VALUES")
            textViewMinutesDec.text = display?.get(0)
            textViewMinutes.text = display?.get(1)
            textViewSecondsDec.text = display?.get(2)
            textViewSeconds.text = display?.get(3)
            val state = savedInstanceState.getSerializable("STATE") as TimerState

            if (state == TimerState.RUNNING) {
                startTimer()
            } else {
                timerState = state
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putStringArrayList(
                "DISPLAY_VALUES",
                arrayListOf(
                    textViewMinutesDec.text.toString(),
                    textViewMinutes.text.toString(),
                    textViewSecondsDec.text.toString(),
                    textViewSeconds.text.toString()
                )
            )
            putSerializable("STATE", timerState)
            timer?.cancel()
        }
        super.onSaveInstanceState(outState)
    }

    private fun changeDisplay(textView: TextView, remove: Boolean = false) {
        if (timerState == TimerState.STOPPED) {
            val evalSign = if (remove) -1 else 1
            val cantRemove = (textView.text == "0" && remove)
            val value = textView.text.toString().toInt()

            if (cantRemove) {
                when (textView.id) {
                    R.id.textViewMinutesDec -> {
                        textView.text = "9"
                    }

                    R.id.textViewMinutes -> {
                        textView.text = "9"
                    }

                    R.id.textViewSecondsDec -> {
                        textView.text = "5"
                    }

                    R.id.textViewSeconds -> {
                        textView.text = "9"
                    }

                }
            } else {

                when (textView.id) {
                    R.id.textViewMinutesDec -> {

                        if (value == 9 && !remove) {
                            textView.text = "0"
                        } else {
                            textView.text = (value + evalSign * 1).toString()
                        }
                    }

                    R.id.textViewMinutes -> {
                        if (value == 9 && !remove) {
                            textView.text = "0"
                        } else {
                            textView.text = (value + evalSign * 1).toString()
                        }
                    }

                    R.id.textViewSecondsDec -> {
                        if (value == 5 && !remove) {
                            textView.text = "0"
                        } else {
                            textView.text = (value + evalSign * 1).toString()
                        }
                    }

                    R.id.textViewSeconds -> {
                        if (value == 9 && !remove) {
                            textView.text = "0"
                        } else {
                            textView.text = (value + evalSign * 1).toString()
                        }
                    }


                }
            }
        }
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
