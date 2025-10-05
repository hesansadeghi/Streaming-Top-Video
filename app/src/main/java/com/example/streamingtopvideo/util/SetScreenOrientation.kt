package com.example.streamingtopvideo.util

import android.app.Activity

fun setScreenOrientation(activity: Activity, orientation: Int) {

    activity.requestedOrientation = orientation
}