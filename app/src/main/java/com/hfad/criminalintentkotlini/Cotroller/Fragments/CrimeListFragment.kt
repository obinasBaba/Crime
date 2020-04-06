package com.hfad.criminalintentkotlini.Cotroller.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Cotroller.TouchListener_Imp
import com.hfad.criminalintentkotlini.Model.CrimeListViewModel
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.Views.ActionMode_Imp
import com.hfad.criminalintentkotlini.Views.RecyclerAdapter

interface ClickListeners{
    fun doo(){
        //Default
    }
    fun onClick( pos : Int, view : View )
    fun onLongClick( pos : Int, view : View )
}

class CrimeListFragment : Fragment() {

    companion object {
        fun newInstance() =
            CrimeListFragment()
    }

    private val viewModel: CrimeListViewModel by lazy{ ViewModelProvider( this ).get( CrimeListViewModel::class.java) }
    private lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter : RecyclerAdapter
    private var actionMode : ActionMode? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate(R.layout.crime_list_fragment, container, false) as RecyclerView

        initRecycler()

        if (viewModel.sparseBoolean.size() > 0)
            startActionMode( null )

        implementListeners()    
        return recyclerView;
    }

    private fun implementListeners()
    {
        recyclerView.addOnItemTouchListener(
            TouchListener_Imp(
                context,
                recyclerView,
                object :
                    ClickListeners {
                    override fun onClick(pos: Int, view: View) {
                        actionMode?.run { startActionMode(pos) } ?: handleOnClick(pos, view)

                    }

                    override fun onLongClick(pos: Int, view: View) {
                        startActionMode(pos)
                    }
                },
                this
            )
        )

    }

    private fun handleOnClick(pos: Int, view: View) {
        Toast.makeText(requireContext(),"OnClick", Toast.LENGTH_SHORT).show()
    }

    private fun startActionMode( pos : Int? ){

        pos?.let{recyclerAdapter.toggleSelection( pos ) }
        val hasMore : Boolean = recyclerAdapter.sparseBoolean.size() > 0

        if ( actionMode == null && hasMore ){
            //Start actionMode
            actionMode = (activity as AppCompatActivity).startSupportActionMode(  ActionMode_Imp( recyclerAdapter, this )  )
        }else if ( actionMode != null && !hasMore ){
            //Last item deselected, stop actionMode
            actionMode?.finish()
        }

        actionMode?.setTitle(  " ${recyclerAdapter.sparseBoolean.size()} "  )
            ?: Toast.makeText(requireContext(),"Null", Toast.LENGTH_SHORT).show()

    }

    private fun initRecycler() {
        recyclerAdapter =  RecyclerAdapter( viewModel.getCrimes(), viewModel.sparseBoolean )
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager( requireContext() )
    }

    fun deleteSelectedItems() : Unit {

        val selectedSize = recyclerAdapter.sparseBoolean.size() - 1

        for (i in selectedSize downTo 0) {
            if (recyclerAdapter.sparseBoolean.valueAt(i)) {
                //If current id is selected remove the item via key
                viewModel.remove( recyclerAdapter.sparseBoolean.keyAt( i ) )
                recyclerAdapter.notifyItemRemoved( recyclerAdapter.sparseBoolean.keyAt( i ) )
//                recyclerAdapter.notifyDataSetChanged() //notify adapter
            }
        }

        actionMode?.finish()
    }


    fun nullifyActionMode(){
        if ( actionMode != null )
            actionMode = null
    }

    override fun onDestroy() {
        super.onDestroy()
      //  viewModel.sparseBoolean = recyclerAdapter.sparseBoolean
    }

}
