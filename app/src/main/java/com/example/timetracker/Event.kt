package com.example.timetracker

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.*

class Event(startTime:Calendar,endTime:Calendar,duration:Long,context:Context){

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
        this.totalDuration=calculateDurationInSec(startTime,endTime);
        this.activeDuration=calculateDurationInSec(0,duration);
        this.passiveDuration=calculateDurationInSec(this.activeDuration!!,this.totalDuration!!);
        //createCalendarEvent(context);
    }

    fun getEventName(name:String){
        this.eventName = name;
    }

    private fun calculateDurationInSec(start:Calendar,end:Calendar):Long{
        return (end.timeInMillis-start.timeInMillis)/1000;
    }

    private fun calculateDurationInSec(start:Long=0,end:Long):Long{
        return (end-start)/1000;
    }

    private fun calculateDurationInMin(start:Calendar,end:Calendar):Long{
        return calculateDurationInSec(start,end)/60;
    }

    private fun millisToTime(millis:Long){

    }

    fun createCalendarEvent(context:Context){
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime?.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime?.timeInMillis)
            .putExtra(CalendarContract.Events.TITLE,eventName)
        context.startActivity(intent)
    }
}