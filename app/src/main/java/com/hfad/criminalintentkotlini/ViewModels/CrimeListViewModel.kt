package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty

class CrimeListViewModel( applicationCtx: Application) : AndroidViewModel( applicationCtx ) {

    private val crimes : MutableLiveData< ArrayList<Crime> > = MutableLiveData( ArrayList())

    var sparseBoolean : SparseBooleanArray by Delegates.observable( SparseBooleanArray() ){ _, _, _ ->
        Toast.makeText( getApplication(), "Value Changed", Toast.LENGTH_SHORT).show() }


    val dataMgr : DataManager = DataManager( getApplication() )

    init {
       readDataFromDatabase()
        Toast.makeText( getApplication(), "MODEL VIEW STARTED", Toast.LENGTH_LONG).show()
    }
    
    fun getCrimes() : LiveData< ArrayList<Crime> >
    {
        return crimes
    }

    private fun readDataFromDatabase() {
        dataMgr.readData{ dataFromDB -> crimes.value = dataFromDB }
    }

    fun remove(keyAt: Int) {
        crimes.value?.removeAt( keyAt )
    }

    fun setAt( keyAt : Int ){

    }

    fun nullifyDB() {
        dataMgr.closeDB()
    }

    override fun onCleared() {
        Toast.makeText( getApplication(), "MODEL VIEW CLEARED", Toast.LENGTH_LONG).show()
        super.onCleared()
    }
}
