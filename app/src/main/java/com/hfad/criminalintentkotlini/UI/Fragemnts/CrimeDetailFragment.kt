package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_fragment.*
import java.util.*
import kotlin.properties.Delegates


class CrimeDetailFragment : Fragment()
{
    companion object{
        private const val NO_ARG = -1
        private var bundledCrimeId = NO_ARG
        const val DATE_REQUEST_CODE = 0
    }

    private val datePicker : MaterialDatePicker<Long> by lazy { buildDatePicker() }

    private var selectedCrime : Crime by observeSelectedCrime()
    // Initialize the viewModel by lazy
    private val viewModel: CrimeListViewModel by activityViewModels()


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.crime_fragment, container, false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) : Unit {
        super.onViewCreated(view, savedInstanceState)

        bundledCrimeId = CrimeDetailFragmentArgs.fromBundle( requireArguments() ).selectedCrimeId

        // Track the state of fab icon with respect to crimeModification for update and create
        viewModel.crimeModified.observe( viewLifecycleOwner, Observer { crimeModified ->
            fabStateListener( crimeModified )

            if ( crimeModified ) selectedCrime.lastUpdated = Date()  // update Modified date
            else selectedCrime.lastUpdated = viewModel.cachedCrime?.lastUpdated  // reverse it if not changed
        } )

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
        crime_date.text = selectedCrime.lastUpdated.toString()
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

        crime_date.setOnClickListener{
            datePicker.apply {
                show(this@CrimeDetailFragment.parentFragmentManager, "Date_Picker_Frag")
                setTargetFragment(this@CrimeDetailFragment, DATE_REQUEST_CODE)
            }
        }

        datePicker.addOnPositiveButtonClickListener { selectedDate ->

            selectedCrime.lastUpdated?.time = selectedDate
            bindViews()

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { incomingIntent ->
            val lastModifiedDate = incomingIntent.getSerializableExtra(TimeStampFragment.SERIALIZED_DATE) as Date
            selectedCrime.lastUpdated = lastModifiedDate
            bindViews()
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


    private fun observeSelectedCrime() = Delegates.observable( Crime() ) { _, _, newCrime ->
        viewModel.crimeModified.value = !( newCrime equals  viewModel.cachedCrime )
        Toast.makeText( context, "dataChanged = ${viewModel.crimeModified.value} ", Toast.LENGTH_SHORT ).show()
    }
    private fun buildDatePicker() : MaterialDatePicker<Long>  {
        val datePicker = MaterialDatePicker.Builder.datePicker()
        
        return datePicker.setTitleText("Select Crime Date")
            .setSelection( selectedCrime.lastUpdated?.time ?: Date().time  )
            .build()
    }
}
