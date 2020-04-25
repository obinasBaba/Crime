package com.hfad.criminalintentkotlini.Model.Database.Room

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hfad.criminalintentkotlini.Model.Database.Room.IO_Executor.foo
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

@Database( entities = [Crime::class], version = 1, exportSchema = true )
@TypeConverters( DateConverter::class )
abstract class RoomCrimeOpenHelper() : RoomDatabase()
{

    abstract fun getCrimeDao( ) : CrimeDao

    companion object{

//        val migration_1_2 = object : Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//            }
//        }
//
//        val migration_2_3 = object : Migration(2,3){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL( "Alter table crime_table add column lastUpdated INTEGER " )
//            }
//        }

        private var roomCrimeOpenHelper : RoomCrimeOpenHelper? = null
        fun getInstance( ctx : Context) : RoomCrimeOpenHelper =
            roomCrimeOpenHelper ?: synchronized( this ){
                Room.databaseBuilder( ctx.applicationContext, RoomCrimeOpenHelper::class.java, "crimeDb" )
                    .addCallback( object : Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val crimeDao = getInstance(ctx).getCrimeDao()
                            IO_Executor.execute {
                                crimeDao.insertBulk( foo())
                            }
                        }
                    })
                 //   .addMigrations( migration_1_2, migration_2_3  )
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