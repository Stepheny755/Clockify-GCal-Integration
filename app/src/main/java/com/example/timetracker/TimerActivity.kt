package com.example.timetracker

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.view.MenuItem
import android.os.SystemClock
import androidx.appcompat.app.AlertDialog
import java.util.Calendar
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alertdialog.view.*

class TimerActivity : AppCompatActivity() {

    private var running:Boolean = false;
    private var pauseOffSet:Long = 0L;
    private var lastStart:Calendar?= null;
    private var lastEnd:Calendar?= null;
    private var lastEvent:String?= null;
    private var newEvent:Event?=null;

    //private val

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        chronometer_start.setOnClickListener{
            if(!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffSet;
                chronometer.start()
                running = true;
                lastStart = Calendar.getInstance();
                textView.text=lastStart.toString();
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
            if(running){
                lastEnd = Calendar.getInstance();
                //textView.text=lastEnd.toString();
                lastEvent = showEditTextDialog(this);
                newEvent = Event(lastStart!!,lastEnd!!,this);
            }
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

    private fun showEditTextDialog(context:Context): String{
        val builder = AlertDialog.Builder(context);
        val inflater = layoutInflater
        builder.setTitle("Set Event Name");
        //builder.setMessage("Please enter the name of this event")
        val dialogLayout = inflater.inflate(R.layout.alertdialog,null);
        val editText = dialogLayout.editText
        builder.setView(dialogLayout);
        builder.setPositiveButton("Done") { _, _ -> Toast.makeText(applicationContext, "Event Name" + editText.text.toString(), Toast.LENGTH_SHORT).show(); newEvent?.getEventName(editText.text.toString()) ;newEvent?.createCalendarEvent(this) };
        builder.setNegativeButton("Cancel"){ _, _ -> return@setNegativeButton };
        builder.show();
        return editText.text.toString();
    }

}
