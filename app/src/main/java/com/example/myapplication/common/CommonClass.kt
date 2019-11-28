package com.example.myapplication.common

import android.content.Context
import android.net.ConnectivityManager


/**
 * Created by Neeta on 11/24/2019.
 */
class CommonClass {

    companion object {
        fun isOnline(context: Context?): Boolean {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        }
    }

}