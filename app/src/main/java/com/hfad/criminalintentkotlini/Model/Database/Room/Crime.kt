package com.hfad.criminalintentkotlini.Model.Database.Room

import android.content.ContentValues
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity( tableName = "crime_table")
data class Crime (
                  @ColumnInfo( name = ID )
                  @PrimaryKey( autoGenerate = true )
                  var id : Int ,
                  @ColumnInfo(name = TITLE)
                  var title: String = "",
                  @ColumnInfo(name = IS_SOLVED)
                  var solved: Boolean? = false,
                  var suspect: String? = "Choose suspect",
                  var date: Date? = null,
                  var lastUpdated: Date? = null,
                  var description: String? = null,
                  var photoName : String? = null
                  ) {

    constructor() : this( id = 0 )

    companion object{
        const val TABLE_NAME = "crime_table"
        const val ID = BaseColumns._ID
        const val TITLE = "crime_title"
        const val IS_SOLVED = "is_solved"

        fun fromContentValues(values: ContentValues? ): Crime {

            val crime = Crime()
            if ( values != null && values.containsKey( ID ))
                crime.id = values.get( ID ) as Int

            if ( values != null && values.containsKey( TITLE ))
                crime.title = values.get( TITLE ) as String

            return crime
        }
    }

    fun uniquePhotoName() = "IMG_$id.jpg"

    @RequiresApi(Build.VERSION_CODES.KITKAT)
   override infix fun equals(other: Any?): Boolean {
       val otherCrime =  other as? Crime ?: return false

       return this.title == otherCrime.title &&
               this.lastUpdated?.time == other.lastUpdated?.time &&
               this.solved == otherCrime.solved &&
               this.photoName == other.photoName &&
               this.id == otherCrime.id &&
               this.suspect == other.suspect &&
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