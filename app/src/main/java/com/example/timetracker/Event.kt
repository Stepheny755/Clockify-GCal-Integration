package com.example.timetracker

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.*

class Event(startTime:Calendar,endTime:Calendar,duration:Long){

    private var startTime:Calendar?=null;
    private var endTime:Calendar?=null;
    private var activeDuration:Long?=null;
    private var passiveDuration:Long?=null;
    private var totalDuration:Long?=null;
    private var eventName:String?=null;
    private val calID:Long = 1;

    init{
        this.startTime=startTime;
        this.endTime = endTime;
        this.totalDuration=calculateDuration(startTime,endTime);
        this.activeDuration=calculateDuration(0,duration);
        this.passiveDuration=calculateDuration(this.activeDuration!!,this.totalDuration!!);
        //createCalendarEvent(context);
    }

    fun getEventActiveDuration():Long?{ return (this.activeDuration!!)/1000; }
    fun getEventPassiveDuration():Long?{ return (this.passiveDuration!!)/1000; }
    fun getEventTotalDuration():Long?{ return (this.totalDuration!!)/1000; }

    fun setEventName(name:String){
        this.eventName = name;
    }

    private fun calculateDuration(start:Calendar,end:Calendar):Long{
        return (end.timeInMillis-start.timeInMillis);
    }

    private fun calculateDuration(start:Long=0,end:Long):Long{
        return (end-start);
    }

    private fun millisToTime(millis:Long):String{
        return millis.toString();//String.format("%02d:%02d:%02d",millis/(1000*60*60),millis/(1000*60)%60,millis/(1000)%60)
    }

    fun createCalendarEvent(context:Context){
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime?.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime?.timeInMillis)
            .putExtra(CalendarContract.Events.TITLE,eventName)
            .putExtra(CalendarContract.Events.DESCRIPTION,millisToTime(passiveDuration!!))
        context.startActivity(intent)
    }
}