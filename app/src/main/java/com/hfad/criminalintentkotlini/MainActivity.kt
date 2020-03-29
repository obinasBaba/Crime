package com.hfad.criminalintentkotlini

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

const val TAG : String = "MAIN"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val fMgr : FragmentManager = supportFragmentManager
        var fragment : Fragment? = fMgr.findFragmentById( R.id.fragment_container_view )

        if ( fragment == null )
        {
            Log.d( TAG, "in let" )
            fragment = CrimeFragment.newInstance()
            val fragTransaction : FragmentTransaction = fMgr.beginTransaction()
            fragTransaction.add( R.id.fragment_container_view, fragment  )
            fragTransaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
            fragTransaction.commit()
        }
    }
}
