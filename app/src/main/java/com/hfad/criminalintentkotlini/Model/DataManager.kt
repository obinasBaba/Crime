package com.hfad.criminalintentkotlini.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.database.sqlite.SQLiteDatabase
import com.hfad.criminalintentkotlini.Model.Database.CrimeOpenHelper
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.CrimeEntry
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.Fragemnts.RecyclerAdapter
import java.util.*
import kotlin.collections.ArrayList

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
    private val dataSetObserver : DataSetObserver by lazy { DataSetObserverInner() }

    private var rowId : Int = -1
    private var titleIndex : Int = -1
    private var solvedIndex : Int = -1
    private var dateIndex : Int = -1
    private var cursorIsValid : Boolean = false

    private val arrayOf: Array<String> by lazy { arrayOf(
        CrimeEntry.COLUMN_CRIME_ID,
        CrimeEntry.COLUMN_CRIME_TITLE,
        CrimeEntry.COLUMN_CRIME_DATE,
        CrimeEntry.COLUMN_CRIME_SOLVED ) }

    fun readBulk( onDataReadyCallback : ( ArrayList<Crime> ) -> Unit ): ArrayList<Crime > {

        val crimeList : ArrayList< Crime > = ArrayList()
        val query: Cursor = readOperation.run {
            query(CrimeEntry.TABLE_NAME, arrayOf,
                null, null, null, null," ${CrimeEntry.COLUMN_CRIME_TITLE}" )
        }

        indexing( query )
        while ( query.moveToNext() ) {
            val crime = buildCrime(query)
            crimeList.add( crime )
        }

        query.close()
        onDataReadyCallback( crimeList )
        return crimeList
    }

    fun queryCrimeById( id : String ) : Crime {

        val cursor : Cursor = readOperation.query(CrimeEntry.TABLE_NAME, arrayOf,
                "${CrimeEntry.COLUMN_CRIME_ID} = ? ", arrayOf( id ), null, null,null )

        cursor.moveToFirst()

        indexing( cursor )
        val crimeId = cursor.getInt( rowId )
        val title = cursor.getString( titleIndex )
        val date = Date( cursor.getString( dateIndex ) )
        val solved = cursor.getInt( solvedIndex ) == 0

        cursor.close()
        return Crime( crimeId, title, solved, date )
    }


    fun closeDB(){

    }

    fun updateCrimeDb(crimeById: Crime, columnToUpdate: Array<String>) {

        val contentValue = ContentValues()

        contentValue.put( CrimeEntry.COLUMN_CRIME_TITLE, crimeById.title )
        contentValue.put( CrimeEntry.COLUMN_CRIME_SOLVED, crimeById.solved)
        writeOperation.update( CrimeEntry.TABLE_NAME, contentValue, CrimeEntry.COLUMN_CRIME_ID + " = ? ", arrayOf( crimeById.id.toString() )  )
    }

    private  fun  buildCrime( cursor : Cursor ) : Crime {
        val id = cursor.getInt( rowId )
        val title = cursor.getString( titleIndex )
        val date = Date( cursor.getString( dateIndex ) )
        val solved = cursor.getInt( solvedIndex ) == 0
        return Crime( id, title, solved, date )
    }

    private fun indexing( cursor: Cursor ) {
        rowId = cursor.getColumnIndex( CrimeEntry.COLUMN_CRIME_ID )
        titleIndex = cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_TITLE)
        solvedIndex = cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_SOLVED)
        dateIndex = cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_DATE)
    }

    inner class DataSetObserverInner : DataSetObserver(){
        override fun onChanged() {
            cursorIsValid = true

        }
        override fun onInvalidated() {
            super.onInvalidated()
            cursorIsValid = false
        }
    }
}