package com.hfad.criminalintentkotlini.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeListFragment

const val TAG : String = "MAIN"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar : Toolbar = findViewById(R.id.toolbar_id)
        setSupportActionBar( toolbar )
        supportActionBar?.title = resources.getString(R.string.app_name)

       val fMgr : FragmentManager = supportFragmentManager
        var fragment : Fragment? = fMgr.findFragmentById(R.id.fragment_container_view)

        if ( fragment == null )
        {
            Log.d(TAG, "in let" )
            fragment = CrimeListFragment.newInstance()
            val fragTransaction : FragmentTransaction = fMgr.beginTransaction()
            fragTransaction.add(R.id.fragment_container_view, fragment  )
            fragTransaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_FADE )
            fragTransaction.commit()
        }
    }

    override fun onBackPressed() {

    }
}
