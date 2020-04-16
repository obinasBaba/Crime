package com.hfad.criminalintentkotlini.Model

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.hfad.criminalintentkotlini.Model.Database.CrimeOpenHelper
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.CrimeEntry
import java.util.*
import kotlin.collections.ArrayList

 class DataManager ( ctx : Context )
{
    private  var crimeRead : SQLiteDatabase = CrimeOpenHelper( ctx ).readableDatabase


    fun readData( onDataReadyCallback: ( ArrayList<Crime> ) -> Unit  ){

        val crimeList : ArrayList<Crime> =  ArrayList()

        val arrayOf: Array<String> = arrayOf(
            CrimeEntry.COLUMN_CRIME_TITLE,
            CrimeEntry.COLUMN_CRIME_DATE,
            CrimeEntry.COLUMN_CRIME_SOLVED
        )
        val query: Cursor = crimeRead.query(CrimeEntry.TABLE_NAME, arrayOf,
            null, null, null, null, CrimeEntry.COLUMN_CRIME_TITLE)

        val titleIndex = query.getColumnIndex(CrimeEntry.COLUMN_CRIME_TITLE)
        val solvedIndex = query.getColumnIndex(CrimeEntry.COLUMN_CRIME_SOLVED)
        val dateIndex = query.getColumnIndex(CrimeEntry.COLUMN_CRIME_DATE)

        try {
            while ( query.moveToNext() ) {

                val title = query.getString( titleIndex )
                val solved : Boolean = query.getInt( solvedIndex ) == 0;
                val date = query.getString( dateIndex )

                val crime = Crime( 0, title, solved, Date( date ))
                crimeList.add( crime )
            }
        }catch ( e : SQLException ){
            e.printStackTrace()
        }

        query.close()

        onDataReadyCallback( crimeList )
    }

    fun closeDB(){
        crimeRead.close()
    }
}