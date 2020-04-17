package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.*
import com.hfad.criminalintentkotlini.ViewModels.CrimeViewModel
import com.hfad.criminalintentkotlini.R
import kotlinx.android.synthetic.main.crime_fragment.*
import kotlin.properties.Delegates

const val CHANGED = "changed"

class CrimeFragment : Fragment() {

    companion object {
        fun newInstance() = CrimeFragment()
    }

    private val viewModel: CrimeViewModel by lazy{ ViewModelProvider( this ).get( CrimeViewModel::class.java) }
    private var selectedCrime by Delegates.observable( Crime() ) { _, _, newCrime ->
        if ( viewModel.cachedCrime != null ){

            viewModel.crimeModified = !( newCrime equals viewModel.cachedCrime )

            if ( !viewModel.crimeModified )
                viewModel.changesToUpdate.removeAll { it.length >= 0 }
            Toast.makeText( context, "dataChanged = ${viewModel.crimeModified}, ${viewModel.changesToUpdate.size}", Toast.LENGTH_SHORT ).show()
        }
    }
    private var index = -1

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.crime_fragment, container, false )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) { super.onActivityCreated(savedInstanceState)

        index = activity?.intent?.getIntExtra( INDEX, -1 ) ?: -1

        viewModel.getMutableCrime( index ).observe( viewLifecycleOwner, Observer {
            when {
                viewModel.surviveConfigChange != null -> {
                    selectedCrime = viewModel.surviveConfigChange!!
                    bindViews()
                }
                it != null -> {
                    selectedCrime = Crime( it.id, it.title, it.solved, it.date )
                    bindViews()
                }
            }
        })
    }

    private fun bindViews() {
        crime_title.setText(selectedCrime.title)
        crime_date.text = selectedCrime.date.toString()
        crime_solved.isChecked = selectedCrime.solved
    }

    override fun onStart() {
        super.onStart()
        crime_title.addTextChangedListener( object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
               selectedCrime = selectedCrime.apply { selectedCrime.title = s.toString() }

                if ( selectedCrime.title != viewModel.cachedCrime?.title )
                    viewModel.changesToUpdate.add( CrimeEntry.COLUMN_CRIME_TITLE )
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

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val finishing =  activity?.isFinishing  ?: false

        if ( finishing && viewModel.crimeModified )
        {
            viewModel.surviveConfigChange = null
            viewModel.cachedCrime = null
            viewModel.updateCrime( selectedCrime )
            Toast.makeText( context, "finishing = $finishing ", Toast.LENGTH_SHORT ).show()

        }else if( finishing && viewModel.cachedCrime == null ){

            viewModel.createCrime()

        }else if( !finishing && viewModel.crimeModified )
        {
            Toast.makeText( context, "configChange", Toast.LENGTH_SHORT ).show()
            viewModel.surviveConfigChange = selectedCrime
        }

    }

}
