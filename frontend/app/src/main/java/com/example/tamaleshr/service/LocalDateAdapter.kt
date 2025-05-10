package com.example.tamaleshr.service
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @ToJson
    fun toJson(localDate: LocalDate): String {
        return localDate.format(formatter)
    }

    @FromJson
    fun fromJson(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }
}