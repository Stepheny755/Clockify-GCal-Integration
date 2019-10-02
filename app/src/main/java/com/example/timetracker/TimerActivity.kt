package com.example.timetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.view.MenuItem
import android.os.SystemClock


import kotlinx.android.synthetic.main.activity_main.*

class TimerActivity : AppCompatActivity() {

    private var running:Boolean = false;
    private var pauseOffSet:Long = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        chronometer_start.setOnClickListener{
            if(!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffSet;
                chronometer.start()
                running=true;
            }
        }

        chronometer_pause.setOnClickListener{
            if(running) {
                chronometer.stop();
                pauseOffSet = SystemClock.elapsedRealtime() - chronometer.base;
                running=false;
            }
        }

        chronometer_stop.setOnClickListener{
            chronometer.stop();
            chronometer.base = SystemClock.elapsedRealtime();
            pauseOffSet = 0;
            running=false;
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
