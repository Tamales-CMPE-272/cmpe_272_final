package com.example.tamaleshr.di

import android.annotation.SuppressLint
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShortDateJsonAdapter {

    @ToJson
    @Synchronized
    @Throws(IOException::class)
    fun toJson(date: Date?): String {
        return if (date != null) {
            DATE_FORMAT.format(date)
        } else {
            throw IOException("Date is not present")
        }
    }

    @FromJson
    @Synchronized
    @Throws(IOException::class)
    fun fromJson(reader: JsonReader): Date? {
        return try {
            DATE_FORMAT.parse(reader.nextString())
        } catch (e: ParseException) {
            throw IOException(e)
        }
    }

    companion object {
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    }
}