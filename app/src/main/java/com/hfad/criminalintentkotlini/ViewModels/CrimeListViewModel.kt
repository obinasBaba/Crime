package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.database.Cursor
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import kotlin.properties.Delegates

class CrimeListViewModel( applicationCtx: Application) : AndroidViewModel( applicationCtx ) {

    private var mutableCursor : MutableLiveData< Cursor >
    private lateinit var cursor : Cursor

    private val dataMgr : DataManager = DataManager.getInstance( getApplication() )
    var sparseBoolean : SparseBooleanArray by Delegates.observable( SparseBooleanArray() ){ _, _, _ ->
        Toast.makeText( getApplication(), "Value Changed", Toast.LENGTH_SHORT).show() }

    init {
        readDataFromDatabase()
        mutableCursor = MutableLiveData( cursor )
        Toast.makeText( getApplication(), "MODEL VIEW STARTED", Toast.LENGTH_LONG).show()
    }
    
    fun getCrimesCursor() : LiveData< Cursor > = mutableCursor


    private fun readDataFromDatabase() {
        dataMgr.readBulk { cursorFromDB -> cursor = cursorFromDB }
    }

    fun removeCrime(keyAt: Int) {

    }

    fun addCrime( crime : Crime ){

    }

    fun nullifyDB() {
        dataMgr.closeDB()
    }

    override fun onCleared() {
        cursor.close()
        super.onCleared()
    }
}
