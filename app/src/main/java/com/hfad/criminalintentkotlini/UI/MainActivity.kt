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

const val TAG : String = "MAIN"
class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController( R.id.main_host_frag ).navigateUp() || super.onSupportNavigateUp()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }
}