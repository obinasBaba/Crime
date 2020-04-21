package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.DataManager

//Will survive config changes
class CrimeViewModel(application: Application) : AndroidViewModel(application)
{
    private val dataManager: DataManager by lazy { DataManager.getInstance(getApplication()) }
    private val mutableCrime: MutableLiveData<Crime> by lazy { MutableLiveData<Crime>() }

    var surviveConfigChange: Crime? = null
    var changesToUpdate: MutableList<String> = ArrayList()
    var crimeModified = false // Determine whether to update give crime or not
    var cachedCrime: Crime? = null // A crime use to compare for change
    var cachedIndex: Int = -1

    fun getMutableCrime(): LiveData<Crime> {
        return mutableCrime
    }

    fun queryCrimeById( newIndex: Int ): Crime? {

        return when {
            newIndex != cachedIndex -> {
                // Query for the first time, Cache and Return
                cachedIndex = newIndex
                cachedCrime = dataManager.queryCrimeById(newIndex.toString())
                mutableCrime.postValue( cachedCrime )
                cachedCrime
            }
            newIndex == cachedIndex -> {
                null  // Prevent reQuery
            }
            else -> cachedCrime
        }
    }

    // TODO -  RUN ON BACKGROUND
    fun updateCrime(crimeById: Crime) {
        dataManager.updateCrimeDb(crimeById, changesToUpdate.distinct().toTypedArray() )
    }

    fun createCrime( selectedCrime: Crime ) : Int  {
       return dataManager.addNewCrime( selectedCrime )
    }

}
