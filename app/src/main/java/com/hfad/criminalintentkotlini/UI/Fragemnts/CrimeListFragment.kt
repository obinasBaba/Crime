package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.ViewModels.CrimeListViewModel
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.ActionMode_Imp
import com.hfad.criminalintentkotlini.UI.CrimeActivity
import com.hfad.criminalintentkotlini.UI.TouchListener_Imp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.crime_list_fragment.*

const val INDEX = "index"
const val REQUEST_CODE = 1
private const val READ_BULK = 1

interface ClickListeners{
    fun onClick( pos : Int, view : View )
    fun onLongClick( pos : Int, view : View )
}



class CrimeListFragment : Fragment(), LoaderManager.LoaderCallbacks< Unit >
{
    companion object {
        fun newInstance() = CrimeListFragment()
    }

    private val viewModel: CrimeListViewModel by lazy{ ViewModelProvider( this ).get( CrimeListViewModel::class.java) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter : RecyclerAdapter
    private var actionMode : ActionMode? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate( R.layout.crime_list_fragment, container, false)
        recyclerView = view.findViewById( R.id.zRecyclerView )

        val fab = view.findViewById( R.id.fab_id) as FloatingActionButton
        fab.setOnClickListener{
            startActivityForResult( Intent( context, CrimeActivity::class.java ), 1 )
        }

        if ( viewModel.sparseBoolean.size() > 0 )
            startActionMode( null )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        implementListeners()

        viewModel.getCrimes().observe( this.viewLifecycleOwner , Observer {

            recyclerAdapter.changeList( it )

            if ( viewModel.firstTime)
              viewModel.firstTime = false
        } )

        LoaderManager.getInstance( this ).initLoader( READ_BULK, null, this)

    }

    private fun implementListeners() {
        recyclerView.addOnItemTouchListener(TouchListener_Imp(context, recyclerView,

            object : ClickListeners {
                override fun onClick(pos: Int, view: View) {
                    actionMode?.let{ startActionMode(pos) } ?: recyclerAdapter.run {
                        handleClick( recyclerAdapter.getCrimeId( pos ) )
                    }
                }
                override fun onLongClick(pos: Int, view: View) { startActionMode(pos) }
            }, this )
        )
    }

    private fun initRecycler() {
        recyclerAdapter = RecyclerAdapter.getInstance( viewModel.getCrimes().value ?: emptyList() , viewModel.sparseBoolean )
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager( requireContext() )
    }

    private fun handleClick( itemId: Int ) {
        val intent = Intent( context, CrimeActivity::class.java )
        intent.putExtra( INDEX, itemId )
        startActivityForResult(  intent, REQUEST_CODE )
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
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK )
        {
            val index = data?.getIntExtra( INDEX, -1)
            if ( index != -1 )
            {
               viewModel.firstTime = true

            }else{
                Toast.makeText( context , "Create", Toast.LENGTH_SHORT).show()

            }
        }
    }


    /**                             === === === ===
     *  --===--- == == == = = >>>> LOADER CALLBACKS  === == = = = == = == == ===========
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Unit> {
        Toast.makeText( context , "onCreateLoader", Toast.LENGTH_SHORT).show()

        return object : AsyncTaskLoader< Unit > ( context!! ) {
            override fun loadInBackground() {
                if ( id == READ_BULK )
                    viewModel.readDataFromDatabase()
            }
            override fun onStartLoading() {

                if ( viewModel.firstTime ) {
                    Toast.makeText( context , "onStartLoading", Toast.LENGTH_SHORT).show()
                    forceLoad()
                }
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Unit>, data: Unit?) {
        Toast.makeText( context , "onLoadFinished", Toast.LENGTH_SHORT).show()
    }

    override fun onLoaderReset(loader: Loader<Unit>) {
        Toast.makeText( context , "onLoaderReset", Toast.LENGTH_SHORT).show()
    }

}
