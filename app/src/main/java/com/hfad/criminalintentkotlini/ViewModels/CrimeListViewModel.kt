package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import com.hfad.criminalintentkotlini.Util.RecyclerAdapter

class CrimeListViewModel( applicationCtx: Application) : AndroidViewModel( applicationCtx ) {





    private val dataManager : DataManager = DataManager.getInstance( getApplication() )
    var savedInstanceCrime: Crime? = null
    var crimeModified : MutableLiveData< Boolean > = MutableLiveData( false ) // Determine whether to update give crime or not
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

    fun queryCrimeById( newIndex: Int ): Crime {

        return if ( newIndex == cachedIndex )
            savedInstanceCrime ?: cachedCrime!!
        else {
            cachedIndex = newIndex
            cachedCrime ?: dataManager.queryCrimeById( newIndex ).also {
                cachedCrime = it
            }
        }
    }

    fun deleteCrimes(adapter: RecyclerAdapter){
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
    fun updateCrime(crimeById: Crime) =
        dataManager.updateCrimeDb(crimeById ).also {
            crimeModified.value = false
        }


    fun createCrime( selectedCrime: Crime) =
        dataManager.addNewCrime( selectedCrime ).also {
           crimeModified.value = false
       }


    override fun onCleared() {
        Toast.makeText( getApplication(), "MODEL VIEW DESTROYED", Toast.LENGTH_LONG).show()
        super.onCleared()
    }

    fun finishing() {
        savedInstanceCrime = null
        crimeModified = MutableLiveData( false )
        cachedCrime = null
        cachedIndex = -1
    }

    fun create(): Crime = cachedCrime ?: Crime( title = "" ).also { cachedCrime = it }

    fun update( id : Int ): Crime = cachedCrime ?: dataManager.queryCrimeById( id ).also { cachedCrime = it }
}
