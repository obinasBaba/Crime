package com.hfad.criminalintentkotlini.Fragments

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
import com.hfad.criminalintentkotlini.Model.CrimeViewModel
import com.hfad.criminalintentkotlini.R


class CrimeFragment : Fragment() {

    companion object {
        fun newInstance() =
            CrimeFragment()
    }

    //Initialized at first access
    private val viewModel: CrimeViewModel by lazy {  ViewModelProvider( this ).get( CrimeViewModel::class.java ) }
    private lateinit var dateButton: Button
    private lateinit var title: EditText
    private lateinit var solved: CheckBox

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view : View =  inflater.inflate(R.layout.crime_fragment, container, false )

        title = view.findViewById(R.id.crime_title) as EditText
        title.setText( viewModel.getTitle() )

        dateButton = view.findViewById(R.id.crime_date) as Button
        dateButton.apply{
            this.text =  viewModel.getDate()
            isEnabled = false
        }

        solved = view.findViewById(R.id.crime_solved)

        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        title.addTextChangedListener( object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setTitle( s?.toString() ?: "" )
            }

        } )
    }

}
