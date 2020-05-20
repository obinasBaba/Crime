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
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.Util.*
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import kotlinx.android.synthetic.main.crime_list_fragment.*



class CrimeListFragment : Fragment() {

    private val ITEM_TOP_PADDING: Int = 5

    val recyclerAdapter: RecyclerAdapter by lazy { RecyclerAdapter( SparseBooleanArray() ) }

    private val viewModel by activityViewModels<CrimeListViewModel>(  )
//    private lateinit var recyclerView: RecyclerView
    private var actionMode: ActionMode? = null

    ///Initializing Views only
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.crime_list_fragment, container, false)
    }

    ///Initializing necessary references
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.sparseBoolean.apply {
            if ( size() > 0 ) {
                recyclerAdapter.sparseBoolean = this
                startActionMode( null )
                Toast.makeText( requireContext(), "", Toast.LENGTH_SHORT ).show()
            }
        }

        viewModel.crimeList.observe( viewLifecycleOwner, Observer {
            recyclerAdapter.submitList( it )

            recyclerAdapter.scrollToPosition { newCrimeIndex ->
               // zRecyclerView.smoothScrollToPosition( newCrimeIndex )
            }
        } )

        fab_id.setOnClickListener( Navigation.createNavigateOnClickListener( R.id.action_crimeListFragment_to_crimeDetailFragment ) )
    }

    private fun setUpRecyclerView() {
        zRecyclerView.apply insideRecyclerView@{
            adapter = recyclerAdapter  // Adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration( ItemDecorationImp( ITEM_TOP_PADDING ) )
//            addItemDecoration( ItemTouchHelper( supportSwipeToDelete() ))
            addOnItemTouchListener(
                TouchListenerImplementation(
                    requireContext(), this, implementListeners(), this@CrimeListFragment
                )
            )

            with( ItemTouchHelper(  supportSwipeToDelete() ) ){
                attachToRecyclerView( this@insideRecyclerView)
            }
        }
    }

    private fun supportSwipeToDelete() : Callback =
        object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int = makeMovementFlags( 0, RIGHT  )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val crimeViewHolder = viewHolder as RecyclerAdapter.CrimeViewHolder
                crimeViewHolder.let {
                    val adapterPosition = it.adapterPosition
                    val swipedCrime = this@CrimeListFragment.recyclerAdapter.getCrimeAtIndex( adapterPosition )
                    viewModel.deleteCrime( swipedCrime )
                }
            }
        }

    private fun implementListeners() : ClickListeners = object :
        ClickListeners {
            override fun onClick(pos: Int, view: View) {
                val crimeRealId = recyclerAdapter.getCrimeId(pos)!!
                actionMode?.run {
                    startActionMode(pos)
                } ?: onCrimeSelected(crimeRealId )
            }
            override fun onLongClick(pos: Int, view: View) {
                startActionMode( pos )
            }
        }

    private fun onCrimeSelected( crimeRealId: Int ) {
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
                ActionModeImplementation(
                    recyclerAdapter,
                    this
                )
            )

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
