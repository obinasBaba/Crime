package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import kotlin.properties.Delegates

class CrimeListViewModel( applicationCtx: Application) : AndroidViewModel( applicationCtx ) {


    private val dataMgr : DataManager = DataManager.getInstance( getApplication() )
    private var mutableCursor : MutableLiveData< List< Crime > > = MutableLiveData( ArrayList() )
    var firstTime : Boolean = true

    var sparseBoolean : SparseBooleanArray by Delegates.observable( SparseBooleanArray() ){ _, _, _ ->
        Toast.makeText( getApplication(), "Value Changed", Toast.LENGTH_SHORT).show() }

    init {
        Toast.makeText( getApplication(), "MODEL VIEW STARTED", Toast.LENGTH_LONG).show()
    }
    
    fun getCrimes() : LiveData< List< Crime > > = mutableCursor

     fun readDataFromDatabase() : ArrayList< Crime > =  dataMgr.readBulk{  mutableCursor.postValue( it )  }


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
