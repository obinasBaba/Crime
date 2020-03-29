package com.hfad.criminalintentkotlini.Model

import androidx.lifecycle.ViewModel

//Will survive config changes
class CrimeViewModel : ViewModel() {

    private val crime : Crime
        get() = Crime()

    fun setTitle( title : String ){
        crime.title = title
    }

    fun getTitle() : String = crime.title

    fun getDate() = crime.date.toString()


}
