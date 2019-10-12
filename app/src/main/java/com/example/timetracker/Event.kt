package com.example.timetracker

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.*
import kotlin.math.round

class Event(startTime:Calendar,endTime:Calendar,duration:Long){

    private var startTime:Calendar?=null
    private var endTime:Calendar?=null
    private var activeDuration:Long?=null
    private var passiveDuration:Long?=null
    private var totalDuration:Long?=null
    private var eventName:String?=null
    private val calID:Long = 1

    init{
        this.startTime=startTime
        this.endTime = endTime
        this.totalDuration=calculateDuration(startTime,endTime)
        this.activeDuration=calculateDuration(0,duration)
        this.passiveDuration=calculateDuration(this.activeDuration!!,this.totalDuration!!)
        if(this.totalDuration!!<this.activeDuration!!){
            this.totalDuration=this.activeDuration;
        }
    }

    fun millisToTime(millis:Long):String{
        return String.format("%02d:%02d:%02d",millis/(1000*60*60),millis/(1000*60)%60,millis/(1000)%60)
    }

    fun getEventActiveDuration():Long?{ return (this.activeDuration!!)/1000 }
    fun getEventPassiveDuration():Long?{ return (this.passiveDuration!!)/1000 }
    fun getEventTotalDuration():Long?{ return (this.totalDuration!!)/1000 }

    fun getEventActiveDurationTime():String?{ return millisToTime(this.activeDuration!!) }
    fun getEventPassiveDurationTime():String?{ return millisToTime(this.passiveDuration!!) }
    fun getEventTotalDurationTime():String?{ return millisToTime(this.totalDuration!!) }

    fun getPercentActive():Double? {
        if (this.activeDuration!!/1000 == 0L) {
            return 0.0;
        } else {
            return round((this.activeDuration!!.toDouble()) / (this.totalDuration!!) * 100);
        }
    }

    fun getPercentPassive():Double?{
        if (this.passiveDuration!!/1000 == 0L) {
            return 0.0;
        } else {
            return round((this.passiveDuration!!.toDouble()) / (this.totalDuration!!) * 100);
        }
    }

    fun setEventName(name:String){
        this.eventName = name
    }

    private fun calculateDuration(start:Calendar,end:Calendar):Long{
        return (end.timeInMillis-start.timeInMillis)
    }

    private fun calculateDuration(start:Long=0,end:Long):Long{
        return (end-start)
    }

    fun createDescriptionString():String{
        var description:String = "Total Duration:      "+getEventTotalDurationTime()
        description+="\nActive Duration:    "+getEventActiveDurationTime()+" "+getPercentActive()+"%"
        description+="\nPassive Duration: "+getEventPassiveDurationTime()+" "+getPercentPassive()+"%"
        return description
    }

    fun createCalendarEvent(context:Context){
        val desc = createDescriptionString()
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startTime?.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime?.timeInMillis)
            .putExtra(CalendarContract.Events.TITLE,eventName)
            .putExtra(CalendarContract.Events.DESCRIPTION,desc)
        context.startActivity(intent)
    }
}