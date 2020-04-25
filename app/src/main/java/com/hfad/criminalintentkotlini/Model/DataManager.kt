package com.hfad.criminalintentkotlini.Model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.RoomDatabase
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract
import com.hfad.criminalintentkotlini.Model.Database.CrimeOpenHelper
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.CrimeEntry
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.Database.Room.CrimeDao
import com.hfad.criminalintentkotlini.Model.Database.Room.RoomCrimeOpenHelper
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class DataManager private constructor ( ctx : Context )
{
    // TODO - Synchronizing
    companion object{
        private var instance : DataManager? = null
        fun getInstance( ctx : Context ) : DataManager =
            instance ?: DataManager( ctx ).also { instance = it }
    }

    private  val readOperation : SQLiteDatabase by lazy { CrimeOpenHelper( ctx ).readableDatabase }
    private  val writeOperation : SQLiteDatabase by lazy { CrimeOpenHelper( ctx ).writableDatabase }
    private val dataSetObserver : DataSetObserver by lazy { DataSetObserverInner() }

    private val roomDatabase : RoomCrimeOpenHelper  = RoomCrimeOpenHelper.getInstance( ctx )
    private val crimeDao  = roomDatabase.getCrimeDao()

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

    fun readBulk( ): LiveData<List<Crime>> {

        return object : AsyncTask< Void, Void, LiveData<List<Crime>> >(){

            override fun doInBackground(vararg params: Void?) : LiveData<List<Crime>> {
                try {
                    val version = roomDatabase.openHelper.readableDatabase.version
                    val name = roomDatabase.openHelper.databaseName
                }catch ( e : Exception ){
                    e.printStackTrace()
                }
              //  val crimeLastUpdated: Date = crimeDao.getCrimeLastUpdated(0)
                return crimeDao.getBulk
            }
        }.execute().get()
    }

    fun queryCrimeById( id : String ) : Crime {
        return object : AsyncTask< Int , Void,  Crime >(){
            override fun doInBackground(vararg params: Int?) :  Crime {
                return crimeDao.getCrime( params.first()!! )
            }
        }.execute().get()
    }


    fun closeDB(){
        roomDatabase.openHelper.close()
    }

    fun updateCrimeDb(crimeById: Crime, columnToUpdate: Array<String>) {

        val contentValue = ContentValues()

        contentValue.put( CrimeEntry.COLUMN_CRIME_TITLE, crimeById.title )
        contentValue.put( CrimeEntry.COLUMN_CRIME_SOLVED, crimeById.solved)
        writeOperation.update( CrimeEntry.TABLE_NAME, contentValue, CrimeEntry.COLUMN_CRIME_ID + " = ? ", arrayOf( crimeById.id.toString() )  )
    }

    fun addNewCrime(newCrime : Crime) : Int
    {
        val contentValue = ContentValues()

        contentValue.put( CrimeEntry.COLUMN_CRIME_TITLE, newCrime.title )
        contentValue.put( CrimeEntry.COLUMN_CRIME_SOLVED, newCrime.solved)
        contentValue.put( CrimeEntry.COLUMN_CRIME_DATE, newCrime.date.toString() )

        val inserted = writeOperation.insert(CrimeEntry.TABLE_NAME, null, contentValue)
        return inserted.toInt()
    }

    fun deleteCrimes( idArray : Array< String > ) : Int
    {

        repeat( idArray.size ){
            val id = idArray[ it ]
            val deletedId = writeOperation.delete(
                            CrimeEntry.TABLE_NAME,
                 "${CrimeEntry.COLUMN_CRIME_ID} = ? ", arrayOf( id ) )
        }

       return 0
    }

    private  fun  buildCrime( cursor : Cursor ) : Crime {
        val id = cursor.getInt( rowId )
        val title = cursor.getString( titleIndex )
        val date = Date( cursor.getString( dateIndex ) )
        val solved = cursor.getInt( solvedIndex ) == 0
        return Crime(
            id,
            title,
            solved,
            date
        )
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