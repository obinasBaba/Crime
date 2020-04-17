package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.DataManager

//Will survive config changes
class CrimeViewModel(application: Application) : AndroidViewModel(application) {

    private val dataManager: DataManager by lazy { DataManager.getInstance(getApplication()) }
    var surviveConfigChange: Crime? = null
    var changesToUpdate: MutableList<String> = ArrayList()
    var crimeModified = false // Determine whether to update give crime or not
    var cachedCrime: Crime? = null // A crime use to compare for change
    var cachedIndex: Int = -1

    private val mutableCrime: MutableLiveData<Crime> by lazy { MutableLiveData(Crime()) }

    fun getMutableCrime(index: Int): LiveData<Crime> {
        mutableCrime.value = queryCrimeById(index)
        return mutableCrime
    }

    private fun queryCrimeById( newIndex: Int ): Crime? {

        return when {
            newIndex == cachedIndex -> {
                null  // Prevent reQuery
            }
            newIndex != cachedIndex -> {
                // Query for the first time Cache and Return
                cachedIndex = newIndex
                cachedCrime = dataManager.queryCrimeById(newIndex.toString())
                cachedCrime
            }
            else -> cachedCrime
        }
    }

    fun updateCrime(crimeById: Crime) {
        Toast.makeText(getApplication(), "$crimeById \n${changesToUpdate.size} ", Toast.LENGTH_LONG)
            .show()
        dataManager.updateCrimeDb(crimeById, changesToUpdate.distinct().toTypedArray())
    }

    fun createCrime(  ) {
        Toast.makeText(getApplication(), "Creating a new Crime", Toast.LENGTH_LONG)
            .show()
    }

}
