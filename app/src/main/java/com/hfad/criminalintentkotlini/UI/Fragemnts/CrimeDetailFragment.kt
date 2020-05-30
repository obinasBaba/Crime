package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.datepicker.MaterialDatePicker
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.Util.Injector
import com.hfad.criminalintentkotlini.Util.PseudoMessageLobby
import com.hfad.criminalintentkotlini.UI.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_fragment.*
import java.util.*
import java.util.concurrent.Executors.newSingleThreadExecutor
import kotlin.properties.Delegates

class CrimeDetailFragment : Fragment()
{
    companion object{
        private const val NO_ARG = -1
        private var bundledCrimeId = NO_ARG
        const val DATE_REQUEST_CODE = 0
        const val CONTACT_REQUEST_CODE = 1
    }

    private val viewModel : CrimeListViewModel by viewModels( { requireActivity() }, factoryProducer =
    { Injector.buildFactory( requireActivity().application)} )

    private val messageLobby = PseudoMessageLobby( newSingleThreadExecutor(), lifecycle )
    private val datePicker : MaterialDatePicker<Long> by lazy { buildDatePicker() }
    private var selectedCrime : Crime by Delegates.observable( Crime() ) { _, _, modifiedCrime ->
        viewModel.crimeModified.value = (modifiedCrime != viewModel.cachedCrime)
        Toast.makeText( context, "dataChanged = ${viewModel.crimeModified.value} ", Toast.LENGTH_SHORT ).show()
        
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.crime_fragment, container, false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) : Unit {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar( toolbar2_id )
        NavigationUI.setupWithNavController( collpsing_layout_id, toolbar2_id, findNavController() )

        bundledCrimeId = CrimeDetailFragmentArgs.fromBundle( requireArguments() ).selectedCrimeId

        observation()
        if (restoreStateIfPresent(savedInstanceState)) return
        initSelectedCrime()
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
            selectedCrime = selectedCrime.apply{ solved = checked }
        }

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            selectedCrime.lastUpdated?.time = selectedDate
            bindViews()
        }
        crime_date.setOnClickListener{
            datePicker.apply {
                show(this@CrimeDetailFragment.parentFragmentManager, "Date_Picker_Frag")
                setTargetFragment(this@CrimeDetailFragment, DATE_REQUEST_CODE)
            }
        }
        send_crime_id.setOnClickListener{
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra( Intent.EXTRA_TEXT, selectedCrime.toString() )
            }
            val appChooser = Intent.createChooser( intent, "Choose MSG App to send - " )
            startActivity( appChooser )
        }
        chooseSuspect_id.setOnClickListener{
            val intent = Intent().apply {
                action = Intent.ACTION_PICK
                data = ContactsContract.Contacts.CONTENT_URI
            }
            startActivity( intent )
        }

        fab_id.setOnClickListener{
            fabEventListener()
        }

        requireActivity().onBackPressedDispatcher.addCallback( viewLifecycleOwner  ){
            onBackPressed()
        }
    }

    private fun observeSelectedCrime() = Delegates.observable( Crime() ) { _, _, modifiedCrime ->
        viewModel.crimeModified.value = !(modifiedCrime == viewModel.cachedCrime)
        Toast.makeText( context, "dataChanged = ${viewModel.crimeModified.value} ", Toast.LENGTH_SHORT ).show()
    }

    private fun bindViews() {
        crime_title.setText(selectedCrime.title)
        crime_date.text = selectedCrime.lastUpdated.toString()
        crime_solved.isChecked = selectedCrime.solved ?: false
    }

    private fun initSelectedCrime() {
        if (bundledCrimeId == NO_ARG) {
            selectedCrime = viewModel.create().copy()
        } else {
            selectedCrime = viewModel.update(bundledCrimeId).copy()
            bindViews()
        }
    }

    private fun restoreStateIfPresent(savedInstanceState: Bundle?): Boolean {
        if (savedInstanceState != null && viewModel.crimeModified.value!!) {
            selectedCrime = viewModel.savedCrime!!
            return true
        }
        return false
    }

    private fun observation() {
        viewModel.crimeModified.observe(viewLifecycleOwner, Observer { crimeModified ->
            fabStateListener(crimeModified)

            if (crimeModified)
                selectedCrime.lastUpdated = Date()  // update Modified date
            else
                selectedCrime.lastUpdated = viewModel.cachedCrime?.lastUpdated  // reverse it if not changed
        })
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
            fab_id.run {
                setImageResource( R.drawable.ic_create_black_24dp )
                show()
            }

        }else if ( bundledCrimeId != NO_ARG && crimeModified  ){
            fab_id.run {
                setImageResource( R.drawable.ic_file_upload_black_24dp )
                show()
            }
        }else
            fab_id.hide()
    }

    private fun buildDatePicker() : MaterialDatePicker<Long>  {
        val datePicker = MaterialDatePicker.Builder.datePicker()
        return datePicker.setTitleText("Select Crime Date")
            .setSelection( selectedCrime.lastUpdated?.time ?: Date().time  )
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( requestCode == DATE_REQUEST_CODE ){
            data?.let { incomingIntent ->
                val lastModifiedDate = incomingIntent.getSerializableExtra(TimeStampFragment.SERIALIZED_DATE) as Date
                selectedCrime.lastUpdated = lastModifiedDate
                bindViews()
            }
        }else if ( requestCode == CONTACT_REQUEST_CODE ){
            data?.let { incommingContactData ->
                val contactUrl = incommingContactData.data
            }
        }
    }

    private fun onBackPressed() {
        if ( viewModel.crimeModified.value!! ) {
            // TODO - ALERT DIALOG
            Toast.makeText( context, "UNSAVE CHANGE", Toast.LENGTH_SHORT).show()
        }

        viewModel.reverse()
        val sucess = findNavController().popBackStack()
        Toast.makeText( context, "$sucess" , Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        Toast.makeText( context, "onDestroy", Toast.LENGTH_SHORT).show()
        if( viewModel.crimeModified.value!! && viewModel.cachedCrime != null ) {
            viewModel.savedCrime = selectedCrime
        }
        super.onDestroy()
    }
}
