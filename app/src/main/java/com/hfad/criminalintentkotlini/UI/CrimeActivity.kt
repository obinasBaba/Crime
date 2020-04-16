package com.hfad.criminalintentkotlini.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeFragment

class CrimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)

        val fMgr : FragmentManager = supportFragmentManager
        var fragment : Fragment? = fMgr.findFragmentById(R.id.CrimeFragment_container_view)

        if ( fragment == null )
        {
            Log.d(TAG, "in let" )
            fragment = CrimeFragment.newInstance()
            val fragTransaction : FragmentTransaction = fMgr.beginTransaction()
            fragTransaction.add(R.id.CrimeFragment_container_view, fragment  )
            fragTransaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
            fragTransaction.commit()
        }
    }
}
