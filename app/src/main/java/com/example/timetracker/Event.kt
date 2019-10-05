package com.example.timetracker

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.*

class Event(startTime:Calendar,endTime:Calendar,context:Context){

    private var startTime:Calendar?=null;
    private var endTime:Calendar?=null;
    private var duration:Long?=null;
    private var eventName:String?=null;
    private val calID:Long = 1;

    init{
        this.startTime=startTime;
        this.endTime = endTime;
        this.duration=calculateDurationInSec(startTime,endTime);
        //createCalendarEvent(context);
    }

    fun getEventName(name:String){
        this.eventName = name;
    }

    private fun calculateDurationInSec(start:Calendar,end:Calendar):Long{
        return (end.timeInMillis-start.timeInMillis)/1000;
    }

    private fun calculateDurationInMin(start:Calendar,end:Calendar):Long{
        return calculateDurationInSec(start,end)/60;
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