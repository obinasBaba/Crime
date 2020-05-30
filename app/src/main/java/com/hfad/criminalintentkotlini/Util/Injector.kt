package com.hfad.criminalintentkotlini.Util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.criminalintentkotlini.Model.DataManager
import com.hfad.criminalintentkotlini.UI.CrimeListViewModel

object Injector {

    fun buildFactory( application: Application ) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CrimeListViewModel::class.java)) {
                val l = DataManager.getInstance( application )
                @Suppress("UNCHECKED_cAST")
                return CrimeListViewModel(
                    application,
                    l
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class *** THIS IS MINEDSKJSDF ***")
        }
    }
}