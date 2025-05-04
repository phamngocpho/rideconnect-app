package com.rideconnect.util.map

import android.content.Context
import com.rideconnect.BuildConfig
import java.io.IOException
import java.util.Properties

object MapBoxConfig {
    fun getMapBoxAccessToken(context: Context): String {
        return try {
            if (BuildConfig::class.java.fields.any { it.name == "MAPBOX_ACCESS_TOKEN" }) {
                val field = BuildConfig::class.java.getField("MAPBOX_ACCESS_TOKEN")
                field.get(null) as String
            } else {
                getTokenFromProperties(context) ?: throw SecurityException("MapBox token not found")
            }
        } catch (e: Exception) {
            getTokenFromProperties(context) ?: throw SecurityException("MapBox token not found")
        }
    }

    private fun getTokenFromProperties(context: Context): String? {
        return try {
            val properties = Properties()
            val assetManager = context.assets
            val inputStream = assetManager.open("local.properties")
            properties.load(inputStream)
            properties.getProperty("MAPBOX_ACCESS_TOKEN")
        } catch (e: IOException) {
            null
        }
    }
}
