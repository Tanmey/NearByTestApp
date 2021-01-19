package org.codejudge.application.util

import android.content.Context
import android.net.ConnectivityManager

class Util {

    companion object {
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        //        47.6204,-122.3491
        fun getUrl(latitude: Double, longitude: Double): String {
            val googlePlaceUrl = StringBuilder("maps/api/place/nearbysearch/json")
                .append("?location=47.6204,-122.3491")
                .append("&radius=2500")
                .append("&type=restaurant")
                .append("&key=AIzaSyD0AQBJ_BwInY5Tv_0tqGPJIWL7FcllnH0")

            return googlePlaceUrl.toString()
        }

        fun getUrlForSearch(latitude: Double, longitude: Double, restaurantName: String): String {
            val googlePlaceUrl = StringBuilder("maps/api/place/nearbysearch/json")
                .append("?location=47.6204,-122.3491")
                .append("&keyword=:$restaurantName")
                .append("&radius=2500")
                .append("&type=restaurant")
                .append("&key=AIzaSyD0AQBJ_BwInY5Tv_0tqGPJIWL7FcllnH0")

            return googlePlaceUrl.toString()
        }

    }
}