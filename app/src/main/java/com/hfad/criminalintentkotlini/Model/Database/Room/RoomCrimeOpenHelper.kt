package com.hfad.criminalintentkotlini.Model.Database.Room

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hfad.criminalintentkotlini.Model.Database.Room.IO_Executor.foo
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

@Database( entities = [Crime::class], version = 4, exportSchema = true )
@TypeConverters( DateConverter::class )
abstract class RoomCrimeOpenHelper() : RoomDatabase()
{

    abstract fun getCrimeDao( ) : CrimeDao

    companion object{

        val migration_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( "Alter table crime_table add column lastUpdated INTEGER " )
            }
        }
        val migration_2_3 = object : Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( "Alter table crime_table add column description TEXT " )
            }
        }

        val migration_3_4 = object : Migration( 3, 4 )
        {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( "ALTER TABLE crime_table ADD COLUMN suspect TEXT" )
            }
        }

        private var roomCrimeOpenHelper : RoomCrimeOpenHelper? = null
        fun getInstance( ctx : Context) : RoomCrimeOpenHelper =
            roomCrimeOpenHelper ?: synchronized( this ){
                Room.databaseBuilder( ctx.applicationContext, RoomCrimeOpenHelper::class.java, "crimeDb" )
                    .addMigrations( migration_1_2, migration_2_3, migration_3_4   )
                    .build().also {
                        roomCrimeOpenHelper = it

                    }
            }
    }


}

object IO_Executor{
    private val executor : Executor = Executors.newSingleThreadExecutor()

    fun execute( block : ( ) -> Unit ){
        executor.execute(block)
    }
    fun foo(): MutableList<Crime> {
        val list : MutableList< Crime > = ArrayList()
        for( i in 0..80){
            list.add( Crime( title = "crimeee $i", date =  Date() ) )
        }
        return list
    }
}

class DateConverter
{
    @TypeConverter
    fun fromTimeStamp( timeStamp : Long? ) : Date? =
        timeStamp?.let { Date(it) }

    @TypeConverter
    fun toTimeStamp( date : Date? ) : Long? =
        date?.time
}