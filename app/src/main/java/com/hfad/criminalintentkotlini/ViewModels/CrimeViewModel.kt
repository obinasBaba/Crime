package com.hfad.criminalintentkotlini.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.DataManager

//Will survive config changes
class CrimeViewModel( application: Application ) : AndroidViewModel( application )
{
    private val dataManager : DataManager by lazy{
        DataManager.getInstance( getApplication() )
    }

    fun queryCrimeById(){

    }


}
