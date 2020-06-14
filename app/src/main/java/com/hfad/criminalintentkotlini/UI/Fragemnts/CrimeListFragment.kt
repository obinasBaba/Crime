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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.Util.*
import com.hfad.criminalintentkotlini.UI.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_list_fragment.*


private const val ITEM_TOP_PADDING: Int = 5
const val s = "key2"
class CrimeListFragment : Fragment() {

    private val viewModel : CrimeListViewModel by viewModels( { requireActivity() }, factoryProducer = { Injector.buildFactory( requireActivity().application)} )
    private val recyclerAdapter: RecyclerAdapter by lazy { RecyclerAdapter( SparseBooleanArray() ) }
    private var actionMode: ActionMode? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            =  inflater.inflate(R.layout.crime_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            Toast.makeText( requireContext(), "${it.get(s)}", Toast.LENGTH_SHORT ).show()
        }

        setUpToolBar()
        setUpRecyclerView()
        restoreActionModeState()
        observation()
    }

    private fun setUpToolBar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar_id)
    }

    override fun onStart() {
        super.onStart()
        fab_id.setOnClickListener(Navigation.createNavigateOnClickListener(
            R.id.action_crimeListFragment_to_crimeDetailFragment))
    }

    private fun setUpRecyclerView() {
        zRecyclerView.apply insideRecyclerView@{
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemDecorationImp(ITEM_TOP_PADDING))
            adapter = recyclerAdapter
            addOnItemTouchListener(
                TouchListenerImplementation(
                    requireContext(), this, setupItemSelectionListener(), this@CrimeListFragment)
            )
            with(ItemTouchHelper( supportSwipeToDelete() { onSwipe( it ) } )) {
                attachToRecyclerView(this@insideRecyclerView)
            }
        }
    }

    private fun setupItemSelectionListener(): ClickListeners = object : ClickListeners {
        override fun onClick( pos: Int, view: View ) {
            val crimeId = recyclerAdapter.getCrimeId(pos)!!
            actionMode?.let {
                startActionMode( pos )
            } ?: onCrimeSelected( crimeId )
        }
        override fun onLongClick(pos: Int, view: View) {
            startActionMode(pos)
        }
    }

    private fun onCrimeSelected(crimeId: Int) {
        val actionToCrimeDetailFragment: NavDirections =
            CrimeListFragmentDirections.actionCrimeListFragmentToCrimeDetailFragment(crimeId)
        findNavController().navigate(actionToCrimeDetailFragment)
    }

    private fun onSwipe(viewHolder: RecyclerView.ViewHolder ){
        val crimeViewHolder = viewHolder as RecyclerAdapter.CrimeViewHolder
        crimeViewHolder.let {
            val adapterPosition = it.adapterPosition
            val swipedCrime =
                this@CrimeListFragment.recyclerAdapter.getCrimeAtIndex(adapterPosition)
            viewModel.deleteCrime(swipedCrime)
        }
    }

    private fun restoreActionModeState() {
        viewModel.sparseBoolean.run {
            if (size() > 0) {
                recyclerAdapter.sparseBoolean = this
                startActionMode(null )
            }
        }
    }

    private fun observation() {
        viewModel.crimeList.observe(viewLifecycleOwner, Observer {
            recyclerAdapter.submitList(it)
            recyclerAdapter.scrollToPosition { newCrimeIndex ->
                // zRecyclerView.smoothScrollToPosition( newCrimeIndex )
            }
        })

        viewModel.deleteSelectionPressed.observe( viewLifecycleOwner, Observer {
            deleteSelectedItems()
        })

        viewModel.deselectPressed.observe( viewLifecycleOwner, Observer {
            recyclerAdapter.deselect()
            nullifyActionMode()
        })
    }

    private fun startActionMode(pos: Int?) {
        pos?.let { recyclerAdapter.toggleSelection(pos) }
        val hasSelectedItems: Boolean = recyclerAdapter.sparseBoolean.size() > 0

        if (actionMode == null && hasSelectedItems) {
            actionMode = (requireActivity() as AppCompatActivity).startSupportActionMode(
                ActionModeCallback()
            )
        } else if (actionMode != null && !hasSelectedItems) {
            //Last item deselected, stop actionMode
            actionMode?.finish()
        }
        actionMode?.setTitle(" ${recyclerAdapter.sparseBoolean.size()} ")
            ?: Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
    }

    private fun deleteSelectedItems() {
        viewModel.deleteCrimes(recyclerAdapter)
        actionMode?.finish()
    }

    private fun nullifyActionMode() {
        if (actionMode != null) actionMode = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(context, "OnActivityResult", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.putString( s, "saved state" )
    }

    override fun onDestroy() {
        viewModel.sparseBoolean = recyclerAdapter.sparseBoolean
        super.onDestroy()
    }
}

