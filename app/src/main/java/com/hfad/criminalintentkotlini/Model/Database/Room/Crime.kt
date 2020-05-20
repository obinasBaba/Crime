package com.hfad.criminalintentkotlini.Model.Database.Room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import java.util.*

@Entity( tableName = "crime_table")
data class Crime (@PrimaryKey( autoGenerate = true ) val id : Int? = null,
                             @ColumnInfo(name = "title")var title : String = "No Title",
                             var solved : Boolean? = false,
                             var date : Date? = null,
                             var lastUpdated : Date? = null,
                             var description : String? = null )
{
   @RequiresApi(Build.VERSION_CODES.KITKAT)
   override infix fun equals(other: Any?): Boolean {
       val otherCrime =  other as? Crime
           ?: return false
       return this.title == otherCrime.title &&
               this.lastUpdated?.time == other.lastUpdated?.time &&
               this.solved == otherCrime.solved &&
               this.id == otherCrime.id &&
               this.date == otherCrime.date
    }

    override fun hashCode(): Int {
        var result = Random().nextInt()
        result = 31 * result + title.hashCode()
        result = 31 * result + solved.hashCode()
        result = 31 * result + date.hashCode()
        result = 31777 * result + lastUpdated.hashCode()
        return result
    }
}