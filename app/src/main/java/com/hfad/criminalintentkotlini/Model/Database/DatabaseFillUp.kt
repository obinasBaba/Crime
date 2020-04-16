package com.hfad.criminalintentkotlini.Model.Database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

class DatabaseFillUp( val db : SQLiteDatabase? )
{
    fun fillUp(){

       putInto("Murder",0)
       putInto("tray Steal",1)
       putInto("Rape",1)
       putInto("Snatch and grape",0)
       putInto("poising rise",1)
    }

    private fun putInto( title : String, solved : Int ){
        require( solved == 1 || solved == 0){throw IllegalArgumentException()}

        val contentValue = ContentValues()
        contentValue.put(   CrimeEntry.COLUMN_CRIME_TITLE,title )
        contentValue.put(   CrimeEntry.COLUMN_CRIME_DATE, Date().toString() )
        contentValue.put(   CrimeEntry.COLUMN_CRIME_SOLVED, solved )
        db?.insertOrThrow(CrimeEntry.TABLE_NAME,null, contentValue) ?: throw IllegalStateException()
    }

}