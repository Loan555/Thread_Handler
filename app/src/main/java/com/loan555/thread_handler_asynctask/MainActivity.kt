package com.loan555.thread_handler_asynctask

import android.graphics.Color
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var count: Int = 0
    var flag: Boolean = false
    var flag_change: Boolean = false


    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1002 -> {
                    text.setText(msg.arg1.toString())
                }
                1001 -> {
                    text.setTextColor(msg.arg1)
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_POINTER_DOWN -> {

                        Log.d("aaa","pointer down")
                    }
                    MotionEvent.ACTION_POINTER_UP -> {

                    Log.d("aaa","pointer up")
                }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        btClickUp.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        doCountUp()
                        setTextColor()
                    }
                    MotionEvent.ACTION_UP -> {
                        doCountReset()
                        setTextColor()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        btClickDown.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        setTextColor()
                        doCountDown()
                    }
                    MotionEvent.ACTION_UP -> {
                        setTextColor()
                        doCountReset()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    fun setTextColor() {
        var threadChangeColor = Thread(Runnable {
            while (flag_change) {

                var color: Int =
                    Color.argb(255, (0..255).random(), (0..255).random(), (0..255).random())
                var msg2 = Message()
                msg2.what = 1001
                msg2.arg1 = color
                handler.sendMessage(msg2)
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    Log.d("aaa", e.toString())
                }
            }
        })
        threadChangeColor.start()
    }

    fun doCountUp() {
        val threadCountUp = Thread(Runnable {
            while (flag) {
                count++//tang len 1
                var msg = Message()//gui count di
                msg.what = 1002
                msg.arg1 = count

                handler.sendMessage(msg)
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    Log.d("aaa", e.toString())
                }
            }
            flag_change = false
        })
        threadCountUp.start()
        flag = true
        flag_change = true
        Log.d(
            "aaa",
            "press, thread cout up is +${threadCountUp.isAlive.toString()}"
        )
    }


    fun doCountDown() {
        val threadCountDown = Thread(Runnable {
            while (flag) {
                count-- //giam 1
                var msg = Message()//gui count di
                msg.what = 1002
                msg.arg1 = count
                handler.sendMessage(msg)
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    Log.d("aaa", e.toString())
                }
            }
            flag_change = false
        })
        threadCountDown.start()
        flag = true
        flag_change = true
        Log.d(
            "aaa",
            "press, thread cout up is +${threadCountDown.isAlive.toString()}"
        )
    }

    fun doCountReset() {

        flag = false
        val threadCountReset = Thread(Runnable {
            Thread.sleep(1000)
            while (!flag && count != 0) {
                if (count > 0) {
                    flag_change = true
                    count-- // giam 1
                     }
                else if (count < 0) {
                        flag_change = true
                        count++
                    }
                var msg = Message()//gui count di
                msg.what = 1002
                msg.arg1 = count
                handler.sendMessage(msg)
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    Log.d("aaa", e.toString())
                }
            }
            flag_change = false
        })
        threadCountReset.start()
    }

}
