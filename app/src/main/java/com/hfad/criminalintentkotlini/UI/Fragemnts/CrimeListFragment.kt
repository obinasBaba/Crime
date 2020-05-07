package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.content.Intent
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.ActionModeImplementation
import com.hfad.criminalintentkotlini.UI.TouchListenerImplementation
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_list_fragment.*

interface ClickListeners {
    fun onClick(pos: Int, view: View)
    fun onLongClick(pos: Int, view: View)
}

class CrimeListFragment : Fragment() {

    var recyclerAdapter: RecyclerAdapter = RecyclerAdapter( emptyList(), SparseBooleanArray() )
    private val viewModel by activityViewModels<CrimeListViewModel>(  )
    private lateinit var recyclerView: RecyclerView
    private var actionMode: ActionMode? = null

    companion object {
        fun newInstance() = CrimeListFragment()
    }

    ///Initializing Views only
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.crime_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.zRecyclerView)
        return view
    }

    ///Initializing necessary references
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        implementListeners()

        viewModel.sparseBoolean.apply {
            if ( size() > 0 ) {
                recyclerAdapter.sparseBoolean = this
                startActionMode( null )
            }
        }

        viewModel.crimeList.observe( viewLifecycleOwner, Observer {
            recyclerAdapter.changeList( it )
        } )

        fab_id.setOnClickListener(
            Navigation.createNavigateOnClickListener( R.id.action_crimeListFragment_to_crimeDetailFragment ) )
    }

    private fun initRecycler() {
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun implementListeners() {
        recyclerView.addOnItemTouchListener(
            TouchListenerImplementation(context, recyclerView,

                object : ClickListeners {
                    override fun onClick(pos: Int, view: View) {
                        val crimeRealId = recyclerAdapter.getCrimeId(pos)!!
                        actionMode?.run {
                            startActionMode(pos)
                        } ?: onCrimeSelected(crimeRealId, view)
                    }
                    override fun onLongClick(pos: Int, view: View) {
                        startActionMode(pos)
                    }
                }, this
            )
        )
    }


    private fun onCrimeSelected(crimeRealId: Int, view: View) {
        val actionToCrimeDetailFragment : NavDirections =
            CrimeListFragmentDirections.actionCrimeListFragmentToCrimeDetailFragment( crimeRealId )
        findNavController().navigate( actionToCrimeDetailFragment )
    }

    private fun startActionMode(pos: Int?) {
        pos?.run { recyclerAdapter.toggleSelection(pos) }
        val hasMore: Boolean = recyclerAdapter.sparseBoolean.size() > 0

        if (actionMode == null && hasMore) {
            //Start actionMode
            actionMode = (activity as AppCompatActivity).startSupportActionMode(
                ActionModeImplementation(recyclerAdapter, this))

        } else if (actionMode != null && !hasMore) {
            //Last item deselected, stop actionMode
            actionMode?.finish()
        }
        actionMode?.setTitle(" ${recyclerAdapter.sparseBoolean.size()} ")
            ?: Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
    }

    fun deleteSelectedItems() {
        viewModel.deleteCrimes(recyclerAdapter)
        actionMode?.finish()
    }

    fun nullifyActionMode() {
        if (actionMode != null) actionMode = null


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(context, "OnActivityResult", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.sparseBoolean = recyclerAdapter.sparseBoolean
    }
}
