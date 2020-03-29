package com.hfad.criminalintentkotlini

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.CrimeListViewModel
import com.hfad.criminalintentkotlini.Views.RecyclerAdapter


class CrimeListFragment : Fragment() {

    companion object {
        fun newInstance() = CrimeListFragment()
    }

    private val viewModel: CrimeListViewModel by lazy{ ViewModelProvider( this ).get( CrimeListViewModel::class.java) }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate(R.layout.crime_list_fragment, container, false) as RecyclerView
        initRecycler()
        implementListeners()
        return recyclerView;
    }

    private fun implementListeners() {}

    private fun initRecycler() {
        recyclerView.adapter = RecyclerAdapter( viewModel.getCrimes() )
        recyclerView.layoutManager = LinearLayoutManager( requireContext() )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}
