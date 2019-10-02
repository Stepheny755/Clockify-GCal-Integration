package com.example.timetracker.util

class Util{
    companion object {
        fun mToS(minutes: Int): Int {
            return minutes * 60;
        }

        fun sToM(seconds: Int): Int {
            return seconds / 60;
        }
    }
}
