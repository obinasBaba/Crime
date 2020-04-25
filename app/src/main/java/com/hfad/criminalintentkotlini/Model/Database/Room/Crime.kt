package com.hfad.criminalintentkotlini.Model.Database.Room

import androidx.room.*
import java.util.*

@Entity( tableName = "crime_table")
data class Crime (@PrimaryKey( autoGenerate = true ) val id : Int? = null,
                             @ColumnInfo(name = "title")var title : String = "No Title",
                             var solved : Boolean? = false,
                             var date : Date? = null)
//                             var lastUpdated : Date? = null )
{
   override infix fun equals(other: Any?): Boolean {
       val otherCrime =  other as? Crime
           ?: return false
       return this.title == otherCrime.title &&
               this.solved == otherCrime.solved &&
               this.date == otherCrime.date
    }

    override fun hashCode(): Int {
        var result = Random().nextInt()
        result = 31 * result + title.hashCode()
        result = 31 * result + solved.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }


}