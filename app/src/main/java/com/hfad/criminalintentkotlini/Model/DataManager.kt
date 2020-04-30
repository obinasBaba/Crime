package com.hfad.criminalintentkotlini.Model

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.Database.Room.RoomCrimeOpenHelper

@SuppressLint("StaticFieldLeak")
class DataManager private constructor ( val ctx : Application )
{
    // TODO - Synchronizing
    companion object{
        private var instance : DataManager? = null
        fun getInstance( ctx : Application ) : DataManager =
            instance ?: DataManager( ctx ).also { instance = it }
    }

    private val roomDatabase : RoomCrimeOpenHelper  = RoomCrimeOpenHelper.getInstance( ctx )
    private val crimeDao  = roomDatabase.getCrimeDao()

    fun readBulk( ): LiveData<List<Crime>> {
        return object : AsyncTask< Void, Void, LiveData<List<Crime>> >(){
            override fun doInBackground(vararg params: Void?) : LiveData<List<Crime>> {
                return crimeDao.getBulk
            }
        }.execute().get()
    }

    fun queryCrimeById( id : Int ) : Crime {
        return object : AsyncTask< Int , Void,  Crime >(){
            override fun doInBackground(vararg params: Int?) : Crime {
                return crimeDao.getCrime( params.first()!! )
            }
        }.execute( id ).get()
    }

    fun updateCrimeDb( crimeById: Crime ) {
         object : AsyncTask< Crime , Void,  Int >(){
            override fun doInBackground(vararg params: Crime?) : Int {
                return crimeDao.update( params.first()!! )
            }
        }.execute( crimeById ).get()
    }

    fun addNewCrime( newCrime : Crime ) {
        object : AsyncTask< Crime , Void,  Void >(){
            override fun doInBackground(vararg params: Crime?) : Void? {
                 crimeDao.insertBulkOrSingle( params.first()!! )
                return null
            }
        }.execute( newCrime ).get()
    }

    fun deleteCrimes( list : List < Crime > ) {
        object : AsyncTask< List< Crime >, Void, Int >(){
            override fun doInBackground(vararg params: List<Crime> ): Int? {
                val arrayOfCrimes = params[0].toTypedArray()
                return crimeDao.deleteBulk(*arrayOfCrimes)
            }

            override fun onPostExecute( affectedRow: Int?) {
                Toast.makeText( ctx, "$affectedRow Crimes Deleted", Toast.LENGTH_SHORT  ).show()
            }
        }.execute( list ).get()
    }

    fun closeDB(){
        roomDatabase.openHelper.close()
    }
}
