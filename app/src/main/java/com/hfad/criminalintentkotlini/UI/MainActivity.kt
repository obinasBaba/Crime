package com.hfad.criminalintentkotlini.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeDetailFragment
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeListFragment

const val TAG : String = "MAIN"
class MainActivity : AppCompatActivity() , OnClickCallBack
{
    companion object{
        const val SELECTED_CRIME_ID_KEY = "incoming crime id"
    }

    private lateinit var mFragmentManager : FragmentManager
    private val crimeListFragInstance : CrimeListFragment by lazy { CrimeListFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFragmentManager = supportFragmentManager

        val fragmentTransaction = mFragmentManager.beginTransaction()
        fragmentTransaction.replace( R.id.fragment_container_view, crimeListFragInstance )
        fragmentTransaction.addToBackStack( crimeListFragInstance.tag )
        fragmentTransaction.commit()

    }

    override fun itemSelected(crimeId: Int) {
        Toast.makeText( this, "onClick", Toast.LENGTH_SHORT ).show()

        val crimeDetailFragInstance = CrimeDetailFragment.newInstance( crimeId )
        val fragmentTransaction = mFragmentManager.beginTransaction()

        fragmentTransaction.hide( crimeListFragInstance )
        fragmentTransaction.add(R.id.fragment_container_view, crimeDetailFragInstance )
        fragmentTransaction.addToBackStack( crimeDetailFragInstance.tag )
        fragmentTransaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}


interface OnClickCallBack {
    fun itemSelected(crimeId: Int)
}
