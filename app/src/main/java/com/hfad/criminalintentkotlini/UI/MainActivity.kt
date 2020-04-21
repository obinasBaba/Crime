package com.hfad.criminalintentkotlini.UI

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeListFragment
import com.hfad.criminalintentkotlini.UI.Fragemnts.REQUEST_CODE

const val TAG : String = "MAIN"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val toolbar : Toolbar = findViewById(R.id.toolbar_id)
//        setSupportActionBar( toolbar )
//        supportActionBar?.title = resources.getString(R.string.app_name)


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

    override fun onActivityResult( requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE ) {
            Toast.makeText( this, "notifyDataSetChanged", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onResume() {
        super.onResume()


    }
}
