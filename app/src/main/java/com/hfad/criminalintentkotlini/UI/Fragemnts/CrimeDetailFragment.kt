package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.*
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.MainActivity.Companion.SELECTED_CRIME_ID_KEY
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_fragment.*
import kotlin.properties.Delegates



class CrimeDetailFragment : Fragment()
{
    companion object {
        private const val QUERY_CRIME = 3
        const val CHANGED = "changed"
        const val ACTION = "action"
        const val UPDATE_CRIME = 1
        const val CREATE_CRIME = 2
        const val INDEX = "id"

        fun newInstance( crimeId : Int  ) : CrimeDetailFragment{
            val crimeDetailFragmentInstance = CrimeDetailFragment()
            val bundle = Bundle().apply { putInt( SELECTED_CRIME_ID_KEY, crimeId ) }

            return crimeDetailFragmentInstance.apply { arguments = bundle }
        }
    }

    private val viewModel: CrimeListViewModel by lazy{ ViewModelProvider( this )
        .get( CrimeListViewModel::class.java) }

    //Listen for change within Current selected crime with observer and update value
    private var selectedCrime : Crime  by Delegates.observable(Crime()) { _, _, newCrime ->
        if ( viewModel.cachedCrime != null ){
            viewModel.crimeModified = !( newCrime equals viewModel.cachedCrime )

            if ( !viewModel.crimeModified )
                viewModel.changesToUpdate.removeAll { it.length >= 0 }
            Toast.makeText( context, "dataChanged = ${viewModel.crimeModified}, ${viewModel.changesToUpdate.size}", Toast.LENGTH_SHORT ).show()
        }
    }
    private var bundledCrimeId = -1

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.crime_fragment, container, false )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) { super.onActivityCreated(savedInstanceState)

        bundledCrimeId = arguments?.getInt( SELECTED_CRIME_ID_KEY ) ?: -1

        if ( bundledCrimeId != -1 )
            viewModel.queryCrimeById( bundledCrimeId )


        viewModel.getDetailCrimeLiveData().observe( viewLifecycleOwner, Observer {
            when {
                viewModel.surviveConfigChange != null -> {
                    selectedCrime = viewModel.surviveConfigChange!!
                    viewModel.surviveConfigChange = null
                    bindViews()
                }
                it != null -> {
                    selectedCrime = Crime(it.id, it.title, it.solved, it.date)
                    bindViews()
                }
            }
        })
    }

    private fun bindViews() {
        crime_title.setText(selectedCrime.title)
        crime_date.text = selectedCrime.date.toString()
        crime_solved.isChecked = selectedCrime.solved ?: false
    }

    override fun onStart() {
        super.onStart()

        crime_title.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
               selectedCrime = selectedCrime.apply { selectedCrime.title = s.toString() }

                if ( viewModel.crimeModified )
                    fab_id.setImageResource( R.drawable.ic_file_upload_black_24dp)
                else fab_id.setImageResource( R.drawable.ic_file_upload_black_24dp )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        } )

        crime_solved.setOnCheckedChangeListener(){ _, checked ->
            selectedCrime = selectedCrime.apply { solved = checked }

            if ( selectedCrime.solved !=  viewModel.cachedCrime?.solved )
                viewModel.changesToUpdate.add( CrimeEntry.COLUMN_CRIME_SOLVED )
        }

        fab_id.setOnClickListener{

            val intent = Intent()
            if ( bundledCrimeId == -1 && !TextUtils.isEmpty( crime_title.text ) && viewModel.cachedCrime == null )
            {
                // Create
                bundledCrimeId = viewModel.createCrime( selectedCrime )
                intent.putExtra( INDEX, bundledCrimeId )
                activity?.setResult( Activity.RESULT_OK, intent )
                activity?.onBackPressed()

            }else if( bundledCrimeId != -1 && viewModel.crimeModified )
            {
                //Update
                viewModel.updateCrime( selectedCrime )
                intent.putExtra( INDEX, bundledCrimeId )
                activity?.setResult( Activity.RESULT_OK, intent )
                activity?.onBackPressed()
            }

        }
    }

    override fun onDestroy() {

        Toast.makeText( context, "onDestroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
        val finishing =  activity?.isFinishing  ?: false

        if ( finishing && viewModel.crimeModified )
        {
            // TODO - ALERT DIALOG
            Toast.makeText( context, "UNSAVE CHANGE", Toast.LENGTH_SHORT).show()
        }

        if( !finishing && viewModel.crimeModified ) {
        //    viewModel.surviveConfigChange = selectedCrime
        }
    }
}
