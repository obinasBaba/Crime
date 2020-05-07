package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_fragment.*
import kotlin.properties.Delegates


private const val NO_ARG = -1

class CrimeDetailFragment : Fragment()
{
    //Listen for change within Current selected crime with observer and update value
    private var selectedCrime : Crime by Delegates.observable( Crime() ) { _, _, newCrime ->
            viewModel.crimeModified.value = !( newCrime equals viewModel.cachedCrime )
            Toast.makeText( context, "dataChanged = ${viewModel.crimeModified.value} ", Toast.LENGTH_SHORT ).show()
    }
    // Initialize the viewModel by lazy
    private val viewModel: CrimeListViewModel by activityViewModels()
    val animBounce: Animation by lazy { AnimationUtils.loadAnimation( requireContext(), R.anim.bounce) }
    private var bundledCrimeId = NO_ARG

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.crime_fragment, container, false )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) : Unit {
        super.onViewCreated(view, savedInstanceState)

        bundledCrimeId = CrimeDetailFragmentArgs.fromBundle( requireArguments() ).selectedCrimeId

        // Track the state of fab icon with respect to crimeModification for update and create
        viewModel.crimeModified.observe( viewLifecycleOwner, Observer { crimeModified -> fabStateListener( crimeModified ) } )

        // if true it's after configChange so use the savedInstanceCrime object
        if ( savedInstanceState != null && viewModel.crimeModified.value!! ) {
            selectedCrime = viewModel.savedInstanceCrime!!
            return
        }

        if( bundledCrimeId == NO_ARG ) {
            // create
            selectedCrime = viewModel.create().copy()
        }
        else {
            // update
            selectedCrime = viewModel.update( bundledCrimeId ).copy()
            bindViews()
        }
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
               selectedCrime = selectedCrime.apply {
                   selectedCrime.title = s.toString()
               }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        } )

        crime_solved.setOnCheckedChangeListener(){ _, checked ->
            selectedCrime = selectedCrime.apply { solved = checked }
        }

        fab_id.setOnClickListener{
            fabEventListener()
        }

        requireActivity().onBackPressedDispatcher.addCallback( viewLifecycleOwner  ){
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Toast.makeText( context, "onDestroy", Toast.LENGTH_SHORT).show()

        if( viewModel.crimeModified.value!! && viewModel.cachedCrime != null ) {
            viewModel.savedInstanceCrime = selectedCrime
        }
    }

    private fun onBackPressed() {
        if ( viewModel.crimeModified.value!! ) {
            // TODO - ALERT DIALOG
            Toast.makeText( context, "UNSAVE CHANGE", Toast.LENGTH_SHORT).show()
        }

        viewModel.finishing()
        val sucess = findNavController().popBackStack()
        Toast.makeText( context, "$sucess" , Toast.LENGTH_SHORT).show()
    }

    private fun fabEventListener() {
        if ( bundledCrimeId == NO_ARG && viewModel.crimeModified.value!!  ) {
            // Create
            viewModel.createCrime( selectedCrime )
            requireActivity().onBackPressed()

        }else if( bundledCrimeId != NO_ARG && viewModel.crimeModified.value!!  ) {
            //Update
            bundledCrimeId = viewModel.updateCrime( selectedCrime )
            requireActivity().onBackPressed()
        }else
            Toast.makeText( context, "Unkown Operation", Toast.LENGTH_SHORT).show()
    }

    private fun fabStateListener( crimeModified : Boolean ){
        if ( bundledCrimeId == NO_ARG && crimeModified ) {
            // Create
            fab_id.apply {
                setImageResource( R.drawable.ic_create_black_24dp )
                show()
            }

        }else if ( bundledCrimeId != NO_ARG && crimeModified  ){
            fab_id.apply {
                setImageResource( R.drawable.ic_file_upload_black_24dp )
                show()
            }
        }else
            fab_id.hide()
    }
}
