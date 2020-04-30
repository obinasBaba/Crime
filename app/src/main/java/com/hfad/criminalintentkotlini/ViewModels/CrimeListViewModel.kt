package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import com.hfad.criminalintentkotlini.UI.Fragemnts.RecyclerAdapter

class CrimeListViewModel( applicationCtx: Application) : AndroidViewModel( applicationCtx ) {




    private val dataManager : DataManager = DataManager.getInstance( getApplication() )
    var surviveConfigChange: Crime? = null
    var changesToUpdate: MutableList<String> = ArrayList()
    var crimeModified = false // Determine whether to update give crime or not
    var cachedCrime: Crime? = null // A crime use to compare for change
    var cachedIndex: Int = -1

    var sparseBoolean : SparseBooleanArray = SparseBooleanArray()
    private val crimeDetail: MutableLiveData<Crime> by lazy { MutableLiveData<Crime>() }
    val crimeList : LiveData< List<Crime> > = fetchCrimeListFromDB()

    init {
        Toast.makeText( getApplication(), "MODEL VIEW STARTED", Toast.LENGTH_LONG).show()
    }

    private fun fetchCrimeListFromDB() = dataManager.readBulk()

    /**
     * =============================================================================================================
     */

    fun getDetailCrimeLiveData(): LiveData<Crime> = crimeDetail

    fun getCrimesLiveData(): LiveData< List<Crime> > = crimeList

    fun queryCrimeById( newIndex: Int ): Crime? {

        return when {
            newIndex != cachedIndex -> {
                // Query for the first time, Cache and Return
                cachedIndex = newIndex
                cachedCrime = dataManager.queryCrimeById( newIndex )
                crimeDetail.value =  cachedCrime
                cachedCrime
            }
            newIndex == cachedIndex -> {
                null  // Prevent reQuery
            }
            else -> cachedCrime
        }
    }

    fun deleteCrimes(adapter : RecyclerAdapter ){
        val willBeDeleted : MutableList< Crime > = ArrayList( )
        val size = sparseBoolean.size()
        IntRange( 0, size ).forEach { index ->
            if ( sparseBoolean.valueAt( index ) ) {
                val recyclerPosition = sparseBoolean.keyAt( index )
                val crimeAtThatPosition = adapter.getCrimeAtIndex( recyclerPosition )
                willBeDeleted.add( crimeAtThatPosition )
            }
        }
        dataManager.deleteCrimes( willBeDeleted )
    }

    // TODO -  RUN ON BACKGROUND
    fun updateCrime(crimeById: Crime) {
        dataManager.updateCrimeDb(crimeById )
    }

    fun createCrime( selectedCrime: Crime) : Int  {
        dataManager.addNewCrime( selectedCrime )
        return 0
    }

    override fun onCleared() {
        Toast.makeText( getApplication(), "MODEL VIEW DESTROYED", Toast.LENGTH_LONG).show()
        super.onCleared()
    }
}
