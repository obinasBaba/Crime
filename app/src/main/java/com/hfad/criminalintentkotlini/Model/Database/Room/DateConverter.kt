package com.hfad.criminalintentkotlini.Model.Database.Room

import androidx.room.TypeConverter
import java.util.*


class DateConverter
{
    @TypeConverter
    fun fromTimeStamp( timeStamp : Long? ) : Date? =
        timeStamp?.let { Date( it ) }

    @TypeConverter
    fun toTimeStamp( date : Date? ) : Long? =
        date?.time
}