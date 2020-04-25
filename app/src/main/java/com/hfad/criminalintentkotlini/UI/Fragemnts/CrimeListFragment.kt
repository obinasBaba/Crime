package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.content.Intent
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
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.ActionModeImplementation
import com.hfad.criminalintentkotlini.UI.CrimeActivity
import com.hfad.criminalintentkotlini.UI.TouchListenerImplementation

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
    lateinit var recyclerAdapter : RecyclerAdapter
    private var actionMode : ActionMode? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.crime_list_fragment, container, false)
        recyclerView = view.findViewById( R.id.zRecyclerView )

        initRecycler()
        implementListeners()

        if ( viewModel.sparseBoolean.size() > 0 )
            startActionMode( null )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.crimeList.observe( viewLifecycleOwner, Observer {
            recyclerAdapter.changeList( it )
        })
    }

    private fun implementListeners()
    {
        recyclerView.addOnItemTouchListener(
            TouchListenerImplementation(context, recyclerView,

                object : ClickListeners {
                    override fun onClick(pos: Int, view: View) {
                        actionMode?.run { startActionMode(pos) } ?: handleOnClick(pos, view)
                    }
                    override fun onLongClick(pos: Int, view: View) {
                        startActionMode(pos)
                    }
                }, this)
        )
    }

    private fun handleOnClick(pos: Int, view: View ) {
//        Toast.makeText(requireContext(),"OnClick", Toast.LENGTH_SHORT).show()
//        val intent = Intent( context, CrimeActivity::class.java )
//        intent.putExtra( "Index", pos )
//        startActivityForResult( intent, 1)
    }

    private fun startActionMode( pos : Int? ){

        pos?.run{ recyclerAdapter.toggleSelection( pos ) }
        val hasMore : Boolean = recyclerAdapter.sparseBoolean.size() > 0

        if ( actionMode == null && hasMore ){
            //Start actionMode
            actionMode = (activity as AppCompatActivity).startSupportActionMode(
                ActionModeImplementation(recyclerAdapter, this )
            )
        }else if ( actionMode != null && !hasMore ){
            //Last item deselected, stop actionMode
            actionMode?.finish()
        }

        actionMode?.setTitle(  " ${recyclerAdapter.sparseBoolean.size()} "  )
            ?: Toast.makeText(requireContext(),"Null", Toast.LENGTH_SHORT).show()

    }

    private fun initRecycler() {
        recyclerAdapter = RecyclerAdapter(
                viewModel.crimeList.value ?: emptyList()
                , viewModel.sparseBoolean
            )
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
        viewModel.sparseBoolean = recyclerAdapter.sparseBoolean
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(context, "OnActivityResult", Toast.LENGTH_SHORT).show()
    }
}
