package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.ViewModels.CrimeViewModel
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import java.lang.IllegalStateException
import java.util.*

class CrimeFragment : Fragment() {

    companion object {
        fun newInstance() =
            CrimeFragment()
    }

    private val viewModel: CrimeListViewModel by lazy{ ViewModelProvider( this ).get( CrimeListViewModel::class.java) }

    private var index = 0
    //Initialized at first access
    private lateinit var dateButton: Button
    private lateinit var title: EditText
    private lateinit var solved: CheckBox
    private lateinit var editableCrime : Crime



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val view : View =  inflater.inflate(R.layout.crime_fragment, container, false )
        title = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solved = view.findViewById(R.id.crime_solved)
        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        index = activity?.intent?.getIntExtra( "Index", 0 ) ?: throw IllegalStateException("No Bundle send")

        editableCrime =  viewModel.getCrimes().value?.elementAt( index ) ?: Crime()

        title.setText( editableCrime.title ?: "Not Found" )
        dateButton.apply{
            this.text =  editableCrime.date.toString()
            isEnabled = false
        }
    }

    override fun onStart() {
        super.onStart()
        title.addTextChangedListener( object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editableCrime.title = s.toString()
            }
        } )
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
