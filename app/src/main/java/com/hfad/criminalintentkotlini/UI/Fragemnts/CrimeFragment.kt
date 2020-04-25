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
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.*
import com.hfad.criminalintentkotlini.ViewModels.CrimeViewModel
import com.hfad.criminalintentkotlini.R
import kotlinx.android.synthetic.main.crime_fragment.*
import kotlin.properties.Delegates

const val CHANGED = "changed"
const val ACTION = "action"
 const val UPDATE_CRIME = 1
 const val CREATE_CRIME = 2
private const val QUERY_CRIME = 3

class CrimeFragment : Fragment(), LoaderManager.LoaderCallbacks< Unit >
{
    companion object {
        fun newInstance() = CrimeFragment()
    }

    private val viewModel: CrimeViewModel by lazy{ ViewModelProvider( this ).get( CrimeViewModel::class.java) }
    private var selectedCrime by Delegates.observable(Crime()) { _, _, newCrime ->
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

        index = activity?.intent?.getIntExtra( "INDEX", -1 )!!
        if ( index != -1 )
            LoaderManager.getInstance( this ).initLoader( QUERY_CRIME, null, this )


        viewModel.getMutableCrime().observe( viewLifecycleOwner, Observer {
            when {
                viewModel.surviveConfigChange != null -> {
                    selectedCrime = viewModel.surviveConfigChange!!
                    viewModel.surviveConfigChange = null
                    bindViews()
                }
                it != null -> {
                    selectedCrime =
                        Crime(it.id, it.title, it.solved, it.date
                        )
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
            if ( index == -1 && !TextUtils.isEmpty( crime_title.text ) && viewModel.cachedCrime == null )
            {
                // Create
                LoaderManager.getInstance( this ).initLoader( CREATE_CRIME, null, this )

            }else if( index != -1 && viewModel.crimeModified )
            {
                //Update
                LoaderManager.getInstance( this ).initLoader( UPDATE_CRIME, null, this )
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val finishing =  activity?.isFinishing  ?: false

        if ( finishing && viewModel.crimeModified )
        {
            // TODO - ALERT DIALOG
            Toast.makeText( context, "UNSAVE CHANGE", Toast.LENGTH_SHORT).show()
        }

        if( !finishing && viewModel.crimeModified ) {
            viewModel.surviveConfigChange = selectedCrime
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader< Unit > {

        return  object :  AsyncTaskLoader< Unit >( context!! )
        {
            override fun loadInBackground() {
                when (id) {
                    UPDATE_CRIME -> {
                        viewModel.updateCrime( selectedCrime )
                    }
                    CREATE_CRIME -> {
                       index = viewModel.createCrime( selectedCrime )
                    }
                    QUERY_CRIME -> {
                        viewModel.queryCrimeById( index )
                    }
                }
            }

            override fun onStartLoading() {
                forceLoad()
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Unit>, data: Unit) {
        Toast.makeText( context, "onLoadFinished", Toast.LENGTH_SHORT).show()

        val intent = Intent()
        intent.putExtra( "INDEX", index )

        if ( loader.id == CREATE_CRIME ) {

            intent.putExtra( ACTION, CREATE_CRIME )
            activity?.setResult( Activity.RESULT_OK, intent )
            activity?.finish()

        }else if ( loader.id == UPDATE_CRIME )
        {

            intent.putExtra( ACTION, UPDATE_CRIME )
            activity?.setResult( Activity.RESULT_OK, intent )
            activity?.finish()
        }
    }

    override fun onLoaderReset(loader: Loader<Unit>) {
        //viewModel.revertEverything()
        Toast.makeText( context, "onLoaderReset", Toast.LENGTH_SHORT).show()
    }
}
