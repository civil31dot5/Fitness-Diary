package com.civil31dot5.fitnessdiary.data.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converter {

    @TypeConverter
    fun fromLocalDateTimeToString(dateTime: LocalDateTime): String {
        return  DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime)
    }

    @TypeConverter
    fun fromStringToLocalDateTime(string: String): LocalDateTime{
        return LocalDateTime.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(string))
    }

}