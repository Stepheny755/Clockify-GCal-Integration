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
import android.R.menu
import android.view.MenuInflater
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class TimerActivity : AppCompatActivity() {

    private enum class State{
        Stopped,Paused,Running
    }

    private var state=State.Stopped
    private var pauseOffSet:Long = 0L
    private var lastStart:Calendar?= null
    private var lastEnd:Calendar?= null
    private var lastEvent:String?= null
    private var newEvent:Event?=null

    private lateinit var appBarConfiguration: AppBarConfiguration
    //private val

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        chronometer_start.setOnClickListener{
            if(state==State.Stopped){
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffSet
                chronometer.start()
                lastStart = Calendar.getInstance()
                state = State.Running
                //textView.text=lastStart.toString()
            }
            if(state==State.Paused){
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffSet
                chronometer.start()
                state = State.Running
            }
        }

        chronometer_pause.setOnClickListener{
            if(state==State.Running) {
                chronometer.stop()
                pauseOffSet = SystemClock.elapsedRealtime() - chronometer.base
                state=State.Paused
            }
        }

        chronometer_stop.setOnClickListener{
            //chronometer.base = SystemClock.elapsedRealtime()-pauseOffSet
            chronometer.stop()
            pauseOffSet = 0
            if(state==State.Running||state==State.Paused){
                lastEnd = Calendar.getInstance()
                lastEvent = showEditTextDialog(this)
                val dur:Long = SystemClock.elapsedRealtime()-chronometer.base
                newEvent = Event(lastStart!!,lastEnd!!,dur)
                textView.text=newEvent?.getEventTotalDuration().toString()
                textView2.text=newEvent?.getEventPassiveDuration().toString()
                textView3.text=newEvent?.getEventActiveDuration().toString()
            }
            chronometer.base = SystemClock.elapsedRealtime()
            state=State.Stopped
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putLong("chronometer_base",chronometer.base)
        outState.putLong("pauseOffset",pauseOffSet)
        outState.putSerializable("state",state)
        outState.putSerializable("startTime",lastStart)
        outState.putSerializable("endTime",lastEnd)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastStart = savedInstanceState.getSerializable("startTime") as? Calendar
        lastEnd = savedInstanceState.getSerializable("endTime") as? Calendar
        state = savedInstanceState.getSerializable("state") as State
        pauseOffSet = savedInstanceState.getLong("pauseOffset")
        chronometer.base = SystemClock.elapsedRealtime()
        if(state==State.Running){
            chronometer.base=savedInstanceState.getLong("chronometer_base")
            chronometer.start()
        }else if(state==State.Paused){
            chronometer.base= SystemClock.elapsedRealtime()-pauseOffSet
        }
    }

    private fun showEditTextDialog(context:Context): String{
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        builder.setTitle("Set Event Name")
        //builder.setMessage("Please enter the name of this event")
        val dialogLayout = inflater.inflate(R.layout.alertdialog,null)
        val editText = dialogLayout.editText
        builder.setView(dialogLayout)
        builder.setPositiveButton("Done") { _, _ -> Toast.makeText(applicationContext, "Event Name" + editText.text.toString(), Toast.LENGTH_SHORT).show(); newEvent?.setEventName(editText.text.toString()); newEvent?.createCalendarEvent(this) }
        builder.setNegativeButton("Cancel"){ _, _ -> return@setNegativeButton }
        builder.show()
        return editText.text.toString()
    }
}
