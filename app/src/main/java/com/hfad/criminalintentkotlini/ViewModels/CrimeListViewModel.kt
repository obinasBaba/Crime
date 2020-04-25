package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import kotlin.properties.Delegates

class CrimeListViewModel( applicationCtx: Application) : AndroidViewModel( applicationCtx ) {


    var sparseBoolean : SparseBooleanArray by Delegates.observable( SparseBooleanArray() ){ _, _, _ ->
        Toast.makeText( getApplication(), "Value Changed", Toast.LENGTH_SHORT).show() }

    private val dataMgr : DataManager = DataManager.getInstance( getApplication() )

     var crimeList : LiveData< List<Crime> >  = MutableLiveData( ArrayList() )

    var firstTime : Boolean = true

    init {
        Toast.makeText( getApplication(), "MODEL VIEW STARTED", Toast.LENGTH_LONG).show()
        readDataFromDatabase()
    }

    private fun readDataFromDatabase()  {
        this.crimeList = dataMgr.readBulk()
    }


    fun removeCrime( ids: Array< String > ) : Int {
       return dataMgr.deleteCrimes( ids )
    }



    fun nullifyDB() {
        dataMgr.closeDB()
    }

    override fun onCleared() {
        Toast.makeText( getApplication(), "MODEL VIEW DESTROYED", Toast.LENGTH_LONG).show()
        nullifyDB()
        super.onCleared()
    }
}
