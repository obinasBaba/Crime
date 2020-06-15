package com.hfad.criminalintentkotlini.Model.Database.Room

import android.content.Context
import android.provider.BaseColumns
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

@Database( entities = [Crime::class], version = 7, exportSchema = true )
@TypeConverters( DateConverter::class )
abstract class CrimeDB() : RoomDatabase()
{

    abstract fun getCrimeDao( ) : CrimeDao

    companion object{

        private val migration_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( "Alter table crime_table add column lastUpdated INTEGER " )
            }
        }
        private val migration_2_3 = object : Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( "Alter table crime_table add column description TEXT " )
            }
        }
        private val migration_3_4 = object : Migration( 3, 4 ) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( "ALTER TABLE crime_table ADD COLUMN suspect TEXT" )
            }
        }
        private val migration_4_5 = object : Migration( 4, 5 ) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE crime_table RENAME COLUMN id TO ${BaseColumns._ID} ")
               // database.execSQL("CREATE TABLE TEMP( ${BaseColumns._ID} INTEGER AUTO INCREMENT PRIMARY KEY,   )")
            }
        }
        private val migration_6_7 = object : Migration(6,7){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE crime_table ADD COLUMN photoName TEXT ")
            }
        }

        private var crimeDB : CrimeDB? = null
        fun getInstance( ctx : Context) : CrimeDB =
            crimeDB ?: synchronized( this ){
                Room.databaseBuilder( ctx.applicationContext, CrimeDB::class.java, "crimeDb" )
                    .addMigrations( migration_6_7 )
                    .build().also {
                        crimeDB = it
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
        for( i in 0..20){
            list.add( Crime().apply { title = "crimee"; date = Date(); lastUpdated = Date() } )
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