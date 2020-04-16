package com.hfad.criminalintentkotlini.ViewModels

import androidx.lifecycle.ViewModel
import com.hfad.criminalintentkotlini.Model.Crime

//Will survive config changes
class CrimeViewModel( ) : ViewModel() {

    private val crime : Crime
        get() = Crime()

    fun setTitle( title : String ){
        crime.title = title
    }

    fun getTitle() : String = crime.title

    fun getDate() = crime.date.toString()
}
