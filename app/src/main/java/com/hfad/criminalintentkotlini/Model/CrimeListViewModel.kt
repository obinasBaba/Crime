package com.hfad.criminalintentkotlini.Model

import android.util.SparseBooleanArray
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeListViewModel : ViewModel() {
    private val crimes : MutableList< Crime > = mutableListOf()
    var sparseBoolean : SparseBooleanArray = SparseBooleanArray()

    init {
        for ( i in 0 until 100 ){
            val crime = Crime( i, "Crime #$i", i % 2 == 0, Date() )
            crimes.add( crime )
        }
    }

    fun getCrimes(): MutableList<Crime> = crimes
    fun remove(keyAt: Int) {
        crimes.removeAt( keyAt )
    }
}
