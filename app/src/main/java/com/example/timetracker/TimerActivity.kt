package com.example.timetracker

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.content.Context

import kotlinx.android.synthetic.main.activity_main.*

class TimerActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: Stopwatch;
    private var timerLengthSeconds = 0L;
    private var timerState = TimerState.Stopped;
    private var secondsRemaining = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title= "     Timer"
        fab_start.setOnClickListener { v ->
            startTimer();
            timerState = TimerState.Running;
            updateButtons();
        }
        fab_pause.setOnClickListener { v ->
            timer.cancel();
            timerState = TimerState.Paused;
            updateButtons();
        }
        fab_stop.setOnClickListener { v ->
            timer.cancel();
            onTimerFinished();
        }
    }

    override fun onResume(){
        super.onResume();
        initTimer();

        //TODO: Remove background timer, hide notifications
    }

    override fun onPause(){
        if(timerState==TImerState.Running){
            timer.cancel()
            //TODO: start background timer and show notification
        }
        else if(timerState==TImerState.Paused){
            //TODO: show notification
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
