package com.hfad.criminalintentkotlini.UI

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeDetailFragment
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeListFragment
import com.hfad.criminalintentkotlini.UI.Fragemnts.s

const val TAG : String = "MAIN"
class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            Toast.makeText( this, "${it.get(s)}", Toast.LENGTH_SHORT ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
      //  if ( !findNavController( R.id.main_host_frag ).popBackStack() )
            super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController( R.id.main_host_frag ).navigateUp() || super.onSupportNavigateUp()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString( s , "saved insss")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }
}