package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.ActionMode_Imp
import com.hfad.criminalintentkotlini.UI.CrimeActivity
import com.hfad.criminalintentkotlini.UI.TouchListener_Imp

 const val INDEX = "index"

interface ClickListeners{
    fun onClick( pos : Int, view : View )
    fun onLongClick( pos : Int, view : View )
}

class CrimeListFragment : Fragment() {
    companion object {
        fun newInstance() = CrimeListFragment()
    }

    private val viewModel: CrimeListViewModel by lazy{ ViewModelProvider( this ).get( CrimeListViewModel::class.java) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter : RecyclerAdapter
    private var actionMode : ActionMode? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recyclerView = inflater.inflate( R.layout.crime_list_fragment, container, false) as RecyclerView

        initRecycler()
        implementListeners()
        if ( viewModel.sparseBoolean.size() > 0 )
            startActionMode( null )
        return recyclerView;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCrimesCursor().observe( this.viewLifecycleOwner , Observer {
            recyclerAdapter.changeCursor( it )  })
    }

    private fun implementListeners() {
        recyclerView.addOnItemTouchListener(TouchListener_Imp(context, recyclerView,

                object : ClickListeners {
                    override fun onClick(pos: Int, view: View) {
                        actionMode?.let{ startActionMode(pos) } ?: recyclerAdapter.run {
                            getItemId( pos )
                        }
                    }
                    override fun onLongClick(pos: Int, view: View) { startActionMode(pos) }
                }, this )
        )
    }



    private fun handleClick() {
        val intent = Intent( context, CrimeActivity::class.java )
        intent.putExtra(INDEX, 1 )
        startActivityForResult(  intent, 1 )
    }

    private fun startActionMode( pos : Int? ){

        pos?.run{ recyclerAdapter.toggleSelection( pos ) }
        val hasMore : Boolean = recyclerAdapter.sparseBoolean.size() > 0

        if ( actionMode == null && hasMore ){
            //Start actionMode
            actionMode = (activity as AppCompatActivity).startSupportActionMode(
                ActionMode_Imp( recyclerAdapter, this ))
        }else if ( actionMode != null && !hasMore ){
            //Last item deselected, stop actionMode
            actionMode?.finish()
        }

        actionMode?.setTitle(  " ${recyclerAdapter.sparseBoolean.size()} "  )
            ?: Toast.makeText(requireContext(),"Null", Toast.LENGTH_SHORT).show()

    }

    private fun initRecycler() {
        recyclerAdapter = RecyclerAdapter.getInstance( null , viewModel.sparseBoolean )
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager( requireContext() )
    }

    fun deleteSelectedItems()  {
        actionMode?.finish()
    }

    fun nullifyActionMode(){
        if ( actionMode != null )
            actionMode = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.nullifyDB()
        viewModel.sparseBoolean = recyclerAdapter.sparseBoolean
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(context, "OnActivityResult", Toast.LENGTH_SHORT).show()
    }
}
