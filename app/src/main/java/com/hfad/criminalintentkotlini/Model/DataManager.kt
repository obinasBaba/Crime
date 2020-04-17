package com.hfad.criminalintentkotlini.Model

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.ImageView
import android.widget.TextView
import com.hfad.criminalintentkotlini.Model.Database.CrimeOpenHelper
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.CrimeEntry
import com.hfad.criminalintentkotlini.R
import java.util.*

class DataManager private constructor ( ctx : Context )
{
    // TODO - Synchronizing
    companion object{
        private var instance : DataManager? = null

        fun getInstance( ctx : Context ) : DataManager =
            instance ?: DataManager( ctx ).also {
                instance = it
            }
    }

    private  val readOperation : SQLiteDatabase by lazy { CrimeOpenHelper( ctx ).readableDatabase }
    private  val writeOperation : SQLiteDatabase by lazy { CrimeOpenHelper( ctx ).writableDatabase }

    private val arrayOf: Array<String> by lazy { arrayOf(
        CrimeEntry.COLUMN_CRIME_ID,
        CrimeEntry.COLUMN_CRIME_TITLE,
        CrimeEntry.COLUMN_CRIME_DATE,
        CrimeEntry.COLUMN_CRIME_SOLVED ) }

    fun readBulk(onDataReadyCallback: (Cursor ) -> Unit  ){

        val query: Cursor = readOperation.use {
            it.query(CrimeEntry.TABLE_NAME, arrayOf,
                null, null, null, null," ${CrimeEntry.COLUMN_CRIME_TITLE}" )
        }
        onDataReadyCallback( query )
    }

    fun queryCrimeById( id : String, crimeFromDataMgr : ( crime : Crime ) -> Boolean ) {

        val cursor : Cursor = readOperation.use {
            it.query(CrimeEntry.TABLE_NAME, arrayOf,
                "${CrimeEntry.COLUMN_CRIME_ID} = ? ", arrayOf( id ), null, null,null )
        }

        val crimeId = cursor.getInt( cursor.getColumnIndex( CrimeEntry.COLUMN_CRIME_ID ) )
        val title = cursor.getString( cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_TITLE) )
        val date = Date( cursor.getString( cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_DATE) ) )
        val solved = cursor.getInt( cursor.getColumnIndex( CrimeEntry.COLUMN_CRIME_SOLVED ) ) == 0

        val crime = Crime( crimeId, title, solved , date )
        crimeFromDataMgr( crime )

        cursor.close()
    }

    fun closeDB(){
        if ( readOperation.isOpen )
               readOperation.close()
    }
}